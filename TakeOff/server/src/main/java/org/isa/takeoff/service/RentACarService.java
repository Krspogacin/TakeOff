package org.isa.takeoff.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.isa.takeoff.model.RentACar;
import org.isa.takeoff.model.RentACarRating;
import org.isa.takeoff.model.RentACarRatingId;
import org.isa.takeoff.repository.RentACarRatingRepository;
import org.isa.takeoff.repository.RentACarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RentACarService 
{
	@Autowired
	private RentACarRepository rentACarRepository;
	
	@Autowired
	private RentACarRatingRepository rentACarRatingRepository;
	
	public RentACar findOne(Long id) {
		return rentACarRepository.findById(id).get();
	}

	public List<RentACar> findAll() {
		return rentACarRepository.findAll();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public RentACar save(RentACar rentACar) {
		return rentACarRepository.save(rentACar);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Long id) {
		rentACarRepository.deleteById(id);
	}
	
	public RentACar findByName(String name) {
		return rentACarRepository.findByName(name);
	}
	
	public RentACarRating findOneRating(RentACarRatingId id) {
		RentACarRating rentACarRating;
		try {
			rentACarRating = rentACarRatingRepository.findById(id).get();
			return rentACarRating;
		} catch(NoSuchElementException noSuchElementException) {
			return null;
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public RentACarRating saveRating(RentACarRating rentACarRating) {
		return rentACarRatingRepository.save(rentACarRating);
	}
}