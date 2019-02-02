package org.isa.takeoff.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.isa.takeoff.dto.LocationDTO;
import org.isa.takeoff.dto.FlightDTO;
import org.isa.takeoff.dto.TicketDTO;
import org.isa.takeoff.model.AirCompany;
import org.isa.takeoff.model.Location;
import org.isa.takeoff.model.Flight;
import org.isa.takeoff.model.FlightDiagram;
import org.isa.takeoff.model.Ticket;
import org.isa.takeoff.service.AirCompanyService;
import org.isa.takeoff.service.LocationService;
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

	@Autowired
	private LocationService destinationService;

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
					flightDTO.getTicketPrice());

			List<Ticket> tickets = new ArrayList<>();
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (!(i == 4 || j == 4 || (i == 0 && (j == 0 || j == 8)))) {
						Ticket t = new Ticket();
						t.setNumber(i * 9 + j);
						t.setFlight(flight);
						tickets.add(t);
					}
				}
			}

			FlightDiagram diagram = new FlightDiagram(9, 9, new ArrayList<>(Arrays.asList(4)),
					new ArrayList<>(Arrays.asList(4)));

			flight.setDiagram(diagram);
			flight.setTickets(tickets);
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
			flight.setTicketPrice(flightDTO.getTicketPrice());
			flight = flightService.save(flight);

			return new ResponseEntity<>(new FlightDTO(flight), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(value = "/{id}/destinations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LocationDTO>> getFlightDestinations(@PathVariable Long id) {

		try {
			Flight flight = flightService.findOne(id);
			List<Location> destinations = flight.getTransferDestinations();

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
	public ResponseEntity<List<LocationDTO>> setFlightDestinations(@PathVariable Long id,
			@RequestBody List<LocationDTO> destinationsDTO) {

		try {
			Flight flight = flightService.findOne(id);

			List<Location> destinations = new ArrayList<>();
			for (LocationDTO d : destinationsDTO) {
				destinations.add(destinationService.findOne(d.getId()));
			}

			flight.setTransferDestinations(destinations);
			flightService.save(flight);

			return new ResponseEntity<>(destinationsDTO, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(value = "/{id}/tickets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TicketDTO>> getFlightTickets(@PathVariable Long id) {

		try {
			Flight flight = flightService.findOne(id);
			List<Ticket> tickets = flight.getTickets();

			List<TicketDTO> ticketsDTO = new ArrayList<>();
			for (Ticket t : tickets) {
				ticketsDTO.add(new TicketDTO(t));
			}

			return new ResponseEntity<>(ticketsDTO, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{id}/reservations", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TicketDTO>> reserveFlightTickets(@PathVariable Long id,
			@RequestBody List<TicketDTO> ticketsDTO) {

		try {
			Flight flight = flightService.findOne(id);
			List<Ticket> tickets = flight.getTickets();

//			List<TicketDTO> ticketsDTO = new ArrayList<>();
//			for (Ticket t : tickets) {
//				ticketsDTO.add(new TicketDTO(t));
//			}

			return new ResponseEntity<>(ticketsDTO, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
