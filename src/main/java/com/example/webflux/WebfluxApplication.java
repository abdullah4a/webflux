package com.example.webflux;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class WebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxApplication.class, args);
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
        HttpClient httpClient1 = (HttpClient) HttpClient.create()
                .baseUrl("http://localhost:8081")
                .get();

        WebClient client = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
                .build();

//        WebClient client = WebClient.builder()
//                .clientConnector(new ReactorClientHttpConnector(httpClient))
//                .build();
        System.out.println("Hello From Spring Web Let's Say");
        System.out.println("Clients Data is\n" + client.get() + "\n" + httpClient);
        try {
            URL url = new URL("https://github.com/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Java 1.1");
            if (con.getResponseCode() == 200) {
                System.out.println(con.getInputStream());
            } else {
                System.out.println("Error in Connection");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
