package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.Office;
import org.isa.takeoff.repository.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OfficeService 
{
	@Autowired
	private OfficeRepository officeRepository;
	
	public Office findOne(Long id) {
		return officeRepository.findById(id).get();
	}

	public List<Office> findAll() {
		return officeRepository.findAll();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Office save(Office office) {
		return officeRepository.save(office);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Long id) {
		officeRepository.deleteById(id);
	}
}