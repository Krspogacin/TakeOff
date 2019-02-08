package org.isa.takeoff.service;

import java.time.LocalDate;
import java.util.List;

import org.isa.takeoff.model.RoomReservation;
import org.isa.takeoff.repository.RoomReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RoomReservationService
{
	@Autowired
	private RoomReservationRepository roomReservationRepository;
	
	public List<RoomReservation> findAllByDate(LocalDate date) {
		return roomReservationRepository.findAllByReservationStartDateLessThanEqualAndReservationEndDateGreaterThanEqual(date, date);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Long id) {
		roomReservationRepository.deleteById(id);
	}
}
