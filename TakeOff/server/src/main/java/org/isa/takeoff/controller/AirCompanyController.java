package org.isa.takeoff.controller;

import java.util.ArrayList;
import java.util.List;

import org.isa.takeoff.dto.AirCompanyDTO;
import org.isa.takeoff.dto.DestinationDTO;
import org.isa.takeoff.dto.FlightDTO;
import org.isa.takeoff.model.AirCompany;
import org.isa.takeoff.model.Destination;
import org.isa.takeoff.model.Flight;
import org.isa.takeoff.service.AirCompanyService;
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

		AirCompany company = new AirCompany(companyDTO.getName(), companyDTO.getAddress(), companyDTO.getDescription());
		company = airCompanyService.save(company);

		return new ResponseEntity<>(new AirCompanyDTO(company), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AirCompanyDTO> updateCompany(@RequestBody AirCompanyDTO companyDTO) {

		try {
			AirCompany company = airCompanyService.findOne(companyDTO.getId());
			company.setName(companyDTO.getName());
			company.setAddress(companyDTO.getAddress());
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
	public ResponseEntity<List<DestinationDTO>> getCompanyDestinations(@PathVariable Long id) {

		try {
			AirCompany company = airCompanyService.findOne(id);
			List<Destination> destinations = company.getDestinations();

			List<DestinationDTO> destinationsDTO = new ArrayList<>();
			for (Destination d : destinations) {
				destinationsDTO.add(new DestinationDTO(d));
			}

			return new ResponseEntity<>(destinationsDTO, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
