package org.isa.takeoff.service;

import java.time.LocalDate;
import java.util.List;

import org.isa.takeoff.model.VehicleReservation;
import org.isa.takeoff.repository.VehicleReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VehicleReservationService
{
	@Autowired
	private VehicleReservationRepository vehicleReservationRepository;
	
	public List<VehicleReservation> findAllByDate(Long id, LocalDate date) {
		return vehicleReservationRepository.findAllByVehicleIdAndReservationStartDateLessThanEqualAndReservationEndDateGreaterThanEqual(id, date, date);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Long id) {
		vehicleReservationRepository.deleteById(id);
	}
}
