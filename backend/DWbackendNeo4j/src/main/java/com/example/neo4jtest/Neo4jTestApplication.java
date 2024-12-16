package com.example.neo4jtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.example.neo4jtest")
public class Neo4jTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(Neo4jTestApplication.class, args);
    }
}



