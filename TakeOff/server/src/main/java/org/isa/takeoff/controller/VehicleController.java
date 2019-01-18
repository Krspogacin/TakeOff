package org.isa.takeoff.controller;

import org.isa.takeoff.dto.VehicleDTO;
import org.isa.takeoff.model.RentACar;
import org.isa.takeoff.model.Vehicle;
import org.isa.takeoff.service.RentACarService;
import org.isa.takeoff.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/vehicles")
public class VehicleController 
{
	@Autowired
	private RentACarService rentACarService;
	
	@Autowired
	private VehicleService vehicleService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<VehicleDTO> getVehicle(@PathVariable Long id) 
	{
		try 
		{
			Vehicle vehicle = this.vehicleService.findOne(id);
			VehicleDTO vehicleDTO = new VehicleDTO(vehicle);
			return new ResponseEntity<>(vehicleDTO, HttpStatus.OK);
		}
		catch (Exception e) 
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<VehicleDTO> addVehicle(@RequestBody VehicleDTO vehicleDTO) 
	{
		if (vehicleDTO.getRentACar() == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try 
		{
			RentACar rentACar = this.rentACarService.findOne(vehicleDTO.getRentACar().getId());
			Vehicle vehicle = new Vehicle(vehicleDTO);
			vehicle.setRentACar(rentACar);
			vehicle = vehicleService.save(vehicle);
			return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.OK);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<VehicleDTO> updateVehicle(@RequestBody VehicleDTO vehicleDTO) 
	{
		try 
		{
			Vehicle vehicle = this.vehicleService.findOne(vehicleDTO.getId());
			vehicle.setBrand(vehicleDTO.getBrand());
			vehicle.setModel(vehicleDTO.getModel());
			vehicle.setYear(vehicleDTO.getYear());
			vehicle.setImage(vehicleDTO.getImage().getBytes());
			vehicle.setReserved(vehicleDTO.isReserved());
			vehicle = vehicleService.save(vehicle);
			return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteVehicle(@PathVariable Long id) 
	{
		try 
		{
			this.vehicleService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}