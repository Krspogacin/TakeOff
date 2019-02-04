package org.isa.takeoff.repository;

import org.isa.takeoff.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
