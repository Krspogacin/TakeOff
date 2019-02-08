package org.isa.takeoff.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.isa.takeoff.dto.UserRatingVehicleDTO;
import org.isa.takeoff.dto.VehicleDTO;
import org.isa.takeoff.dto.VehiclePriceDTO;
import org.isa.takeoff.model.FuelType;
import org.isa.takeoff.model.RentACar;
import org.isa.takeoff.model.RentACarMainService;
import org.isa.takeoff.model.TransmissionType;
import org.isa.takeoff.model.User;
import org.isa.takeoff.model.Vehicle;
import org.isa.takeoff.model.VehiclePrice;
import org.isa.takeoff.model.VehicleRating;
import org.isa.takeoff.model.VehicleRatingId;
import org.isa.takeoff.model.VehicleReservation;
import org.isa.takeoff.service.RentACarMainServiceService;
import org.isa.takeoff.service.RentACarService;
import org.isa.takeoff.service.UserService;
import org.isa.takeoff.service.VehicleService;
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
@RequestMapping(value = "/vehicles")
public class VehicleController 
{
	@Autowired
	private RentACarService rentACarService;
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private RentACarMainServiceService rentACarMainServiceService;
	
	@Autowired
	private UserService userService;
	
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
	@PreAuthorize("hasRole('ROLE_RENTACAR_ADMIN')")
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
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_RENTACAR_ADMIN')")
	public ResponseEntity<VehicleDTO> updateVehicle(@RequestBody VehicleDTO vehicleDTO) 
	{
		if (vehicleDTO.isReserved() || vehicleDTO.getRentACar() == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		try 
		{
			Vehicle vehicle = this.vehicleService.findOne(vehicleDTO.getId());
			
			List<VehicleReservation> vehicleReservations =  vehicle.getVehicleReservations();
			for (VehicleReservation vehicleReservation : vehicleReservations)
			{
				if (vehicleReservation.getReservationEndDate().isAfter(LocalDate.now()))
				{
					return new ResponseEntity<>(HttpStatus.FORBIDDEN); 
				}
			}
			
			vehicle.setBrand(vehicleDTO.getBrand());
			vehicle.setModel(vehicleDTO.getModel());
			vehicle.setYear(vehicleDTO.getYear());
			vehicle.setImage(vehicleDTO.getImage() == null ? null : vehicleDTO.getImage().getBytes());
			vehicle.setReserved(vehicleDTO.isReserved());
			vehicle.setDiscount(vehicleDTO.getDiscount());
			vehicle = vehicleService.save(vehicle);
			return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_RENTACAR_ADMIN')")
	public ResponseEntity<?> deleteVehicle(@PathVariable Long id) 
	{
		try 
		{
			Vehicle vehicle = this.vehicleService.findOne(id);
			
			if (vehicle.getRentACar() == null)
			{
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			List<VehicleReservation> vehicleReservations =  vehicle.getVehicleReservations();
			for (VehicleReservation vehicleReservation : vehicleReservations)
			{
				if (vehicleReservation.getReservationEndDate().isAfter(LocalDate.now()))
				{
					return new ResponseEntity<>(HttpStatus.FORBIDDEN); 
				}
			}
			
			this.vehicleService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value="/prices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_RENTACAR_ADMIN')")
	public ResponseEntity<List<VehiclePriceDTO>> addVehiclePrices(@RequestBody List<VehiclePriceDTO> vehiclePriceDTOs) 
	{
		try 
		{
			List<VehiclePriceDTO> vehiclePrices = new ArrayList<>();
			for (VehiclePriceDTO vehiclePriceDTO : vehiclePriceDTOs)
			{				
				Vehicle vehicle = this.vehicleService.findOne(vehiclePriceDTO.getVehicle().getId());
				RentACarMainService rentACarMainService = this.rentACarMainServiceService.findOne(vehiclePriceDTO.getRentACarMainServiceDTO().getId());
				
				if (rentACarMainService.getRentACar().getId() != vehicle.getRentACar().getId()) 
				{
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				
				VehiclePrice vehiclePrice = new VehiclePrice(vehiclePriceDTO.getPrice());
				vehiclePrice.setVehicle(vehicle);
				vehiclePrice.setRentACarMainService(rentACarMainService);
				vehiclePrice = this.vehicleService.saveVehiclePrice(vehiclePrice);
				vehiclePrices.add(new VehiclePriceDTO(vehiclePrice));
			}
			return new ResponseEntity<>(vehiclePrices, HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value="/prices", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_RENTACAR_ADMIN')")
	public ResponseEntity<List<VehiclePriceDTO>> updateVehiclePrices(@RequestBody List<VehiclePriceDTO> vehiclePriceDTOs) 
	{
		try 
		{
			List<VehiclePriceDTO> vehiclePrices = new ArrayList<>();
			for (VehiclePriceDTO vehiclePriceDTO : vehiclePriceDTOs)
			{				
				VehiclePrice vehiclePrice = this.vehicleService.findOneVehiclePrice(vehiclePriceDTO.getId());
				vehiclePrice.setPrice(vehiclePriceDTO.getPrice());
				vehiclePrice = this.vehicleService.saveVehiclePrice(vehiclePrice);
				vehiclePrices.add(new VehiclePriceDTO(vehiclePrice));
			}
			return new ResponseEntity<>(vehiclePrices, HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/fuelTypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FuelType>> getFuelTypes() 
	{
		return new ResponseEntity<>(Arrays.asList(FuelType.values()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/transmissionTypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TransmissionType>> getTransmissionTypes() 
	{
		return new ResponseEntity<>(Arrays.asList(TransmissionType.values()), HttpStatus.OK);
	}
	
	@RequestMapping(value="/rateVehicle", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> rateVehicle(@RequestBody UserRatingVehicleDTO userRatingVehicleDTO) 
	{
		if (userRatingVehicleDTO.getVehicle() == null || userRatingVehicleDTO.getRating() == null || userRatingVehicleDTO.getUsername() == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		User user = null;
		try
		{
			user = this.userService.findByUsernameUser(userRatingVehicleDTO.getUsername());
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		try
		{
			Vehicle vehicle = this.vehicleService.findOne(userRatingVehicleDTO.getVehicle().getId());
			VehicleRating vehicleRating = this.vehicleService.findOneRating(new VehicleRatingId(vehicle, user));
			if (vehicleRating == null )
			{				
				vehicleRating = new VehicleRating();
				vehicleRating.setId(new VehicleRatingId(vehicle, user));
			}
			vehicleRating.setRating(userRatingVehicleDTO.getRating());
			this.vehicleService.saveRating(vehicleRating);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}