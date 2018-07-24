package com.findwise.spring;

import com.findwise.blockchain.Block;
import com.findwise.blockchain.Blockchain;
import com.findwise.blockchain.DataChain;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import static com.findwise.utils.StringUtil.BLOCKCHAIN_STORAGE;

@SpringBootApplication(scanBasePackages = "com.findwise.blockchain")
@RestController
@EnableScheduling
@Log4j2
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

    @RequestMapping(method = RequestMethod.POST, path = "/store")
    public ResponseEntity storeBlockchain() {
        try (FileWriter file = new FileWriter(BLOCKCHAIN_STORAGE)) {
            file.write(new Gson().toJson(dataChain.getBlockchain()));
        } catch (IOException e) {
            log.error("Cannot store blockchain", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/restore", produces = "application/octet-stream")
    public ResponseEntity restoreFileFromBlockIndex(Integer index) {
        final Optional<Block> block = dataChain.getBlockchain().stream().filter(b -> b.getBlockIndex().equals(index)).findAny();
        if (block.isPresent()) {
            return ResponseEntity.ok()
                                 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + block.get().getFile().getName() + "\"")
                                 .body(Base64.decodeBase64(block.get().getFileContent()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}