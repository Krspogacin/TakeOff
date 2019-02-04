package org.isa.takeoff.repository;

import org.isa.takeoff.model.FlightReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightReservationRepository extends JpaRepository<FlightReservation, Long> {

}
