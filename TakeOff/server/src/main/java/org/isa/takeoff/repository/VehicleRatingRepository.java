package org.isa.takeoff.repository;

import org.isa.takeoff.model.VehicleRating;
import org.isa.takeoff.model.VehicleRatingId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRatingRepository extends JpaRepository<VehicleRating, VehicleRatingId> {
	
}
