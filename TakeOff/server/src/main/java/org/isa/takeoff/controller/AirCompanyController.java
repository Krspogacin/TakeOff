package org.isa.takeoff.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.isa.takeoff.dto.AirCompanyDTO;
import org.isa.takeoff.dto.FlightDTO;
import org.isa.takeoff.dto.LocationDTO;
import org.isa.takeoff.model.AirCompany;
import org.isa.takeoff.model.AirCompanyRating;
import org.isa.takeoff.model.Flight;
import org.isa.takeoff.model.FlightRating;
import org.isa.takeoff.model.Location;
import org.isa.takeoff.service.AirCompanyService;
import org.isa.takeoff.service.LocationService;
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
@RequestMapping(value = "/companies")
public class AirCompanyController {

	@Autowired
	private AirCompanyService airCompanyService;

	@Autowired
	private LocationService locationService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AirCompanyDTO>> getCompanies() {

		List<AirCompany> companies = airCompanyService.findAll();

		// convert companies to DTOs
		List<AirCompanyDTO> companiesDTO = new ArrayList<>();
		for (AirCompany company : companies) {
			companiesDTO.add(new AirCompanyDTO(company));
		}

		return new ResponseEntity<>(companiesDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AirCompanyDTO> getCompany(@PathVariable Long id) {

		try {
			AirCompany company = airCompanyService.findOne(id);
			AirCompanyDTO companyDTO = new AirCompanyDTO(company);

			return new ResponseEntity<>(companyDTO, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AirCompanyDTO> addCompany(@RequestBody AirCompanyDTO companyDTO) {

		AirCompany company = new AirCompany(companyDTO.getName(), companyDTO.getDescription());
		Location location = this.locationService.findOneByLatitudeAndLongitude(companyDTO.getLocation().getLatitude(),
				companyDTO.getLocation().getLongitude());

		if (location == null) {
			location = new Location(companyDTO.getLocation());
			location = this.locationService.save(location);
		}

		company.setLocation(location);
		company = airCompanyService.save(company);

		return new ResponseEntity<>(new AirCompanyDTO(company), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AirCompanyDTO> updateCompany(@RequestBody AirCompanyDTO companyDTO) {

		try {
			AirCompany company = airCompanyService.findOne(companyDTO.getId());

			if (companyDTO.getLocation().getId() == null) {
				Location location = this.locationService.findOneByLatitudeAndLongitude(
						companyDTO.getLocation().getLatitude(), companyDTO.getLocation().getLongitude());

				if (location == null) {
					location = new Location(companyDTO.getLocation());
					location = this.locationService.save(location);
				}
				company.setLocation(location);
			}

			company.setName(companyDTO.getName());
			company.setDescription(companyDTO.getDescription());
			company = airCompanyService.save(company);

			return new ResponseEntity<>(new AirCompanyDTO(company), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(value = "/{id}/flights", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FlightDTO>> getCompanyFlights(@PathVariable Long id) {

		try {
			AirCompany company = airCompanyService.findOne(id);
			List<Flight> flights = company.getFlights();

			List<FlightDTO> flightsDTO = new ArrayList<>();
			for (Flight f : flights) {
				flightsDTO.add(new FlightDTO(f));
			}

			return new ResponseEntity<>(flightsDTO, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/{id}/destinations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LocationDTO>> getCompanyDestinations(@PathVariable Long id) {

		try {
			AirCompany company = airCompanyService.findOne(id);
			List<Location> destinations = company.getDestinations();

			List<LocationDTO> destinationsDTO = new ArrayList<>();
			for (Location d : destinations) {
				destinationsDTO.add(new LocationDTO(d));
			}

			return new ResponseEntity<>(destinationsDTO, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/{id}/destinations", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LocationDTO>> setCompanyDestinations(@PathVariable Long id,
			@RequestBody List<LocationDTO> destinationsDTO) {

		try {
			AirCompany company = airCompanyService.findOne(id);

			List<Location> destinations = new ArrayList<>();
			for (LocationDTO destDTO : destinationsDTO) {

				Location location = this.locationService.findOneByLatitudeAndLongitude(destDTO.getLatitude(),
						destDTO.getLongitude());

				if (location == null) {
					location = new Location(destDTO);
					location = this.locationService.save(location);
				}

				destinations.add(location);
			}

			company.setDestinations(destinations);
			airCompanyService.save(company);

			return new ResponseEntity<>(destinationsDTO, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/destinations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LocationDTO>> getAllDestinations() {

		try {
			List<Location> destinations = locationService.findAll();

			List<LocationDTO> destinationsDTO = new ArrayList<>();
			for (Location d : destinations) {
				destinationsDTO.add(new LocationDTO(d));
			}

			return new ResponseEntity<>(destinationsDTO, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/ratings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Double>> getCompaniesRatings() {
		List<AirCompany> companies = airCompanyService.findAll();
		List<Double> allRatings = new ArrayList<>();

		for (AirCompany company : companies) {
			List<AirCompanyRating> ratings = company.getCompanyRatings();

			if (ratings.isEmpty()) {
				allRatings.add(0.0);
				continue;
			}

			AtomicReference<Double> ratingsSum = new AtomicReference<Double>(0.0);
			ratings.forEach(rating -> ratingsSum.accumulateAndGet(rating.getRating(), (x, y) -> x + y));
			allRatings.add(ratingsSum.get() / ratings.size());
		}

		return new ResponseEntity<>(allRatings, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/rating", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Double> getCompanyRating(@PathVariable Long id) {
		try {
			AirCompany company = airCompanyService.findOne(id);
			List<AirCompanyRating> ratings = company.getCompanyRatings();

			if (ratings.isEmpty()) {
				return new ResponseEntity<>(new Double(0), HttpStatus.OK);
			}

			AtomicReference<Double> ratingsSum = new AtomicReference<Double>(0.0);
			ratings.forEach(rating -> ratingsSum.accumulateAndGet(rating.getRating(), (x, y) -> x + y));

			return new ResponseEntity<>(ratingsSum.get() / ratings.size(), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/{id}/flights/ratings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Double>> getFlightsRatings(@PathVariable Long id) {
		try {
			AirCompany company = airCompanyService.findOne(id);
			List<Flight> flights = company.getFlights();
			List<Double> allRatings = new ArrayList<>();

			for (Flight flight : flights) {
				List<FlightRating> ratings = flight.getFlightRatings();

				if (ratings.isEmpty()) {
					allRatings.add(0.0);
					continue;
				}

				AtomicReference<Double> ratingsSum = new AtomicReference<Double>(0.0);
				ratings.forEach(rating -> ratingsSum.accumulateAndGet(rating.getRating(), (x, y) -> x + y));
				allRatings.add(ratingsSum.get() / ratings.size());
			}

			return new ResponseEntity<>(allRatings, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
