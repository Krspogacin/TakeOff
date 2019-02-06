package org.isa.takeoff.repository;

import org.isa.takeoff.model.HotelRating;
import org.isa.takeoff.model.HotelRatingId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRatingRepository extends JpaRepository<HotelRating, HotelRatingId> {

}
