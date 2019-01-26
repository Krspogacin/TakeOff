package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.Ticket;
import org.isa.takeoff.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TicketService {

	@Autowired
	private TicketRepository ticketRepository;
	
	public Ticket findOne(Long id) {
		return ticketRepository.findById(id).get();
	}

	public List<Ticket> findAll() {
		return ticketRepository.findAll();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Ticket save(Ticket ticket) {
		return ticketRepository.save(ticket);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Long id) {
		ticketRepository.deleteById(id);
	}

}
