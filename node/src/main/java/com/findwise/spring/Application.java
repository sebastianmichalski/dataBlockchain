package com.findwise.spring;

import com.findwise.blockchain.Block;
import com.findwise.blockchain.DataChain;
import com.findwise.blockchain.Blockchain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "com.findwise.blockchain")
@RestController
@EnableScheduling
public class Application {

    @Autowired
    private DataChain dataChain;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public Blockchain getBlockchain() {
        Blockchain blockchain = new Blockchain();
        blockchain.setList(dataChain.getBlockchain());
        return blockchain;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/size")
    public Integer getBlockchainSize() {
        return dataChain.getBlockchain().size();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity addBlock(@RequestBody Block block) {
        dataChain.getBlockchain().add(block);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}