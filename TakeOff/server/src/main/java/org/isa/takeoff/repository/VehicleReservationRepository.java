package org.isa.takeoff.repository;

import java.time.LocalDate;
import java.util.List;

import org.isa.takeoff.model.VehicleReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleReservationRepository extends JpaRepository<VehicleReservation, Long> {
	List<VehicleReservation> findAllByVehicleIdAndReservationStartDateLessThanEqualAndReservationEndDateGreaterThanEqual(Long id, LocalDate date1, LocalDate date2);
}
