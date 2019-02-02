package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.Hotel;
import org.isa.takeoff.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class HotelService {

	@Autowired
	private HotelRepository hotelRepository;
	
	public Hotel findOne(Long id){
		return hotelRepository.getOne(id);
	}
	
	public List<Hotel> findAll(){
		return hotelRepository.findAll();
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Hotel save(Hotel hotel){
		return hotelRepository.save(hotel);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Long id){
		hotelRepository.deleteById(id);
	}
}
