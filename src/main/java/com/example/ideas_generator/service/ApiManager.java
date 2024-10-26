package com.example.ideas_generator.service;

import com.example.ideas_generator.models.ApiData;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ApiManager {

    private static final String API_FILE_PATH = "D:\\radu\\Curs Nexus\\3th\\Ideas_Generator\\apidata\\apis_list.txt";
    private static final int NO_OF_PICKS = 5;

    private List<ApiData> apiList;
    private Random random;

    public ApiManager() {
    this.random = new Random();
}

@PostConstruct
public void init() {
    loadApiList();
}

private void loadApiList() {
    try {
        apiList = Files.lines(Paths.get(API_FILE_PATH))
                .map(this::parseApiData)
                .collect(Collectors.toList());
    } catch (IOException e) {
        throw new RuntimeException("Eroare la citirea fișierului cu API-uri", e);
    }
}

private ApiData parseApiData(String line) {
    String[] parts = line.split("___");
    if (parts.length != 3) {
        throw new IllegalArgumentException("Format invalid pentru linia API: " + line);
    }
    return new ApiData(parts[0], parts[1], parts[2]);
}

public List<ApiData> pickRandomApis() {
    List<ApiData> pickedApis = new ArrayList<>();
    List<Integer> pickedIndices = new ArrayList<>();

    while (pickedApis.size() < NO_OF_PICKS && pickedIndices.size() < apiList.size()) {
        int index = random.nextInt(apiList.size());
        if (!pickedIndices.contains(index)) {
            pickedIndices.add(index);
            pickedApis.add(apiList.get(index));
        }
    }

    return pickedApis;
}

public int getTotalApiCount() {
    return apiList.size();
}
}