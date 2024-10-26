package com.example.ideas_generator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    private final DataSource dataSource;

    @Autowired
    public DatabaseConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public boolean testDatabaseConnection() {
        try {
            jdbcTemplate().execute("SELECT 1");
            System.out.println("Conexiunea la baza de date a fost stabilită cu succes!");
            return true;
        } catch (Exception e) {
            System.err.println("Eroare la conectarea la baza de date: " + e.getMessage());
            return false;
        }
    }
}