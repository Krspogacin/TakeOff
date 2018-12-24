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
import org.isa.takeoff.service.FlightService;
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
@RequestMapping(value = "/flights")
public class FlightController {

	@Autowired
	private FlightService flightService;

	@Autowired
	private AirCompanyService airCompanyService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FlightDTO> getFlight(@PathVariable Long id) {

		try {
			Flight flight = flightService.findOne(id);
			FlightDTO flightDTO = new FlightDTO(flight);

			return new ResponseEntity<>(flightDTO, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FlightDTO> addFlight(@RequestBody FlightDTO flightDTO) {

		if (flightDTO.getCompany() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try {
			AirCompany company = airCompanyService.findOne(flightDTO.getCompany().getId());

			Flight flight = new Flight(flightDTO.getTakeOffDate(), flightDTO.getLandingDate(), flightDTO.getDistance(),
					flightDTO.getNumberOfTransfers(), flightDTO.getTicketPrice());

			flight.setCompany(company);
			flight = flightService.save(flight);

			return new ResponseEntity<>(new FlightDTO(flight), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FlightDTO> updateFlight(@RequestBody FlightDTO flightDTO) {

		try {
			Flight flight = flightService.findOne(flightDTO.getId());
			flight.setTakeOffDate(flightDTO.getTakeOffDate());
			flight.setLandingDate(flightDTO.getLandingDate());
			flight.setDistance(flightDTO.getDistance());
			flight.setNumberOfTransfers(flightDTO.getNumberOfTransfers());
			flight.setTicketPrice(flightDTO.getTicketPrice());
			flight = flightService.save(flight);

			return new ResponseEntity<>(new FlightDTO(flight), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(value = "/{id}/destinations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DestinationDTO>> getFlightDestinations(@PathVariable Long id) {

		try {
			Flight flight = flightService.findOne(id);
			List<Destination> destinations = flight.getTransferDestinations();

			List<DestinationDTO> destinationsDTO = new ArrayList<>();
			for (Destination d : destinations) {
				destinationsDTO.add(new DestinationDTO(d));
			}

			return new ResponseEntity<>(destinationsDTO, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/{id}/destinations", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DestinationDTO>> addFlightDestinations(@PathVariable Long id,
			@RequestBody List<DestinationDTO> destinationsDTO) {

		try {
			Flight flight = flightService.findOne(id);
			List<Destination> destinations = flight.getTransferDestinations();

			for (DestinationDTO d : destinationsDTO) {
				
			}

			return new ResponseEntity<>(destinationsDTO, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

}
