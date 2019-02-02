package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.Vehicle;
import org.isa.takeoff.model.VehiclePrice;
import org.isa.takeoff.repository.VehiclePriceRepository;
import org.isa.takeoff.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VehicleService 
{
	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private VehiclePriceRepository vehiclePriceRepository;

	public Vehicle findOne(Long id) {
		return vehicleRepository.findById(id).get();
	}

	public List<Vehicle> findAll() {
		return vehicleRepository.findAll();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Vehicle save(Vehicle vehicle) {
		return vehicleRepository.save(vehicle);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Long id) {
		vehicleRepository.deleteById(id);
	}
	
	public VehiclePrice findOneVehiclePrice(Long id) {
		return vehiclePriceRepository.findById(id).get();
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public VehiclePrice saveVehiclePrice(VehiclePrice vehiclePrice) {
		return vehiclePriceRepository.save(vehiclePrice);
	}
}