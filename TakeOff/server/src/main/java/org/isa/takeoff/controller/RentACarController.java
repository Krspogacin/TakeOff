package org.isa.takeoff.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.isa.takeoff.dto.AvailableVehiclesDTO;
import org.isa.takeoff.dto.OfficeDTO;
import org.isa.takeoff.dto.RentACarDTO;
import org.isa.takeoff.dto.RentACarMainServiceDTO;
import org.isa.takeoff.dto.VehicleDTO;
import org.isa.takeoff.dto.VehiclePriceDTO;
import org.isa.takeoff.dto.VehicleReservationParametersDTO;
import org.isa.takeoff.model.Location;
import org.isa.takeoff.model.Office;
import org.isa.takeoff.model.RentACar;
import org.isa.takeoff.model.RentACarMainService;
import org.isa.takeoff.model.RentACarRating;
import org.isa.takeoff.model.Vehicle;
import org.isa.takeoff.model.VehiclePrice;
import org.isa.takeoff.model.VehicleRating;
import org.isa.takeoff.model.VehicleReservation;
import org.isa.takeoff.service.LocationService;
import org.isa.takeoff.service.OfficeService;
import org.isa.takeoff.service.RentACarMainServiceService;
import org.isa.takeoff.service.RentACarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/rent-a-cars")
public class RentACarController 
{
	@Autowired
	private RentACarService rentACarService;
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private RentACarMainServiceService rentACarMainServiceService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@RequestMapping(method = GET, value = "/checkMainServiceName/{id}/{name}")
	public ResponseEntity<?> checkMainServiceName(@PathVariable("id") Long id, @PathVariable("name") String name)
	{
		RentACarMainService rentACarMainService = this.rentACarMainServiceService.findByName(name);
		if (rentACarMainService != null)
		{
			if (id != null && rentACarMainService.getId().equals(id)) 
			{
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
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
	
	@RequestMapping(value = "/ratings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Double>> getRentACarRatings() 
	{
		List<RentACar> rentACars = rentACarService.findAll();
		List<Double> allRatings = new ArrayList<>();
		for (RentACar rentACar : rentACars)
		{
			List<RentACarRating> ratings = rentACar.getRentACarRatings();
			
			if (ratings.isEmpty())
			{
				allRatings.add(0.0);
				continue;
			}
			
			AtomicReference<Double> ratingsSum = new AtomicReference<Double>(0.0);
			ratings.forEach(rating -> ratingsSum.accumulateAndGet(rating.getRating(), (x, y) -> x + y));
			allRatings.add(ratingsSum.get() / ratings.size());
		}
		return new ResponseEntity<>(allRatings, HttpStatus.OK);
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
	//@PreAuthorize("hasRole('SYS_ADMIN')")
	public ResponseEntity<RentACarDTO> addRentACar(@RequestBody RentACarDTO rentACarDTO) 
	{
		if (rentACarDTO.getLocation() == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		RentACar rentACar = new RentACar(rentACarDTO);
		Location location = this.locationService.findOneByLatitudeAndLongitude(rentACarDTO.getLocation().getLatitude(),
																			   rentACarDTO.getLocation().getLongitude());
		
		if (location == null) 
		{
			location = new Location(rentACarDTO.getLocation());
			location = this.locationService.save(location);
		}
		
		rentACar.setLocation(location);
		rentACar = rentACarService.save(rentACar);
		return new ResponseEntity<>(new RentACarDTO(rentACar), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	//@PreAuthorize("hasRole('RENTACAR_ADMIN')")
	public ResponseEntity<RentACarDTO> updateRentACar(@RequestBody RentACarDTO rentACarDTO) 
	{
		if (rentACarDTO.getLocation() == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		try 
		{
			RentACar rentACar = rentACarService.findOne(rentACarDTO.getId());

			if (rentACarDTO.getLocation().getId() == null)
			{				
				Location location = this.locationService.findOneByLatitudeAndLongitude(rentACarDTO.getLocation().getLatitude(),
						   			rentACarDTO.getLocation().getLongitude());
	
				if (location == null) 
				{
					location = new Location(rentACarDTO.getLocation());
					location = this.locationService.save(location);
				}
				rentACar.setLocation(location);
			}
			
			rentACar.setName(rentACarDTO.getName());
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
	
	@RequestMapping(value = "/{id}/vehiclesOnDiscount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<VehicleDTO>> getVehiclesOnDiscount(@PathVariable Long id) 
	{
		try 
		{
			RentACar rentACar = rentACarService.findOne(id);
			List<Vehicle> vehicles = rentACar.getVehicles();
		
			List<VehicleDTO> vehicleDTO = new ArrayList<>();
			for (Vehicle vehicle : vehicles)
			{
				if (vehicle.getDiscount() != null && vehicle.getDiscount() > 0)
				{
					vehicleDTO.add(new VehicleDTO(vehicle));					
				}
			}
			return new ResponseEntity<>(vehicleDTO, HttpStatus.OK);
		}
		catch (Exception e) 
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{id}/areThereAvailableVehiclesNotOnDiscount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> areThereAvailableVehiclesNotOnDiscount(@PathVariable Long id) 
	{
		try 
		{
			RentACar rentACar = rentACarService.findOne(id);
			List<Vehicle> vehicles = rentACar.getVehicles();
		
			for (Vehicle vehicle : vehicles)
			{
				if (vehicle.getDiscount() == null || vehicle.getDiscount() == 0)
				{
					return new ResponseEntity<>(true, HttpStatus.OK);
				}
			}
			
			return new ResponseEntity<>(false, HttpStatus.OK);
		}
		catch (Exception e) 
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/vehicles/availableVehicles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AvailableVehiclesDTO>> getAvailableVehicles(@RequestParam String parametersDTO) 
	{
		VehicleReservationParametersDTO parameters;
		try
		{
			parameters = objectMapper.readValue(parametersDTO, VehicleReservationParametersDTO.class);
		} 
		catch (IOException e)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		System.out.println(parameters);
		if (parameters.getStartDate() == null || parameters.getEndDate() == null || parameters.getNumOfPassengers() == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		try 
		{
			RentACar rentACar = rentACarService.findOne(parameters.getRentACarId());
			List<RentACarMainService> rentACarMainServices = rentACar.getMainServicesRentACar();
			long reservationDays = ChronoUnit.DAYS.between(parameters.getStartDate(), parameters.getEndDate()) + 1;
			Long mainServiceId = null;
			
			for(RentACarMainService mainService : rentACarMainServices)
			{
				if (mainService.getStartDay() <= reservationDays &&
				   ((mainService.getEndDay() != null && reservationDays <= mainService.getEndDay()) || mainService.getEndDay() == null))
				{
					mainServiceId = mainService.getId();
					break;
				}
			}
			
			List<Vehicle> vehicles = rentACar.getVehicles();
			
			List<AvailableVehiclesDTO> availableVehicles = new ArrayList<>();
			for (Vehicle vehicle : vehicles)
			{
				//If vehicle is on discount, skip it
				if (vehicle.getDiscount() != null && vehicle.getDiscount() > 0) 
				{
					continue;					
				}
					
				//If vehicle is already reserved in entered period, also skip it
				List<VehicleReservation> vehicleReservations = vehicle.getVehicleReservations();				
				boolean reserved = false;
				for (VehicleReservation vehicleReservation : vehicleReservations)
				{
					if ((vehicleReservation.getReservationStartDate().isBefore(parameters.getStartDate()) &&
						 vehicleReservation.getReservationEndDate().isAfter(parameters.getStartDate())) || 
						(vehicleReservation.getReservationStartDate().isBefore(parameters.getEndDate()) &&
						 vehicleReservation.getReservationEndDate().isAfter(parameters.getEndDate())))
					{
						reserved = true;
						break;
					}
				}
				
				if (reserved)
				{
					continue;
				}
				
				//If vehicle has enough number of seats for requested number of passengers, skip it
				if (vehicle.getNumOfSeats() < parameters.getNumOfPassengers())
				{
					continue;
				}
				
				//If vehicle fuel type is not one of requested, skip it
				if (parameters.getFuelTypes() != null && !parameters.getFuelTypes().isEmpty() &&
				   !parameters.getFuelTypes().contains(vehicle.getFuel()))
				{
					continue;
				}
				
				//If vehicle transmission type is not one of requested, skip it
				if(parameters.getTransmissionTypes() != null && !parameters.getTransmissionTypes().isEmpty() &&
				  !parameters.getTransmissionTypes().contains(vehicle.getTransmission()))
				{
					continue;
				}
				
				//Find and check is vehicle price in requested range. If not, skip it
				List<VehiclePrice> prices = vehicle.getVehiclePrices();
				Double vehiclePrice = null;
				for (VehiclePrice price : prices)
				{
					if (price.getRentACarMainService().getId() == mainServiceId)
					{					
						if ((parameters.getMinPrice() == null || parameters.getMinPrice() <= price.getPrice()) &&
							(parameters.getMaxPrice() == null || parameters.getMaxPrice() >= price.getPrice()))
						vehiclePrice = price.getPrice();
						break;
					}
				}
				
				if (vehiclePrice == null)
				{
					continue;
				}
				
				List<VehicleRating> ratings = vehicle.getVehicleRatings();
				Double vehicleRating = 0.0;
				
				if (!ratings.isEmpty())
				{
					AtomicReference<Double> ratingsSum = new AtomicReference<Double>(0.0);
					ratings.forEach(rating -> ratingsSum.accumulateAndGet(rating.getRating(), (x, y) -> x + y));
					vehicleRating = ratingsSum.get() / ratings.size();
				}
				
				AvailableVehiclesDTO availableVehicle = new AvailableVehiclesDTO(new VehicleDTO(vehicle), vehiclePrice * reservationDays, vehicleRating);
				availableVehicles.add(availableVehicle);
			}
			return new ResponseEntity<>(availableVehicles, HttpStatus.OK);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{id}/rating", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Double> getRentACarRating(@PathVariable Long id) 
	{
		try 
		{
			RentACar rentACar = rentACarService.findOne(id);
			List<RentACarRating> ratings = rentACar.getRentACarRatings();
			
			if (ratings.isEmpty())
			{
				return new ResponseEntity<>(new Double(0), HttpStatus.OK);
			}
			
			AtomicReference<Double> ratingsSum = new AtomicReference<Double>(0.0);
			ratings.forEach(rating -> ratingsSum.accumulateAndGet(rating.getRating(), (x, y) -> x + y));
			return new ResponseEntity<>(ratingsSum.get() / ratings.size(), HttpStatus.OK);
		}
		catch (Exception e) 
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{id}/vehicles/ratings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Double>> getVehiclesRatings(@PathVariable Long id) 
	{
		try
		{
			RentACar rentACar = rentACarService.findOne(id);
			List<Vehicle> vehicles = rentACar.getVehicles();
			List<Double> allRatings = new ArrayList<>();
			for (Vehicle vehicle : vehicles)
			{
				List<VehicleRating> ratings = vehicle.getVehicleRatings();
				
				if (ratings.isEmpty())
				{
					allRatings.add(0.0);
					continue;
				}
				
				AtomicReference<Double> ratingsSum = new AtomicReference<Double>(0.0);
				ratings.forEach(rating -> ratingsSum.accumulateAndGet(rating.getRating(), (x, y) -> x + y));
				allRatings.add(ratingsSum.get() / ratings.size());
			}
			return new ResponseEntity<>(allRatings, HttpStatus.OK);			
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{id}/mainServices", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RentACarMainServiceDTO>> getMainServices(@PathVariable Long id) 
	{
		try 
		{
			RentACar rentACar = rentACarService.findOne(id);
			List<RentACarMainService> mainServices = rentACar.getMainServicesRentACar();
			
			List<RentACarMainServiceDTO> mainServiceDTO = new ArrayList<>();
			for (RentACarMainService service : mainServices)
			{
				mainServiceDTO.add(new RentACarMainServiceDTO(service));
			}
			return new ResponseEntity<>(mainServiceDTO, HttpStatus.OK);
		}
		catch (Exception e) 
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{id}/mainServicesPrices", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<List<VehiclePriceDTO>>> getMainServicesPrices(@PathVariable Long id) 
	{
		try 
		{
			RentACar rentACar = rentACarService.findOne(id);
			List<RentACarMainService> mainServices = rentACar.getMainServicesRentACar();
			
			List<List<VehiclePriceDTO>> vehiclesAndPricesDTO = new ArrayList<>();
			
			for (RentACarMainService service : mainServices)
			{
				List<VehiclePrice> vehiclePrices = service.getVehiclesAndPrices();
				List<VehiclePriceDTO> vehiclePricesDTO = new ArrayList<>();
				for (VehiclePrice vehiclePrice : vehiclePrices)
				{
					vehiclePricesDTO.add(new VehiclePriceDTO(vehiclePrice));
				}
				vehiclesAndPricesDTO.add(vehiclePricesDTO);					
			}
			
			return new ResponseEntity<>(vehiclesAndPricesDTO, HttpStatus.OK);
		}
		catch (Exception e) 
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/mainServices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RentACarMainServiceDTO> addMainService(@RequestBody RentACarMainServiceDTO rentACarMainServiceDTO) 
	{
		if (rentACarMainServiceDTO.getRentACar() == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try 
		{
			RentACar rentACar = this.rentACarService.findOne(rentACarMainServiceDTO.getRentACar().getId());
			RentACarMainService rentACarMainService = new RentACarMainService(rentACarMainServiceDTO.getName(), rentACarMainServiceDTO.getStartDay(), rentACarMainServiceDTO.getEndDay());
			rentACarMainService.setRentACar(rentACar);
			rentACarMainService = this.rentACarMainServiceService.save(rentACarMainService);
			return new ResponseEntity<>(new RentACarMainServiceDTO(rentACarMainService), HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/mainServices", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RentACarMainServiceDTO> updateMainService(@RequestBody RentACarMainServiceDTO rentACarMainServiceDTO) 
	{
		if (rentACarMainServiceDTO.getRentACar() == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try 
		{
			RentACarMainService rentACarMainService = this.rentACarMainServiceService.findOne(rentACarMainServiceDTO.getId());
			rentACarMainService.setName(rentACarMainServiceDTO.getName());
			rentACarMainService.setStartDay(rentACarMainServiceDTO.getStartDay());
			rentACarMainService.setEndDay(rentACarMainServiceDTO.getEndDay());
			rentACarMainService = this.rentACarMainServiceService.save(rentACarMainService);
			return new ResponseEntity<>(new RentACarMainServiceDTO(rentACarMainService), HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	
	
	@RequestMapping(value = "/mainServices/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMainService(@PathVariable Long id) 
	{
		try 
		{
			this.rentACarMainServiceService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/offices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OfficeDTO> addOffice(@RequestBody OfficeDTO officeDTO) 
	{
		if (officeDTO.getRentACar() == null || officeDTO.getLocation() == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try 
		{
			RentACar rentACar = this.rentACarService.findOne(officeDTO.getRentACar().getId());
			Location location = this.locationService.findOneByLatitudeAndLongitude(officeDTO.getLocation().getLatitude(),
																				   officeDTO.getLocation().getLongitude());
			if (location == null) 
			{
				location = new Location(officeDTO.getLocation());
				location = this.locationService.save(location);
			}
			
			Office office = new Office(officeDTO.getName());
			office.setRentACar(rentACar);
			office.setLocation(location);
			office = this.officeService.save(office); 
			return new ResponseEntity<>(new OfficeDTO(office), HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/offices", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OfficeDTO> updateOffice(@RequestBody OfficeDTO officeDTO) 
	{
		if (officeDTO.getRentACar() == null || officeDTO.getLocation() == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try 
		{
			Office office = this.officeService.findOne(officeDTO.getId());
			
			if (officeDTO.getLocation().getId() == null)
			{				
				Location location = this.locationService.findOneByLatitudeAndLongitude(officeDTO.getLocation().getLatitude(),
																					   officeDTO.getLocation().getLongitude());
	
				if (location == null) 
				{
					location = new Location(officeDTO.getLocation());
					location = this.locationService.save(location);
				}
				office.setLocation(location);
			}
			
			office.setName(officeDTO.getName());
			office = this.officeService.save(office);
			return new ResponseEntity<>(new OfficeDTO(office), HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	
	
	@RequestMapping(value = "/offices/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteOffice(@PathVariable Long id) 
	{
		try 
		{
			this.officeService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{id}/offices", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OfficeDTO>> getOffices(@PathVariable Long id) 
	{
		try 
		{
			RentACar rentACar = rentACarService.findOne(id);
			List<Office> offices = rentACar.getOffices();
			
			List<OfficeDTO> officeDTO = new ArrayList<>();
			for (Office office : offices)
			{
				officeDTO.add(new OfficeDTO(office));
			}
			return new ResponseEntity<>(officeDTO, HttpStatus.OK);
		}
		catch (Exception e) 
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}