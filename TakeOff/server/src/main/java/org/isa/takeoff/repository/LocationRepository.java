package org.isa.takeoff.repository;

import org.isa.takeoff.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long>{
	Location findOneByLatitudeAndLongitude(Double latitude, Double longitude);
}
