package org.isa.takeoff.repository;

import org.isa.takeoff.model.RentACarMainService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentACarMainServiceRepository extends JpaRepository<RentACarMainService, Long> 
{
	RentACarMainService findByName(String name);
}
