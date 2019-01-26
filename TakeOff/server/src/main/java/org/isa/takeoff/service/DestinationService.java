package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.Destination;
import org.isa.takeoff.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DestinationService {
	
	@Autowired
	private DestinationRepository destRepository;
	
	public Destination findOne(Long id) {
		return destRepository.findById(id).get();
	}

	public List<Destination> findAll() {
		return destRepository.findAll();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Destination save(Destination destination) {
		return destRepository.save(destination);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Long id) {
		destRepository.deleteById(id);
	}

}
