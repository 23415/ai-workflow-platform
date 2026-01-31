package com.platform.workflowservice.controller;

import com.platform.workflowservice.dto.SlaResponseDto;
import com.platform.workflowservice.dto.TicketRequestDto;
import com.platform.workflowservice.dto.TicketResponseDto;
import com.platform.workflowservice.entities.Ticket;
import com.platform.workflowservice.enums.SlaStatus;
import com.platform.workflowservice.repository.SlaRepository;
import com.platform.workflowservice.security.CustomUserPrincipal;
import com.platform.workflowservice.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final SlaRepository slaRepository;

    public TicketController(TicketService ticketService, SlaRepository slaRepository) {
        this.ticketService = ticketService;
        this.slaRepository = slaRepository;
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

        return res;
    }

    @PutMapping("/{id}/assign")
    public TicketResponseDto assignTicket(@PathVariable Long id, @RequestParam Long userId){
//        throw new ConfigDataResourceNotFoundException("ticket Test");
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
    public Page<Ticket> getTicketsByRole(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            Authentication authentication
    ){
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(
                page,pageSize, Sort.by("createdAt").descending()
        );

        Page<Ticket> ticketPage = ticketService.getTicketByRole(principal.getEmail(), principal.getRole(),pageable);
        return ticketPage;
    }

    @GetMapping("/{ticketId}")
    public TicketResponseDto getTicketById(@PathVariable Long ticketId){
        Ticket ticket = ticketService.getTicketById(ticketId);
        TicketResponseDto res = toDto(ticket);
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
        return res;
    }
    private TicketResponseDto toDto(Ticket ticket) {
        return new TicketResponseDto(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getStatus()
        );
    }
}
