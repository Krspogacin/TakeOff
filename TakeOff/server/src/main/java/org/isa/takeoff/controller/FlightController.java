package org.isa.takeoff.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.isa.takeoff.dto.FlightDTO;
import org.isa.takeoff.dto.FlightDiagramDTO;
import org.isa.takeoff.dto.LocationDTO;
import org.isa.takeoff.dto.TicketDTO;
import org.isa.takeoff.dto.UserRatingFlightDTO;
import org.isa.takeoff.model.AirCompany;
import org.isa.takeoff.model.Flight;
import org.isa.takeoff.model.FlightDiagram;
import org.isa.takeoff.model.FlightRating;
import org.isa.takeoff.model.FlightRatingId;
import org.isa.takeoff.model.Location;
import org.isa.takeoff.model.Ticket;
import org.isa.takeoff.model.User;
import org.isa.takeoff.service.AirCompanyService;
import org.isa.takeoff.service.FlightService;
import org.isa.takeoff.service.LocationService;
import org.isa.takeoff.service.UserService;
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
@RequestMapping(value = "/flights")
public class FlightController {

	@Autowired
	private FlightService flightService;

	@Autowired
	private AirCompanyService airCompanyService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private UserService userService;
	
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
	@PreAuthorize("hasRole('ROLE_AIRCOMPANY_ADMIN')")
	public ResponseEntity<FlightDTO> addFlight(@RequestBody FlightDTO flightDTO) {

		if (flightDTO.getCompany() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try {
			AirCompany company = airCompanyService.findOne(flightDTO.getCompany().getId());

			Flight flight = new Flight(flightDTO.getTakeOffDate(), flightDTO.getLandingDate(),
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

			Location takeOffLocation = this.locationService.findOneByLatitudeAndLongitude(
					flightDTO.getTakeOffLocation().getLatitude(), flightDTO.getTakeOffLocation().getLongitude());

			if (takeOffLocation == null) {
				takeOffLocation = new Location(flightDTO.getTakeOffLocation());
				takeOffLocation = this.locationService.save(takeOffLocation);
			}

			Location landingLocation = this.locationService.findOneByLatitudeAndLongitude(
					flightDTO.getLandingLocation().getLatitude(), flightDTO.getLandingLocation().getLongitude());

			if (landingLocation == null) {
				landingLocation = new Location(flightDTO.getLandingLocation());
				landingLocation = this.locationService.save(landingLocation);
			}

			flight.setDiagram(diagram);
			flight.setTickets(tickets);
			flight.setTakeOffLocation(takeOffLocation);
			flight.setLandingLocation(landingLocation);
			flight.setCompany(company);
			flight = flightService.save(flight);

			return new ResponseEntity<>(new FlightDTO(flight), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_AIRCOMPANY_ADMIN')")
	public ResponseEntity<FlightDTO> updateFlight(@RequestBody FlightDTO flightDTO) {

		try {
			Flight flight = flightService.findOne(flightDTO.getId());
			flight.setTakeOffDate(flightDTO.getTakeOffDate());
			flight.setLandingDate(flightDTO.getLandingDate());
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
	@PreAuthorize("hasRole('ROLE_AIRCOMPANY_ADMIN')")
	public ResponseEntity<List<LocationDTO>> setFlightDestinations(@PathVariable Long id,
			@RequestBody List<LocationDTO> destinationsDTO) {

		try {
			Flight flight = flightService.findOne(id);

			List<Location> destinations = new ArrayList<>();
			for (LocationDTO dest : destinationsDTO) {

				Location location = this.locationService.findOneByLatitudeAndLongitude(dest.getLatitude(),
						dest.getLongitude());

				if (location == null) {
					location = new Location(dest);
					location = this.locationService.save(location);
				}

				destinations.add(location);
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

	@RequestMapping(value = "/tickets", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_AIRCOMPANY_ADMIN')")
	public ResponseEntity<List<TicketDTO>> updateFlightTickets(@RequestBody List<TicketDTO> ticketsDTO) {

		try {
			Flight flight = flightService.findOne(ticketsDTO.get(0).getFlight().getId());
			List<Ticket> tickets = flight.getTickets();

			for (TicketDTO ticketDTO : ticketsDTO) {
				for (Ticket ticket : tickets) {
					if (ticketDTO.getId().equals(ticket.getId())) {
						ticket.setIsOnDiscount(ticketDTO.isOnDiscount());
						break;
					}
				}
			}

			flightService.save(flight);

			return new ResponseEntity<>(ticketsDTO, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(value = "/{id}/rating", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Double> getFlightRating(@PathVariable Long id) {
		try {
			Flight flight = flightService.findOne(id);
			List<FlightRating> ratings = flight.getFlightRatings();

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

	@RequestMapping(value = "/{id}/diagram", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_AIRCOMPANY_ADMIN')")
	public ResponseEntity<FlightDiagramDTO> updateFlightDiagram(@PathVariable Long id,
			@RequestBody FlightDiagramDTO diagramDTO) {

		try {
			Flight flight = flightService.findOne(id);
			FlightDiagram diagram = flight.getDiagram();

			List<Ticket> oldTickets = flight.getTickets();

			// delete old tickets if they are in disabled rows
			for (Ticket t : oldTickets) {
				int number = (int) Math.floor(t.getNumber() / diagramDTO.getCols());
				if (diagramDTO.getDisabledRows().contains(number)) {
					if (t.getIsReserved()) {
						return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					}
					flight.removeTicket(t);
				}
			}

			// add new tickets if there are new rows
			for (int i = diagram.getRows(); i < diagramDTO.getRows(); i++) {
				for (int j = 0; j < diagram.getCols(); j++) {
					Ticket t = new Ticket();
					t.setNumber(i * diagramDTO.getRows() + j);
					t.setFlight(flight);
					flight.addTicket(t);
				}
			}

			diagram.setRows(diagramDTO.getRows());
			diagram.setCols(diagramDTO.getCols());
			diagram.setDisabledRows(diagramDTO.getDisabledRows());
			diagram.setDisabledCols(diagramDTO.getDisabledCols());

			flightService.save(flight);

			return new ResponseEntity<>(diagramDTO, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value="/rateFlight", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> rateFlight(@RequestBody UserRatingFlightDTO userRatingFlightDTO) 
	{
		if (userRatingFlightDTO.getFlight() == null || userRatingFlightDTO.getRating() == null || userRatingFlightDTO.getUsername() == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		User user = null;
		try
		{
			user = this.userService.findByUsernameUser(userRatingFlightDTO.getUsername());
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		try
		{
			Flight flight = this.flightService.findOne(userRatingFlightDTO.getFlight().getId());
			FlightRating flightRating = this.flightService.findOneRating(new FlightRatingId(flight, user));
			if (flightRating == null )
			{				
				flightRating = new FlightRating();
				flightRating.setId(new FlightRatingId(flight, user));
			}
			flightRating.setRating(userRatingFlightDTO.getRating());
			this.flightService.saveRating(flightRating);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}