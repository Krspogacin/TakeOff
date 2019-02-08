package org.isa.takeoff.repository;

import java.time.LocalDate;
import java.util.List;

import org.isa.takeoff.model.RoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomReservationRepository extends JpaRepository<RoomReservation, Long> {
	List<RoomReservation> findAllByReservationStartDateLessThanEqualAndReservationEndDateGreaterThanEqual(LocalDate date1, LocalDate date2);
}
