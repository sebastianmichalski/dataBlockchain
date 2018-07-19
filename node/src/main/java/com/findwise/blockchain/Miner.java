package com.findwise.blockchain;

import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
@Log4j2
public class Miner {

    private DataChain dataChain;
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public Miner(DataChain dataChain) {
        this.dataChain = dataChain;
    }

    @Scheduled(cron = "0 * * * * *")
    public void mine() throws IOException {
        log.info("Scheduled task START");
        try (Stream<Path> paths = Files.walk(Paths.get("/files"))) {
            paths.filter(Files::isRegularFile).forEach(f -> {
                String fileName = f.getFileName().toString();
                log.info("File to be added: ", fileName);
                try {
                    Block block = new Block(f.toFile(), dataChain.getBlockchain().getLast().getHash());
                    dataChain.getBlockchain().add(block);
                    final JSONArray nodes;
                    nodes = DataChain.getNodes(restTemplate);
                    for (int i = 0; i < nodes.length(); i++) {
                        final String host = nodes.getString(i);
                        String nodeIp = "http://" + host;
                        if (!Inet4Address.getLocalHost().getHostAddress().equals(host)) {
                            sendBlock(block, nodeIp + DataChain.PORT);
                            // TODO move file, to avoid adding it twice to blockchain
                        }
                    }
                } catch (JSONException | URISyntaxException | IOException e) {
                    log.error("Error during new block propagation");
                }
            });
        }

    }

    private void sendBlock(Block block, String url) throws URISyntaxException {
        restTemplate.put(new URI(url), block);
    }
}
