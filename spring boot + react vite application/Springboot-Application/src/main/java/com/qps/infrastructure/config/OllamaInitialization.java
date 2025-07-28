package com.qps.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.net.HttpURLConnection;
import java.net.URI;

@Slf4j
@Configuration
@PropertySource(
        value = "classpath:prompts.yaml",
        factory = YamlPropertySourceFactory.class
)
public class OllamaInitialization {
    @Value("${gemma.init}")
    private String init;

    @PostConstruct
    public void contextInitialized() {
        try {
            log.info("🔥 Sending initial prompt to Ollama...");

            var url = new URI("http://localhost:11434/api/generate");
            var conn = (HttpURLConnection) url.toURL().openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            var initialPrompt = String.format("""
                    {
                        "model": "gemma3:4b",
                        "prompt": %s,
                        "stream": false
                    }
                    """, new ObjectMapper().writeValueAsString(init));

            try (var os = conn.getOutputStream()) {
                os.write(initialPrompt.getBytes());
                os.flush();
            }

            var responseCode = conn.getResponseCode();
            log.info("Initial prompt sent. Ollama responded with code: {}", responseCode);
        } catch (Exception e) {
            log.error("Failed to send initial prompt to Ollama: {}", e.getMessage());
        }
    }
}
