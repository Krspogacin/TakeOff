package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.RentACarMainService;
import org.isa.takeoff.repository.RentACarMainServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RentACarMainServiceService 
{
	@Autowired
	private RentACarMainServiceRepository rentACarMainServiceRepository;
	
	public RentACarMainService findOne(Long id) {
		return rentACarMainServiceRepository.findById(id).get();
	}

	public List<RentACarMainService> findAll() {
		return rentACarMainServiceRepository.findAll();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public RentACarMainService save(RentACarMainService rentACarMainService) {
		return rentACarMainServiceRepository.save(rentACarMainService);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Long id) {
		rentACarMainServiceRepository.deleteById(id);
	}
	
	public RentACarMainService findByName(String name) {
		return rentACarMainServiceRepository.findByName(name);
	}
}