package com.kedar.taskmanager.service;

import com.kedar.taskmanager.dto.GeminiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeminiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeminiService geminiService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(geminiService, "apiKey", "test-key");
        ReflectionTestUtils.setField(geminiService, "apiUrl", "http://test-url");
        ReflectionTestUtils.setField(geminiService, "restTemplate", restTemplate);
    }

    @Test
    void generateContent_ShouldReturnContentFromGemini_WhenApiSucceeds() {
        GeminiResponse.Part part = new GeminiResponse.Part();
        part.setText("Extracted Task details");

        GeminiResponse.Content content = new GeminiResponse.Content();
        content.setParts(List.of(part));

        GeminiResponse.Candidate candidate = new GeminiResponse.Candidate();
        candidate.setContent(content);

        GeminiResponse response = new GeminiResponse();
        response.setCandidates(List.of(candidate));

        String expectedUrl = "http://test-url?key=test-key";
        when(restTemplate.postForObject(eq(expectedUrl), any(HttpEntity.class), eq(GeminiResponse.class)))
                .thenReturn(response);

        String result = geminiService.generateContent("Create a high priority task for lunch");

        assertEquals("Extracted Task details", result);
    }
}
