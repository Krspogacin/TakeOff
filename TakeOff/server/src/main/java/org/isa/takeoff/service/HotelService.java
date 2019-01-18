package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.Hotel;
import org.isa.takeoff.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelService {

	@Autowired
	private HotelRepository hotelRepository;
	
	public Hotel findOne(Long id){
		return hotelRepository.getOne(id);
	}
	
	public List<Hotel> findAll(){
		return hotelRepository.findAll();
	}
	
	public Hotel save(Hotel hotel){
		return hotelRepository.save(hotel);
	}
	
	public void delete(Long id){
		hotelRepository.deleteById(id);
	}
}
