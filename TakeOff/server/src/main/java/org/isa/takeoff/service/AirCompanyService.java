package org.isa.takeoff.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.isa.takeoff.model.AirCompany;
import org.isa.takeoff.model.AirCompanyRating;
import org.isa.takeoff.model.AirCompanyRatingId;
import org.isa.takeoff.repository.AirCompanyRatingRepository;
import org.isa.takeoff.repository.AirCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AirCompanyService {

	@Autowired
	private AirCompanyRepository airCompanyRepository;
	
	@Autowired
	private AirCompanyRatingRepository airCompanyRatingRepository;

	public AirCompany findOne(Long id) {
		return airCompanyRepository.findById(id).get();
	}

	public List<AirCompany> findAll() {
		return airCompanyRepository.findAll();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public AirCompany save(AirCompany airCompany) {
		return airCompanyRepository.save(airCompany);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Long id) {
		airCompanyRepository.deleteById(id);
	}
	
	public AirCompanyRating findOneRating(AirCompanyRatingId id) {
		AirCompanyRating airCompanyRating;
		try {
			airCompanyRating = airCompanyRatingRepository.findById(id).get();
			return airCompanyRating;
		} catch(NoSuchElementException noSuchElementException) {
			return null;
		}
	}
}