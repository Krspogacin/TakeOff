package org.isa.takeoff.repository;

import org.isa.takeoff.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long>{

}
