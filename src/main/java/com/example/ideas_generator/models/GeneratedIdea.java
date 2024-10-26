package com.example.ideas_generator.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "generated_ideas")
public class GeneratedIdea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String idea;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    // Constructori, getteri și setteri

    public GeneratedIdea() {
    }

    public GeneratedIdea(String idea) {
        this.idea = idea;
        this.generatedAt = LocalDateTime.now();
    }

    // Getteri și setteri
}