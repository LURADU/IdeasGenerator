package com.example.ideas_generator;

import com.example.ideas_generator.models.ApiData;
import com.example.ideas_generator.service.ApiManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class IdeasGeneratorApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(IdeasGeneratorApplication.class, args);

        boolean isDatabaseConnected = context.getBean("testDatabaseConnection", Boolean.class);




        ApiManager apiManager = context.getBean(ApiManager.class);

        System.out.println("Total number of APIs: " + apiManager.getTotalApiCount());

        List<ApiData> pickedApis = apiManager.pickRandomApis();
        System.out.println("Randomly picked APIs:");
        pickedApis.forEach(System.out::println);


        if (isDatabaseConnected) {
            System.out.println("Aplicația a pornit cu succes și este conectată la baza de date.");
        } else {
            System.err.println("Aplicația a pornit, dar nu s-a putut conecta la baza de date.");
        }
    }
}