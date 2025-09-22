package com.github.ojvzinn.desafiopicpay.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class TransactionDTO {

    @NotNull
    private Long payerID;

    @NotNull
    private Long payeeID;

    @Min(value = 0)
    @NotNull
    private BigDecimal amount;

}
