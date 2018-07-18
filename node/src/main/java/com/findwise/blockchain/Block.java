package com.findwise.blockchain;

import com.findwise.utils.StringUtil;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.time.Instant;

@Log4j2
@Data
public class Block {

    private String hash;
    private String previousHash;
    private File data;
    private long timeStamp;
    private int nonce;

    public Block(File data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = Instant.now().toEpochMilli();
        this.hash = calculateHash();
        mineBlock(DataChain.DIFFICULTY);
    }

    String calculateHash() {
        log.debug("Calculating hash for current Block");
        return StringUtil.applySha256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + data.getName());
    }

    private void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            ++nonce;
            hash = calculateHash();
        }
        log.info("Block Mined!!! : " + hash);
    }

}