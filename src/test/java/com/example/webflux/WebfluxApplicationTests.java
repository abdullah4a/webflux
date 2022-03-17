package com.example.webflux;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebHandler;
import org.springframework.web.servlet.mvc.Controller;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@SpringBootTest
class WebfluxApplicationTests {
    @Autowired
    private ApplicationContext context;
    @NonNull
    private Controller controller;

    @Test
    void contextLoads() {
        //		/////////////////
        //		5.1. Binding to a Server
        //		/////////////////
//        WebTestClient testClient = WebTestClient
//                .bindToServer()
//                .baseUrl("http://localhost:8080")
//                .build();

        //		//////////////////
        //      5.4. Binding to an Application Context
        //		//////////////////
        WebTestClient testClient = WebTestClient.bindToApplicationContext(context)
                .build();
        //		//////////////////
        //      5.3. Binding to a Web Handler
        //		//////////////////
        WebHandler handler = exchange -> Mono.empty();
        WebTestClient.bindToWebHandler(handler).build();
        //		//////////////////
        //      5.5. Binding to a Controller
        //		//////////////////
//        WebTestClient testClient = WebTestClient.bindToController(controller).build();
        // 		//////////////////
        //     5.6. Making a Request
        // 		//////////////////
        WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8080")
                .build()
                .post()
                .uri("/resource")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("field").isEqualTo("value");

    }

}
