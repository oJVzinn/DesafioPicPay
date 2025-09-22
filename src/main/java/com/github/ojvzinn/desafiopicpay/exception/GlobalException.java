package com.github.ojvzinn.desafiopicpay.exception;

import com.github.ojvzinn.desafiopicpay.entity.exception.TransactionException;
import com.github.ojvzinn.desafiopicpay.entity.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> illegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler({UserException.class})
    public ResponseEntity<String> userException(UserException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({TransactionException.class})
    public ResponseEntity<String> transactionException(TransactionException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

}
