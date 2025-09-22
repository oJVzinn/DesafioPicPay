package com.github.ojvzinn.desafiopicpay.controller;

import com.github.ojvzinn.desafiopicpay.dto.AddBalanceDTO;
import com.github.ojvzinn.desafiopicpay.dto.UserDTO;
import com.github.ojvzinn.desafiopicpay.entity.UserEntity;
import com.github.ojvzinn.desafiopicpay.enums.UserType;
import com.github.ojvzinn.desafiopicpay.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/comum/create")
    public ResponseEntity<Void> createComumUser(@Valid @RequestBody UserDTO userDTO) {
        service.createUser(userDTO, UserType.COMUM);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/trader/create")
    public ResponseEntity<Void> createTraderUser(@Valid @RequestBody UserDTO userDTO) {
        service.createUser(userDTO, UserType.TRADER);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/comum/update/{id}")
    public ResponseEntity<Void> updateComumUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        service.updateUser(id, userDTO, UserType.COMUM);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/trader/update/{id}")
    public ResponseEntity<Void> updateTraderUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        service.updateUser(id, userDTO, UserType.TRADER);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> findUser(@PathVariable Long id) {
        UserEntity user = service.findByID(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(user.toJSON().toString());
    }

    @PatchMapping("/add/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody @Valid AddBalanceDTO addBalanceDTO) {
        boolean isAccept = service.addBalanceToUser(id, addBalanceDTO.getBalance());
        if (!isAccept) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

}
