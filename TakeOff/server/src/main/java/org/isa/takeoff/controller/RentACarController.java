package org.isa.takeoff.controller;

import java.util.ArrayList;
import java.util.List;

import org.isa.takeoff.dto.RentACarDTO;
import org.isa.takeoff.dto.VehicleDTO;
import org.isa.takeoff.model.RentACar;
import org.isa.takeoff.model.Vehicle;
import org.isa.takeoff.service.RentACarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rent-a-cars")
public class RentACarController 
{
	@Autowired
	private RentACarService rentACarService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RentACarDTO>> getRentACars() 
	{
		List<RentACar> rentACars = rentACarService.findAll();

		List<RentACarDTO> rentACarDTOs = new ArrayList<>();
		for (RentACar rentACar : rentACars)
		{
			rentACarDTOs.add(new RentACarDTO(rentACar));
		}

		return new ResponseEntity<>(rentACarDTOs, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RentACarDTO> getRentACar(@PathVariable Long id) 
	{
		try 
		{
			RentACar rentACar = rentACarService.findOne(id);
			return new ResponseEntity<>(new RentACarDTO(rentACar), HttpStatus.OK);
		}
		catch (Exception e) 
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('SYS_ADMIN')")
	public ResponseEntity<RentACarDTO> addRentACar(@RequestBody RentACarDTO rentACarDTO) 
	{
		RentACar rentACar = new RentACar(rentACarDTO);
		rentACar = rentACarService.save(rentACar);
		return new ResponseEntity<>(new RentACarDTO(rentACar), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	//@PreAuthorize("hasRole('RENTACAR_ADMIN')")
	public ResponseEntity<RentACarDTO> updateRentACar(@RequestBody RentACarDTO rentACarDTO) 
	{
		try 
		{
			RentACar rentACar = rentACarService.findOne(rentACarDTO.getId());
			rentACar.setName(rentACarDTO.getName());
			rentACar.setAddress(rentACarDTO.getAddress());
			rentACar.setDescription(rentACarDTO.getDescription());
			rentACar = rentACarService.save(rentACar);
			return new ResponseEntity<>(new RentACarDTO(rentACar), HttpStatus.OK);
		}
		catch (Exception e) 
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{id}/vehicles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<VehicleDTO>> getVehicles(@PathVariable Long id) 
	{
		try 
		{
			RentACar rentACar = rentACarService.findOne(id);
			List<Vehicle> vehicles = rentACar.getVehicles();
		
			List<VehicleDTO> vehicleDTO = new ArrayList<>();
			for (Vehicle vehicle : vehicles)
			{
				vehicleDTO.add(new VehicleDTO(vehicle));
			}
			return new ResponseEntity<>(vehicleDTO, HttpStatus.OK);
		}
		catch (Exception e) 
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}