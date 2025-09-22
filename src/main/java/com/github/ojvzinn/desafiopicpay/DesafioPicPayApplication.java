package com.github.ojvzinn.desafiopicpay;

import com.github.ojvzinn.desafiopicpay.database.Database;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DesafioPicPayApplication {

    public static void main(String[] args) {
        Database.init();
        SpringApplication.run(DesafioPicPayApplication.class, args);
    }

}
