# dataBlockchain

Project made for self-learning week purpose in current job. It was made as proof of concept for making decentralized blockchain,
that stores files. The only centralized point here is `sever`, which keeps set with ip addresses of nodes, to let each node know
where to get blockchain data and where to propagate freshly mined blocks. To get rid of centralization, P2P should be introduced
instead of `server` container.

Technologies used during development:
* Docker along with docker-compose
* Gson
* Log4j
* Spring Boot
* Lombok
* Google Guava
* Java 8

Prerequisites:
* Java 8 JDK installed, environment variables are set up: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
* Docker CE installed: https://docs.docker.com/install/
* Docker-compose installed: https://docs.docker.com/compose/install/

## Usage

To start `Server` container call command as presented below:

```
docker-compose up --build -d server
```

Application should start on host 10.5.0.5 and port 8080:

```
2018-07-20 10:10:25.598  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2018-07-20 10:10:25.607  INFO 1 --- [           main] server.Server                            : Started Server in 3.311 seconds (JVM running for 3.767)
2018-07-20 10:10:32.394  INFO 1 --- [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring FrameworkServlet 'dispatcherServlet'
2018-07-20 10:10:32.395  INFO 1 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : FrameworkServlet 'dispatcherServlet': initialization started
2018-07-20 10:10:32.407  INFO 1 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : FrameworkServlet 'dispatcherServlet': initialization completed in 12 ms
```

To start `Node` container call command as presented below:

```
docker-compose up --build -d node
```

Application should start on host 10.5.0.2 and port 8080:

```
2018-07-20 10:12:45.094  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2018-07-20 10:12:45.098  INFO 1 --- [           main] com.findwise.spring.Application          : Started Application in 3.901 seconds (JVM running for 4.461)
2018-07-20 10:12:46.013  INFO 1 --- [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring FrameworkServlet 'dispatcherServlet'
2018-07-20 10:12:46.013  INFO 1 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : FrameworkServlet 'dispatcherServlet': initialization started
2018-07-20 10:12:46.028  INFO 1 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : FrameworkServlet 'dispatcherServlet': initialization completed in 15 ms
```

Now you can call `10.5.0.2:8080` from your browser and you should get json like presented below:

```
{
  list: [
    {
      hash: "00000fb22535c23a449fb9d14b5db2c2623fbc20096a80d1d8cc04e48b1b619f",
      previousHash: "0",
      file: "/",
      timeStamp: 1532090506504,
      nonce: 126842,
      fileContent: "",
      blockIndex: 1
    }
  ]
}
```

To mine more blocks add some file to `files` directory in project location. Scheduled task `Miner` should add 
this file to blockchain and propagate it to all other nodes (if there are any). Now output from `10.5.0.2:8080` should look like:

```
{
  list: [
    {
      hash: "00000fb22535c23a449fb9d14b5db2c2623fbc20096a80d1d8cc04e48b1b619f",
      previousHash: "0",
      file: "/",
      timeStamp: 1532090506504,
      nonce: 126842,
      fileContent: "",
      blockIndex: 1
    },
    {
      hash: "00000b58d0500a026a59b64e458d2e16cdcdcb4bddace8f63e582995a9908abb",
      previousHash: "00000fb22535c23a449fb9d14b5db2c2623fbc20096a80d1d8cc04e48b1b619f",
      file: "/files/dataChain.iml",
      timeStamp: 1532090760002,
      nonce: 94660,
      fileContent: "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPG1vZHVsZSBvcmcuamV0YnJhaW5zLmlkZWEubWF2ZW4ucHJvamVjdC5NYXZlblByb2plY3RzTWFuYWdlci5pc01hdmVuTW9kdWxlPSJ0cnVlIiB2ZXJzaW9uPSI0Ij4KICA8Y29tcG9uZW50IG5hbWU9Ik5ld01vZHVsZVJvb3RNYW5hZ2VyIiBMQU5HVUFHRV9MRVZFTD0iSkRLXzFfNSI+CiAgICA8b3V0cHV0IHVybD0iZmlsZTovLyRNT0RVTEVfRElSJC90YXJnZXQvY2xhc3NlcyIgLz4KICAgIDxvdXRwdXQtdGVzdCB1cmw9ImZpbGU6Ly8kTU9EVUxFX0RJUiQvdGFyZ2V0L3Rlc3QtY2xhc3NlcyIgLz4KICAgIDxjb250ZW50IHVybD0iZmlsZTovLyRNT0RVTEVfRElSJCI+CiAgICAgIDxleGNsdWRlRm9sZGVyIHVybD0iZmlsZTovLyRNT0RVTEVfRElSJC90YXJnZXQiIC8+CiAgICA8L2NvbnRlbnQ+CiAgICA8b3JkZXJFbnRyeSB0eXBlPSJpbmhlcml0ZWRKZGsiIC8+CiAgICA8b3JkZXJFbnRyeSB0eXBlPSJzb3VyY2VGb2xkZXIiIGZvclRlc3RzPSJmYWxzZSIgLz4KICA8L2NvbXBvbmVudD4KPC9tb2R1bGU+",
      blockIndex: 2
    }
  ]
}

```

Now you can finally add another node to blockchain network by calling command:

```
docker-compose up --build -d node2
```

If you call `10.5.0.3:8080` you should get exactly the same blockchain presented in json, because new node synchronized blockchain
from yet existing node with the longes blockchain. Again, if you add some file to `files` or `files2` directory one of nodes will mine 
new block with this file and propagate new block to another node.
