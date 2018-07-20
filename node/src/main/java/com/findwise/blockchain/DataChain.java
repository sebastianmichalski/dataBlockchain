package com.findwise.blockchain;

import com.findwise.exceptions.InvalidBlockchainException;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.*;

@Log4j2
@Getter
@Component
public class DataChain {

    public static final String PORT = ":8080";
    public static final String SERVER_IP = "http://10.5.0.5" + PORT;
    public static final int DIFFICULTY = 5;
    private LinkedList<Block> blockchain = new LinkedList<>();

    @PostConstruct
    public void init() {
        RestTemplate restTemplate = new RestTemplate();

        try {
            final HttpStatus response = restTemplate.postForObject(new URI(SERVER_IP), Inet4Address.getLocalHost().getHostAddress(), HttpStatus.class);
            if (!HttpStatus.OK.equals(response))
                throw new RestClientException("Could not connect to server: " + SERVER_IP);
            final JSONArray nodes = getNodes(restTemplate);
            if (Objects.requireNonNull(nodes).length() > 1) {
                synchronizeBlockchain(restTemplate, nodes);
            } else {
                initBlockchain();
            }
        } catch (UnknownHostException e) {
            log.error("Unknown host exception: ", e);
        } catch (URISyntaxException e) {
            log.error("URI has not proper syntax: ", e);
        } catch (JSONException e) {
            log.error("JSON parsing exception: ", e);
        } catch (InvalidBlockchainException e) {
            log.error("Invalid blockchain synchronized: ", e);
        } catch (IOException e) {
            log.error("Fail to store file in blockchain: ", e);
        }
    }

    static JSONArray getNodes(RestTemplate restTemplate) throws JSONException {
        return new JSONArray(restTemplate.getForObject(SERVER_IP, String.class));
    }

    private void synchronizeBlockchain(RestTemplate restTemplate, JSONArray nodes) throws JSONException, InvalidBlockchainException, IOException {
        Map<Integer, String> map = new HashMap<>();
        Integer blockchainSize = 0;
        for (int i = 0; i < nodes.length(); i++) {
            final String host = nodes.getString(i);
            String nodeIp = "http://" + host;
            if (!Inet4Address.getLocalHost().getHostAddress().equals(host)) {
                try {
                    blockchainSize = restTemplate.getForObject(nodeIp + PORT + "/size", Integer.class);
                } catch (RestClientException e) {
                    log.info("Host unreachable: ", host);
                }
                map.put(blockchainSize, nodeIp);
            }
        }
        final OptionalInt longestBlockchainSize = map.keySet().stream().mapToInt(Integer::intValue).max();
        if (longestBlockchainSize.orElse(0) == 0) {
            initBlockchain();
        } else {
            blockchain = Objects.requireNonNull(restTemplate.getForObject(map.get(longestBlockchainSize.getAsInt()) + PORT, Blockchain.class)).getList();
        }
        if (!isChainValid()) {
            throw new InvalidBlockchainException();
        }
    }

    private void initBlockchain() {
        blockchain.add(new Block(new File(""), "0", 1));
        log.info("Blockchain initialized, genesis block mined!");
    }

    private boolean isChainValid() {
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
