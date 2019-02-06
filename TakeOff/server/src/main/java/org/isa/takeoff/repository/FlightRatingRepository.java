package org.isa.takeoff.repository;

import org.isa.takeoff.model.FlightRating;
import org.isa.takeoff.model.FlightRatingId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRatingRepository extends JpaRepository<FlightRating, FlightRatingId> {

}
