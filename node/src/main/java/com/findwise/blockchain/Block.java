package com.findwise.blockchain;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.findwise.utils.StringUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Log4j2
@Data
@JsonRootName("block")
@NoArgsConstructor
public class Block {

    private String hash;
    private String previousHash;
    private File file;
    private long timeStamp;
    private int nonce;
    private String fileContent;

    Block(File file, String previousHash) {
        this.file = file;
        this.previousHash = previousHash;
        this.timeStamp = Instant.now().toEpochMilli();
        this.hash = calculateHash();
        this.fileContent = encodeFileToBase64(this.file);
        mineBlock();
    }

    String calculateHash() {
        log.debug("Calculating hash for current Block");
        return StringUtil.applySha256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + file.getName());
    }

    private void mineBlock() {
        String target = new String(new char[DataChain.DIFFICULTY]).replace('\0', '0');
        while (!hash.substring(0, DataChain.DIFFICULTY).equals(target)) {
            ++nonce;
            hash = calculateHash();
        }
        log.info("Block Mined!!! : " + hash);
    }

    private String encodeFileToBase64(File file) {
        try {
            byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
            return new String(encoded, StandardCharsets.US_ASCII);
        } catch (IOException e) {
            return "";
        }
    }
}