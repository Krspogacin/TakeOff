package org.isa.takeoff.repository;

import org.isa.takeoff.model.AirCompanyRating;
import org.isa.takeoff.model.AirCompanyRatingId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirCompanyRatingRepository extends JpaRepository<AirCompanyRating, AirCompanyRatingId> {

}
