package org.isa.takeoff.repository;

import org.isa.takeoff.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
	Administrator findByUsername( String username );
	Administrator findByEmail( String email );
}
