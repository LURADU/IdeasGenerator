package com.example.ideas_generator.repository;

import com.example.ideas_generator.models.GeneratedIdea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneratedIdeaRepository extends JpaRepository<GeneratedIdea, Long> {
    boolean existsByIdea(String idea);
}