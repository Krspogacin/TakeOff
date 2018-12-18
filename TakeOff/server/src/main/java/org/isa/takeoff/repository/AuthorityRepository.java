package org.isa.takeoff.repository;

import org.isa.takeoff.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> 
{
	Authority findByName(String name);
}