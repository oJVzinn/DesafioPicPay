package com.github.ojvzinn.desafiopicpay.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.ojvzinn.desafiopicpay.dto.TransactionDTO;
import com.github.ojvzinn.desafiopicpay.entity.MessageEntity;
import com.github.ojvzinn.desafiopicpay.entity.TransactionEntity;
import com.github.ojvzinn.desafiopicpay.entity.UserEntity;
import com.github.ojvzinn.desafiopicpay.entity.exception.TransactionException;
import com.github.ojvzinn.desafiopicpay.entity.exception.UserException;
import com.github.ojvzinn.desafiopicpay.enums.UserType;
import com.github.ojvzinn.desafiopicpay.repository.TransactionRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionService {

    private final TransactionRepository repository = new TransactionRepository();

    private final Queue<MessageEntity> messages = new LinkedList<>();

    private final Cache<Long, TransactionEntity> CACHE = Caffeine.newBuilder().expireAfterWrite(1L, TimeUnit.HOURS).build();

    @Autowired
    private UserService userService;

    public TransactionEntity findTransactionByID(Long id) {
        TransactionEntity transaction = CACHE.getIfPresent(id);
        if (transaction == null) {
            JSONObject info = repository.findTransactionByID(id);
            if (info.isEmpty()) {
                return null;
            }

            transaction = new TransactionEntity();
            transaction.fromJSON(info);
            CACHE.put(id, transaction);
        }

        return transaction;
    }

    public void createTransaction(TransactionDTO transactionDTO) {
        try {
            URL url = new URL("https://util.devi.tools/api/v2/authorize");
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            request.disconnect();
            if (responseCode == 403) {
                throw new TransactionException("Error authenticating");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        UserEntity payer = userService.findByID(transactionDTO.getPayerID());
        UserEntity payee = userService.findByID(transactionDTO.getPayeeID());
        if (payer == null || payee == null) {
            throw new UserException("user not found");
        }

        if (payer.getUserType().equals(UserType.TRADER)) {
            throw new TransactionException("trader not allowed");
        }

        BigDecimal oldBalance = payer.getBalance();
        payer.setBalance(oldBalance.subtract(transactionDTO.getAmount()));

        oldBalance = payee.getBalance();
        payee.setBalance(oldBalance.add(transactionDTO.getAmount()));

        userService.updateUser(payer);
        userService.updateUser(payee);
        repository.createTransaction(payer.getId(), payee.getId(), transactionDTO.getAmount());
        messages.add(new MessageEntity(payer.getId(), "Você recebeu uma mensagem :)"));
    }

    @Scheduled(fixedRate = 5000)
    public void messageTask() {
        MessageEntity messageEntity = messages.poll();
        if (messageEntity != null) {
            UserEntity userEntity = userService.findByID(messageEntity.getReceiverID());
            if (userEntity != null) {
                try {
                    URL url = new URL("https://util.devi.tools/api/v1/notify");
                    HttpURLConnection request = (HttpURLConnection) url.openConnection();
                    request.setRequestMethod("POST");
                    request.setRequestProperty("Content-Type", "application/json");
                    int responseCode = request.getResponseCode();
                    request.disconnect();
                    if (responseCode == 504) {
                        messages.add(messageEntity);
                        return;
                    }

                    System.out.println(messageEntity.getMessage());
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }

}
