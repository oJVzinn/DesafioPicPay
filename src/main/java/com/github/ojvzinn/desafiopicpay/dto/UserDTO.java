package com.github.ojvzinn.desafiopicpay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UsersDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String register;
    
}
