package com.github.ojvzinn.desafiopicpay.configuration;

import com.github.ojvzinn.desafiopicpay.service.TransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalConfiguration {

    @Bean
    public TransactionService transactionService() {
        return new TransactionService();
    }

}
