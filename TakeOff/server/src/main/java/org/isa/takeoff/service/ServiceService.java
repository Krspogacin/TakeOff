package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.Service;
import org.isa.takeoff.model.ServiceHotel;
import org.isa.takeoff.repository.ServiceRepository;
import org.isa.takeoff.repository.ServHotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@Transactional(readOnly = true)
public class ServiceService {

	@Autowired
	private ServiceRepository serviceRepository;
	
	@Autowired
	private ServHotelRepository serviceHotelRepository;
	
	public List<Service> findAll(){
		return serviceRepository.findAll();
	}
	
	public List<ServiceHotel> findAllHotel(){
		return serviceHotelRepository.findAll();
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public ServiceHotel save(ServiceHotel hotelService){
		return serviceHotelRepository.save(hotelService);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Long id){
		serviceHotelRepository.deleteById(id);
	}
}
