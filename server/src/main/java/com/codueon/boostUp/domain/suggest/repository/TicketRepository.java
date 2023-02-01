package com.codueon.boostUp.domain.suggest.repository;

import com.codueon.boostUp.domain.suggest.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
