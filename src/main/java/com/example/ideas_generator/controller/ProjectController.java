package com.example.ideas_generator.controller;

import com.example.ideas_generator.service.LlmService;
import com.example.ideas_generator.service.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@RestController
public class ProjectController {
    private final LlmService llmService;
    private final PdfService pdfService;

    public ProjectController(LlmService llmService, PdfService pdfService) {
        this.llmService = llmService;
        this.pdfService = pdfService;
    }

    @GetMapping("/generate-project")
    public Mono<List<String>> generateProject() {
        return llmService.generateProjectIdeas();
    }

    @GetMapping("/develop-project")
    public Mono<List<String>> developProject(@RequestParam String idea, @RequestParam int level) {
        return llmService.developProjectIdea(idea, level);
    }


    @PostMapping("/continue-conversation")
    public Mono<String> continueConversation(@RequestBody Map<String, String> payload) {
        String conversation = payload.get("conversation");
        String userInput = payload.get("userInput");
        return llmService.continueConversation(conversation, userInput);
    }

    @PostMapping("/download-pdf")
    public ResponseEntity<byte[]> downloadPdf(@RequestBody Map<String, String> payload) {
        String idea = payload.get("idea");
        String development = payload.get("development");
        String level = payload.get("level");
        String conversation = payload.get("conversation");

        byte[] pdfBytes = pdfService.generatePdf(idea, development, level, conversation);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "project_idea.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}