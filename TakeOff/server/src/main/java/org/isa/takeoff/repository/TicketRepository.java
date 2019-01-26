package org.isa.takeoff.repository;

import org.isa.takeoff.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long>{

}
