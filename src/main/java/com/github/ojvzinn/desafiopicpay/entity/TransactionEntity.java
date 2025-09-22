package com.github.ojvzinn.desafiopicpay.entity;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionEntity {

    private Long id;
    private Long payerID;
    private Long payeeID;
    private BigDecimal amount;

    public void fromJSON(JSONObject json) {
        this.id = json.getLong("id");
        this.payerID = json.getLong("payerID");
        this.payeeID = json.getLong("payeeID");
        this.amount = json.getBigDecimal("amount");
    }

    public JSONObject toJSON() {
        JSONObject response = new JSONObject();
        response.put("id", this.id);
        response.put("payerID", this.payerID);
        response.put("payeeID", this.payeeID);
        response.put("amount", this.amount);
        return response;
    }

}
