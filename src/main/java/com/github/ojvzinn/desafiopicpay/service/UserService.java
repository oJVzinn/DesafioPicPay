package com.github.ojvzinn.desafiopicpay.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.ojvzinn.desafiopicpay.dto.UserDTO;
import com.github.ojvzinn.desafiopicpay.entity.UserEntity;
import com.github.ojvzinn.desafiopicpay.enums.UserType;
import com.github.ojvzinn.desafiopicpay.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository repository = new UserRepository();
    private final Cache<Long, UserEntity> CACHE = Caffeine.newBuilder().expireAfterWrite(1L, TimeUnit.HOURS).build();

    public void createUser(UserDTO userDTO, UserType userType) {
        checkUser(userDTO, userType);
        repository.createUser(userDTO.getName(), userDTO.getRegister(), userDTO.getEmail(), userDTO.getPassword(), new BigDecimal("0"), userType.getId());
    }

    public UserEntity findByID(Long id) {
        UserEntity user = CACHE.getIfPresent(id);
        if (user == null) {
            JSONObject info = repository.findUserByID(id);
            if (info.isEmpty()) {
                return null;
            }

            user = new UserEntity();
            user.fromJSON(info);
            CACHE.put(id, user);
        }

        return user;
    }

    public void updateUser(Long id, UserDTO userDTO, UserType userType) {
        checkUser(userDTO, userType);
        UserEntity user = findByID(id);
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRegister(userDTO.getRegister());
        repository.updateUser(user.getId(), user.getName(), user.getRegister(), user.getEmail(), user.getPassword(), user.getBalance(), user.getUserTypeID());
        CACHE.put(id, user);
    }

    public void updateUser(UserEntity user) {
        repository.updateUser(user.getId(), user.getName(), user.getRegister(), user.getEmail(), user.getPassword(), user.getBalance(), user.getUserTypeID());
        CACHE.put(user.getId(), user);
    }

    public boolean addBalanceToUser(Long id, BigDecimal balance) {
        UserEntity user = findByID(id);
        if (user == null) {
            return false;
        }

        BigDecimal oldBalance = user.getBalance();
        user.setBalance(oldBalance.add(balance).setScale(2, BigDecimal.ROUND_HALF_UP));
        repository.updateUser(user.getId(), user.getName(), user.getRegister(), user.getEmail(), user.getPassword(), user.getBalance(), user.getUserTypeID());
        CACHE.put(id, user);
        return true;
    }

    private void checkUser(UserDTO userDTO, UserType userType) {
        String register = userDTO.getRegister().replaceAll("\\d", "");
        Pattern pattern = Pattern.compile("^\\d{11}$\n");
        if (userType == UserType.COMUM && pattern.matcher(register).matches()) {
            throw new IllegalArgumentException("Invalid register");
        }

        pattern = Pattern.compile("^\\d{14}$\n");
        if (userType == UserType.TRADER && pattern.matcher(register).matches()) {
            throw new IllegalArgumentException("Invalid register");
        }

        if (repository.containsUserWithEmailAndRegister(userDTO.getEmail(), userDTO.getRegister())) {
            throw new IllegalArgumentException("User with email or register already exists");
        }
    }
}
