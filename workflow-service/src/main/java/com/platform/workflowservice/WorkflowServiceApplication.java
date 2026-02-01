package com.platform.workflowservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableCaching
@SpringBootApplication
public class WorkflowServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowServiceApplication.class, args);
    }

}
