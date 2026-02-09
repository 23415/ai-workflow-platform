package com.platform.workflowservice.controller;

import com.platform.workflowservice.dto.*;
import com.platform.workflowservice.entities.Ticket;
import com.platform.workflowservice.enums.SlaStatus;
import com.platform.workflowservice.repository.SlaRepository;
import com.platform.workflowservice.security.CustomUserPrincipal;
import com.platform.workflowservice.service.AiServiceIntegration;
import com.platform.workflowservice.service.TicketService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final SlaRepository slaRepository;
    private final AiServiceIntegration aiServiceIntegration;

    public TicketController(TicketService ticketService, SlaRepository slaRepository, AiServiceIntegration aiServiceIntegration) {
        this.ticketService = ticketService;
        this.slaRepository = slaRepository;
        this.aiServiceIntegration = aiServiceIntegration;
    }
    @PostMapping("/create")
    public TicketResponseDto createTicket(@RequestBody TicketRequestDto req){
        Long userId = 1L;
        Ticket ticket = ticketService.createTicket(req,userId);

        TicketResponseDto res = new TicketResponseDto();

        res.setId(ticket.getId());
        res.setTitle(ticket.getTitle());
        res.setStatus(ticket.getStatus());
        slaRepository.findByTicketId(ticket.getId())
                .ifPresent(sla->{

                    res.setSla(
                            new SlaResponseDto(
                                    sla.getStatus(),
                                    sla.getDeadline(),
                                    sla.getStatus() == SlaStatus.BREACHED
                            )
                    );
                });
        AiPredictResponse aiRes = aiServiceIntegration.callAiService(ticket.getTitle(), ticket.getDescription());
        res.setCategory(aiRes.category());
        return res;
    }

    @PutMapping("/{id}/assign")
    public TicketResponseDto assignTicket(@PathVariable Long id, @RequestParam Long userId){
        Ticket ticket = ticketService.assignTicket(id, userId);
        return new TicketResponseDto(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getStatus()
        );
    }

    @PutMapping("/{id}/status")
    public TicketResponseDto changeStatus(@PathVariable Long id, @RequestParam String status){
        Ticket ticket = ticketService.ticketStatusChange(id,status);
        return new TicketResponseDto(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getStatus()
        );
    }

    @GetMapping
    public PageResponse<TicketResponseDto> getTicketsByRole(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            Authentication authentication
    ){
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

        return ticketService.getTicketByRole(principal.getEmail(), principal.getRole(),page,pageSize);
    }

    @GetMapping("/{ticketId}")
    public TicketResponseDto getTicketById(@PathVariable Long ticketId){
        TicketResponseDto res = ticketService.getTicketById(ticketId);
        slaRepository.findByTicketId(res.getId())
                .ifPresent(sla->{
                    res.setSla(
                            new SlaResponseDto(
                                    sla.getStatus(),
                                    sla.getDeadline(),
                                    sla.getStatus() == SlaStatus.BREACHED
                            )
                    );
                });
        return res;
    }
}
