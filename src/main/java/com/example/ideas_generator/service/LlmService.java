package com.example.ideas_generator.service;

import com.example.ideas_generator.models.ApiData;
import com.example.ideas_generator.models.GeneratedIdea;
import com.example.ideas_generator.repository.GeneratedIdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LlmService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final GeneratedIdeaRepository generatedIdeaRepository;
    private final ApiManager apiManager;

    @Value("${ollama.model}")
    private String ollamaModel;

    @Autowired
    public LlmService(@Value("${ollama.api.url}") String ollamaApiUrl,
                      ObjectMapper objectMapper,
                      GeneratedIdeaRepository generatedIdeaRepository,
                      ApiManager apiManager) {
        this.webClient = WebClient.builder().baseUrl(ollamaApiUrl).build();
        this.objectMapper = objectMapper;
        this.generatedIdeaRepository = generatedIdeaRepository;
        this.apiManager = apiManager;
    }

    public Mono<List<String>> generateProjectIdeas() {
        List<ApiData> selectedApis = apiManager.pickRandomApis();
        String apiListString = formatApiList(selectedApis);

        String prompt = """
            Genereaza 5 ideei folosind lista de api de mai jos pentru proiecte java pentru juniori in maxim 10 cuvinte
            Generarea sa nu contine decat lista de ideii numerotata fara "Aici sunt 5 idei pentru proiecte Java pentru juniori:"
            Here are the APIs to use:
            """ + apiListString;

        return webClient.post()
                .uri("/api/generate")
                .bodyValue(objectMapper.createObjectNode()
                        .put("model", ollamaModel)
                        .put("prompt", prompt)
                        .put("stream", false))
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractIdeasFromResponse)
                .map(this::filterAndSaveUniqueIdeas);
    }

    public Mono<List<String>> developProjectIdea(String idea, int level) {
        String prompt = String.format("""
                Dezvoltă următoarea idee de proiect Java pentru juniori: '%s'.
                Oferă mai multe detalii și sugestii pentru implementare,
                considerând un nivel de complexitate de %d (pe o scară de la 1 la 10).
                "Include sugestii pentru funcționalități, structura proiectului și potențiale provocări.
                """, idea, level);

        return webClient.post()
                .uri("/api/generate")
                .bodyValue(objectMapper.createObjectNode()
                        .put("model", ollamaModel)
                        .put("prompt", prompt)
                        .put("stream", false))
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractIdeasFromResponse);
    }

    private String formatApiList(List<ApiData> apis) {
        return apis.stream()
                .map(api -> String.format("- %s: %s (%s)", api.getApiName(), api.getDescription(), api.getUrl()))
                .collect(Collectors.joining("\n"));
    }

    private List<String> extractIdeasFromResponse(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            String ideas = jsonNode.get("response").asText().replace("*","") + "\n";

            return
                    Arrays.stream(ideas.split("\n"))
                            .filter(idea -> !idea.isEmpty())
                            .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Ollama response", e);
        }
    }

    private List<String> filterAndSaveUniqueIdeas(List<String> ideas) {
        List<String> uniqueIdeas = new ArrayList<>();
        for (String idea : ideas) {
            if (!generatedIdeaRepository.existsByIdea(idea)) {
                generatedIdeaRepository.save(new GeneratedIdea(idea));
                uniqueIdeas.add(idea);
            }
        }
        return uniqueIdeas;
    }


    public Mono<String> continueConversation(String conversation, String userInput) {
        String prompt = String.format("""
                Conversație anterioară:
                %s
                
                Întrebare utilizator: %s
                
                Continuă conversația în limba română, oferind un răspuns detaliat și util la întrebarea utilizatorului.
                """, conversation, userInput);

        return webClient.post()
                .uri("/api/generate")
                .bodyValue(objectMapper.createObjectNode()
                        .put("model", ollamaModel)
                        .put("prompt", prompt)
                        .put("stream", false))
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(response);
                        return jsonNode.get("response").asText();
                    } catch (Exception e) {
                        throw new RuntimeException("Error parsing Ollama response", e);
                    }
                });
    }
}