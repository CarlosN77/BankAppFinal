package com.projeto.bankapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories("com.projeto.bankapp")
@SpringBootApplication
public class BankAppApplication {



    public static void main(String[] args) {
        SpringApplication.run(BankAppApplication.class, args);
    }

}
