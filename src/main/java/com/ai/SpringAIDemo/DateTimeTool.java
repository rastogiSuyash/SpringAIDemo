package com.ai.SpringAIDemo;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class DateTimeTool {

    RestTemplate restTemplate=new RestTemplate();

    @Tool
    public String getCurrentDateTime(){
//        return LocalDateTime.now().toString();
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }

    @Tool(description = "News details")
    public String getNews(String country, String category){
        System.out.println("calling new api");
//        WebClient webClient = WebClient.builder().baseUrl("https://newsdata.io/api/1/latest").build();
//
//         return webClient.get()
//                 .uri(uriBuilder -> uriBuilder
//                         .queryParam("apikey", "pub_91c5228f02a54e2ca37852d08b20dca9")
//                         .queryParam("country", country)
//                         .queryParam("category", category)
//                         .build())
//                 .retrieve()
//                 .bodyToMono(String.class)
//                 .block();

        String uri= "https://newsdata.io/api/1/latest?   apikey=pub_91c5228f02a54e2ca37852d08b20dca9  &country="+country+"  &category="+category;

        return restTemplate.getForObject(uri, String.class);


    }
}
