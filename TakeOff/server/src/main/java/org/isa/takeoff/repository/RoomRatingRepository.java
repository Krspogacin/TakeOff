package org.isa.takeoff.repository;

import org.isa.takeoff.model.RoomRating;
import org.isa.takeoff.model.RoomRatingId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRatingRepository extends JpaRepository<RoomRating, RoomRatingId> {

}
