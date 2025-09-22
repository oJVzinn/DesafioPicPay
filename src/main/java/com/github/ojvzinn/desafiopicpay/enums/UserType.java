package com.github.ojvzinn.desafiopicpay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum UserType {

    COMUM(0),
    TRADER(1);

    private final int id;

    public static UserType findByID(int id) {
        return Arrays.stream(values()).filter(userType -> userType.getId() == id).findFirst().orElse(null);
    }

}
