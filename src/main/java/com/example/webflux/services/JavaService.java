package com.example.webflux.services;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
public class JavaService {
    public void httpClientWorking() {
        //        Bleading Practice https://www.baeldung.com/spring-5-webclient
//         Creating a WebClient Instance with Timeouts
        //        Docs Example HttpClient

//        WebClient client = WebClient.builder()
//                .clientConnector(new ReactorClientHttpConnector(httpClient))
//                .build();
//        ////////////////////
//        4.1. Creating a WebClient Instance
//        /////////////////////
        HttpClient httpClient1 = (HttpClient) HttpClient.create()
                .baseUrl("http://localhost:8081")
                .get();
        //
        WebClient client = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
                .build();
//        ///////////////////////////
//        4.2. Creating a WebClient Instance with Timeouts
//       ///////////////////////////
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
//        /////////////////////////////
//        4.3. Preparing a Request – Define the Method
//        /////////////////////////////
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.method(HttpMethod.POST);
//        or following is Shortcut method
//        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.post();
        WebClient.RequestBodySpec bodySpec = uriSpec.uri("/resource");
//        ////////////////////////////
//        4.5. Preparing a Request – Define the Body
//        ///////////////////////////
        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue("data");
//        or  publishers method
//        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.body(
//                BodyInserters.fromPublisher(Mono.just("data"), String.class)
//        );
//        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
//        map.add("key1", "value1");
//        map.add("key2", "value2");
//        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.body(
//                BodyInserters.fromMultipartData(map));
//        ////////////////////////////
//        4.6. Preparing a Request – Define the Headers
//        ////////////////////////////
        WebClient.ResponseSpec responseSpec = headersSpec.header(
                        HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .ifNoneMatch("*")
                .ifModifiedSince(ZonedDateTime.now())
                .retrieve();
//        ////////////////////////////
//        4.7. Getting a Response
//        ////////////////////////////
        Mono<String> response = headersSpec.exchangeToMono(response1 -> {
            if (response1.statusCode()
                    .equals(HttpStatus.OK)) {
                return response1.bodyToMono(String.class);
            } else if (response1.statusCode()
                    .is4xxClientError()) {
                return Mono.just("Error response");
            } else {
                return response1.createException()
                        .flatMap(Mono::error);
            }
        });
        System.out.println("Logging Response\n" + response + "\n" + responseSpec);
//        ////////////////////////////
//        5. Working with the WebTestClient
//        ////////////////////////////
    }

}
