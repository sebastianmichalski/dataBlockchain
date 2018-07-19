package server;

import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@RestController
@Log4j2
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
    public ResponseEntity addNode(@RequestBody String ip) {
        nodes.add(ip);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
