package com.platform.workflowservice.service;

import com.platform.workflowservice.dto.AiPredictRequest;
import com.platform.workflowservice.dto.AiPredictResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AiServiceIntegration {

    RestTemplate restTemplate = new RestTemplate();

    public AiPredictResponse callAiService(String title, String description){

        return restTemplate.postForObject(
                "http://ai-service:8000/predict",
                new AiPredictRequest(title,description),
                AiPredictResponse.class
        );
    }
}
