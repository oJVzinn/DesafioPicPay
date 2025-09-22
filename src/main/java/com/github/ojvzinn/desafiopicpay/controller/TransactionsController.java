package com.github.ojvzinn.desafiopicpay.controller;

import com.github.ojvzinn.desafiopicpay.dto.TransactionDTO;
import com.github.ojvzinn.desafiopicpay.entity.TransactionEntity;
import com.github.ojvzinn.desafiopicpay.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionsController {

    @Autowired
    private TransactionService service;

    @PostMapping("/create")
    public ResponseEntity<Void> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        service.createTransaction(transactionDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> findTransaction(@PathVariable Long id) {
        TransactionEntity transaction = service.findTransactionByID(id);
        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(transaction.toJSON().toString());
    }

}
