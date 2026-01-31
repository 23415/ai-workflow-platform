package com.platform.workflowservice.utils;

import com.platform.workflowservice.entities.SLA;
import com.platform.workflowservice.enums.SlaStatus;
import com.platform.workflowservice.repository.SlaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class SlaBreachScheduler {

    private final SlaRepository slaRepository;

    public SlaBreachScheduler(SlaRepository slaRepository) {
        this.slaRepository = slaRepository;
    }


    @Scheduled(fixedRate = 60000)
    public void checkBreachedStatus(){
        List<SLA> breachedSlaList = slaRepository.findBreachedSlas(Instant.now());

        for(SLA breachedSLA : breachedSlaList){
            breachedSLA.setStatus(SlaStatus.BREACHED);
        }

        slaRepository.saveAll(breachedSlaList);
    }
}
