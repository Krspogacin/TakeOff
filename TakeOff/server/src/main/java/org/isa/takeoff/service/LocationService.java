package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.Location;
import org.isa.takeoff.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LocationService {
	
	@Autowired
	private LocationRepository locationRepository;
	
	public Location findOne(Long id) {
		return locationRepository.findById(id).get();
	}

	public List<Location> findAll() {
		return locationRepository.findAll();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Location save(Location location) {
		return locationRepository.save(location);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Long id) {
		locationRepository.deleteById(id);
	}
	
	public Location findOneByLatitudeAndLongitude(Double latitude, Double longitude) {
		return locationRepository.findOneByLatitudeAndLongitude(latitude, longitude);
	}
}