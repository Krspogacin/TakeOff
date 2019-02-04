package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.FlightReservation;
import org.isa.takeoff.repository.FlightReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FlightReservationService {

	@Autowired
	private FlightReservationRepository reservationRepository;

	public FlightReservation findOne(Long id) {
		return reservationRepository.findById(id).get();
	}

	public List<FlightReservation> findAll() {
		return reservationRepository.findAll();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public FlightReservation save(FlightReservation reservation) {
		return reservationRepository.save(reservation);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Long id) {
		reservationRepository.deleteById(id);
	}

}
