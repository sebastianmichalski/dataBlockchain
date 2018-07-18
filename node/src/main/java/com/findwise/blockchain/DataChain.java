package com.findwise.blockchain;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.LinkedList;

@Log4j2
@Getter
@Component
public class DataChain {

    @PostConstruct
    public void init() {
        // TODO synchronization with existing blockchain
        // TODO if it is first initialization, we need to create genesis block

        blockchain.add(new Block(new File(""), "0"));
        log.info("Blockchain initialized, genesis block mined!");
        log.info("Is blockchain valid: ", isChainValid());

    }

    public static int DIFFICULTY = 5;
    private LinkedList<Block> blockchain = new LinkedList<>();

    public boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[DIFFICULTY]).replace('\0', '0');

        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                log.info("Current hash not equal");
                return false;
            }
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                log.info("Previous hash not equal");
                return false;
            }
            if (!currentBlock.getHash().substring(0, DIFFICULTY).equals(hashTarget)) {
                log.info("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }

}
