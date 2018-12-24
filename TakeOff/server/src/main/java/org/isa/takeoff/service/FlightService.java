package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.Flight;
import org.isa.takeoff.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FlightService {

	@Autowired
	private FlightRepository flightRepository;

	public Flight findOne(Long id) {
		return flightRepository.findById(id).get();
	}

	public List<Flight> findAll() {
		return flightRepository.findAll();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Flight save(Flight flight) {
		return flightRepository.save(flight);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Long id) {
		flightRepository.deleteById(id);
	}

}
