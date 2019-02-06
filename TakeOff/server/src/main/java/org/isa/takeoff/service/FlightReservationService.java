package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.FlightReservation;
import org.isa.takeoff.model.Reservation;
import org.isa.takeoff.repository.FlightReservationRepository;
import org.isa.takeoff.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FlightReservationService {

	@Autowired
	private FlightReservationRepository flightReservationRepository;
	
	@Autowired
	private ReservationRepository reservationRepository;

	public FlightReservation findOne(Long id) {
		return flightReservationRepository.findById(id).get();
	}

	public List<FlightReservation> findAll() {
		return flightReservationRepository.findAll();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public FlightReservation save(FlightReservation reservation) {
		return flightReservationRepository.save(reservation);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Long id) {
		flightReservationRepository.deleteById(id);
	}
	
	public Reservation findOneReservation(Long id) {
		return reservationRepository.findById(id).get();
	}

	public List<Reservation> findAllReservations() {
		return reservationRepository.findAll();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Reservation saveReservation(Reservation reservation) {
		return reservationRepository.save(reservation);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteReservation(Long id) {
		reservationRepository.deleteById(id);
	}
}