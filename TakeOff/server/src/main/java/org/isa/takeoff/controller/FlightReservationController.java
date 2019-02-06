package org.isa.takeoff.controller;

import java.util.ArrayList;
import java.util.List;

import org.isa.takeoff.dto.FlightReservationDTO;
import org.isa.takeoff.dto.ReservationDTO;
import org.isa.takeoff.dto.TicketDTO;
import org.isa.takeoff.dto.UserDTO;
import org.isa.takeoff.dto.VehicleReservationDTO;
import org.isa.takeoff.model.Flight;
import org.isa.takeoff.model.FlightReservation;
import org.isa.takeoff.model.Reservation;
import org.isa.takeoff.model.Ticket;
import org.isa.takeoff.model.User;
import org.isa.takeoff.model.Vehicle;
import org.isa.takeoff.model.VehicleReservation;
import org.isa.takeoff.service.FlightReservationService;
import org.isa.takeoff.service.FlightService;
import org.isa.takeoff.service.UserService;
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
@RequestMapping(value = "/reservations")
public class FlightReservationController {

	@Autowired
	private FlightReservationService reservationService;

	@Autowired
	private UserService userService;

	@Autowired
	private FlightService flightService;
	
	@Autowired
	private VehicleService vehicleService;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FlightReservationDTO>> addReservations(@RequestBody List<FlightReservationDTO> reservationsDTO) {

		try {
			Flight flight = flightService.findOne(reservationsDTO.get(0).getTicket().getFlight().getId());
			List<Ticket> tickets = flight.getTickets();
			for (FlightReservationDTO r : reservationsDTO) {

				Ticket ticket = null;
				for (Ticket t : tickets) {
					if (t.getId().equals(r.getTicket().getId())) {
						if (t.getIsReserved()) {
							return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
						}
						t.setIsReserved(true);
						t.setType(r.getTicket().getType());
						ticket = t;
						break;
					}
				}

				if (ticket != null) {
					User user = null;
					if (r.getUser() != null) {
						user = userService.findByUsernameUser(r.getUser().getUsername());
					}
					Reservation reservation = new Reservation();
					FlightReservation flightReservation = new FlightReservation(user, ticket, reservation);
					reservationService.save(flightReservation);
				}
			}

			return new ResponseEntity<>(reservationsDTO, HttpStatus.OK);

		} catch (Exception e) {

		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FlightReservationDTO>> getReservations(@PathVariable String username) {
		
		User user = userService.findByUsernameUser(username);
		if (user != null) {

			List<FlightReservation> flightReservations = user.getReservation();

			List<FlightReservationDTO> reservationsDTO = new ArrayList<>();
			for (FlightReservation fr : flightReservations) {
				reservationsDTO.add(new FlightReservationDTO(new UserDTO(fr.getUser()), new TicketDTO(fr.getTicket()), new ReservationDTO(fr.getReservation())));
			}

			return new ResponseEntity<>(reservationsDTO, HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/vehicleReservations", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReservationDTO> createVehicleReservation(@RequestBody VehicleReservationDTO vehicleReservationDTO) 
	{	
		System.out.println(vehicleReservationDTO);
		if (vehicleReservationDTO.getReservationId() == null || vehicleReservationDTO.getReservationStartDate() == null ||
			vehicleReservationDTO.getReservationEndDate() == null || vehicleReservationDTO.getTotalPrice() == null || vehicleReservationDTO.getVehicle() == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
			
		try
		{			
			Reservation reservation = this.reservationService.findOneReservation(vehicleReservationDTO.getReservationId());
			if (reservation != null)
			{
				VehicleReservation vehicleReservation = new VehicleReservation(vehicleReservationDTO.getReservationStartDate(),
																			   vehicleReservationDTO.getReservationEndDate(),
																			   vehicleReservationDTO.getTotalPrice());
				
				Vehicle vehicle = this.vehicleService.findOne(vehicleReservationDTO.getVehicle().getId());
				vehicleReservation.setVehicle(vehicle);
				reservation.setVehicleReservation(vehicleReservation);
				reservation = this.reservationService.saveReservation(reservation);
				return new ResponseEntity<>(new ReservationDTO(reservation), HttpStatus.OK);
			}
			else 
			{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}