package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.AirCompany;
import org.isa.takeoff.repository.AirCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirCompanyService {

	@Autowired
	private AirCompanyRepository airCompanyRepository;

	public AirCompany findOne(Long id) {
		return airCompanyRepository.findById(id).get();
	}

	public List<AirCompany> findAll() {
		return airCompanyRepository.findAll();
	}

	public AirCompany save(AirCompany airCompany) {
		return airCompanyRepository.save(airCompany);
	}

	public void delete(Long id) {
		airCompanyRepository.deleteById(id);
	}

}
