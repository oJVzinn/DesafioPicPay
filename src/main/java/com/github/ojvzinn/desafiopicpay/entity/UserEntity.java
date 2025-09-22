package com.github.ojvzinn.desafiopicpay.entity;

import com.github.ojvzinn.desafiopicpay.enums.UserType;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.math.BigDecimal;

@Getter
@Setter
public class UserEntity {

    private long id;
    private String name;
    private String register;
    private String email;
    private String password;
    private BigDecimal balance;
    private int userTypeID;

    public UserType getUserType() {
        return UserType.findByID(userTypeID);
    }

    public void fromJSON(JSONObject json) {
        this.id = json.getLong("id");
        this.name = json.getString("name");
        this.register = json.getString("register");
        this.email = json.getString("email");
        this.password = json.getString("password");
        this.balance = json.getBigDecimal("balance").setScale(2, BigDecimal.ROUND_HALF_UP);
        this.userTypeID = json.getInt("userTypeID");
    }

    public JSONObject toJSON() {
        JSONObject response = new JSONObject();
        response.put("id", this.id);
        response.put("name", this.name);
        response.put("register", this.register);
        response.put("email", this.email);
        response.put("password", this.password);
        response.put("balance", this.balance.setScale(2, BigDecimal.ROUND_HALF_UP));
        response.put("userTypeID", this.userTypeID);
        return response;
    }
}
