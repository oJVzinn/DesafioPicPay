package com.github.ojvzinn.desafiopicpay.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MessageEntity {

    private Long receiverID;
    private String message;

}
