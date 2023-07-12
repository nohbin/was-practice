package org.example;


import org.example.calculator.domain.Calculator;
import org.example.calculator.domain.PositiveNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomWebApplicationServer {
    private final int port;
    //Thread Pool 을 이용해 안정적으로 이용하기
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final Logger logger = LoggerFactory.getLogger(CustomWebApplicationServer.class);

    public CustomWebApplicationServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("[CustomWebApplicationServer] started {} port", port);

            Socket clientSocket;
            logger.info("[CustomWebApplicationServer] waiting for clinet");

            while ((clientSocket = serverSocket.accept()) != null) {
                logger.info("[CustomWebApplicationServer] client connected");

//                /**
//                 * Step 2- 사용자의 요청을  Thread 를 새로 생성해서 사용자 요청을 처리하도록 한다.
//                 * -- 메모리를 할당 받기 떄문에 성능이 떨어짐
//                 * -- CPU 와 메모리 사용량이 증가. SERVER 의 resource 가 부담이 될 수 있음.
//                 */
//              new Thread(new ClientRequestHandler(clientSocket)).start();

                /**
                 *  Step3 - Thread Pool 을 이용하여 안정적 서비스가 가능하도록 한다.
                 */
                executorService.execute(new ClientRequestHandler(clientSocket));
            }
        }
    }
}
