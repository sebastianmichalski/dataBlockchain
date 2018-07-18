package com.findwise.spring;

import com.findwise.blockchain.DataChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.findwise.*")
public class Application {

    @Autowired
    private DataChain dataChain;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}