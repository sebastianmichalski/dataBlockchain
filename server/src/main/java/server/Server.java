package server;

import com.google.gson.GsonBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@RestController
public class Server {

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }

    private Set<String> nodes = new HashSet<>();

    @RequestMapping(method = RequestMethod.GET)
    public String getNodes() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(nodes);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addNode(String ip) {
        nodes.add(ip);
    }
}
