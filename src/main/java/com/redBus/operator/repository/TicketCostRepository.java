package com.redBus.operator.repository;

import com.redBus.operator.entity.TicketCost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketCostRepository extends JpaRepository<TicketCost, String> {
    TicketCost findByTicketId(String ticketId);

    // Method to find TicketCost by ID (as String)
}
