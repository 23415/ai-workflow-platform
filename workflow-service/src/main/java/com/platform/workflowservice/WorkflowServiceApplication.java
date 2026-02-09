package com.platform.workflowservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableCaching
@EnableKafka
@SpringBootApplication
public class WorkflowServiceApplication {

    public static void main(String[] args) {
        System.out.println("🔥🔥 WORKFLOW SERVICE MAIN STARTED 🔥🔥");
        SpringApplication.run(WorkflowServiceApplication.class, args);
    }

}
