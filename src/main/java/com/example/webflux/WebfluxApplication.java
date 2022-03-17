package com.example.webflux;

import com.example.webflux.services.JavaService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootApplication
public class WebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxApplication.class, args);
//        Object of Service
        JavaService javaService=new JavaService();
        javaService.httpClientWorking();
//        Videos Practice
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
