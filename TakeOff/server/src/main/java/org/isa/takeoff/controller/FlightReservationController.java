package org.isa.takeoff.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.isa.takeoff.dto.FlightReservationDTO;
import org.isa.takeoff.dto.ReservationDTO;
import org.isa.takeoff.dto.RoomDTO;
import org.isa.takeoff.dto.RoomRatingDTO;
import org.isa.takeoff.dto.RoomReservationDTO;
import org.isa.takeoff.dto.TicketDTO;
import org.isa.takeoff.dto.UserDTO;
import org.isa.takeoff.dto.VehicleReservationDTO;
import org.isa.takeoff.model.AirCompanyRating;
import org.isa.takeoff.model.AirCompanyRatingId;
import org.isa.takeoff.model.Flight;
import org.isa.takeoff.model.FlightRating;
import org.isa.takeoff.model.FlightRatingId;
import org.isa.takeoff.model.FlightReservation;
import org.isa.takeoff.model.Hotel;
import org.isa.takeoff.model.HotelRating;
import org.isa.takeoff.model.HotelRatingId;
import org.isa.takeoff.model.RentACar;
import org.isa.takeoff.model.RentACarRating;
import org.isa.takeoff.model.RentACarRatingId;
import org.isa.takeoff.model.Reservation;
import org.isa.takeoff.model.Room;
import org.isa.takeoff.model.RoomRating;
import org.isa.takeoff.model.RoomRatingId;
import org.isa.takeoff.model.RoomReservation;
import org.isa.takeoff.model.RoomReservationRooms;
import org.isa.takeoff.model.Ticket;
import org.isa.takeoff.model.User;
import org.isa.takeoff.model.Vehicle;
import org.isa.takeoff.model.VehicleRating;
import org.isa.takeoff.model.VehicleRatingId;
import org.isa.takeoff.model.VehicleReservation;
import org.isa.takeoff.service.AirCompanyService;
import org.isa.takeoff.service.EmailService;
import org.isa.takeoff.service.FlightReservationService;
import org.isa.takeoff.service.FlightService;
import org.isa.takeoff.service.HotelService;
import org.isa.takeoff.service.RentACarService;
import org.isa.takeoff.service.RoomReservationService;
import org.isa.takeoff.service.RoomService;
import org.isa.takeoff.service.TicketService;
import org.isa.takeoff.service.UserService;
import org.isa.takeoff.service.VehicleReservationService;
import org.isa.takeoff.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.prepost.PreAuthorize;
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
	private RoomService roomService;

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private AirCompanyService airCompanyService;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private RentACarService rentACarService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private RoomReservationService roomReservationService;
	
	@Autowired
	private VehicleReservationService vehicleReservationService;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FlightReservationDTO>> addReservations(
			@RequestBody List<FlightReservationDTO> reservationsDTO) {

		try {
			Flight flight = flightService.findOne(reservationsDTO.get(0).getTicket().getFlight().getId());
			List<Ticket> tickets = flight.getTickets();
			Reservation reservation = new Reservation();

			int index = 0;
			for (FlightReservationDTO r : reservationsDTO) {

				Ticket ticket = null;
				for (Ticket t : tickets) {
					if (t.getId().equals(r.getTicket().getId())) {
						ticket = new Ticket(r.getTicket());
						ticket.setFlight(t.getFlight());
						t.setIsReserved(true);
						t.setType(r.getTicket().getType());
						break;
					}
				}

				if (ticket != null) {
					User user = null;
					if (r.getUser() != null) {
						user = userService.findByUsernameUser(r.getUser().getUsername());
					}
					if(r.getReservationDTO().getRoomReservation() != null){
						RoomReservation roomReservation = new RoomReservation();
						roomReservation.setPrice(r.getReservationDTO().getRoomReservation().getTotalPrice());
						roomReservation.setReservationEndDate(
								r.getReservationDTO().getRoomReservation().getReservationEndDate());
						roomReservation.setReservationStartDate(
								r.getReservationDTO().getRoomReservation().getReservationStartDate());
						List<RoomReservationRooms> rrrs = new ArrayList<>();
						RoomReservationRooms rrr = new RoomReservationRooms();
						Room room = roomService.findOne(r.getReservationDTO().getRoomReservation().getRoomsAndRatings()
								.get(0).getRoom().getId());
						rrr.setRoom(room);
						rrr.setRoomReservation(roomReservation);
						rrrs.add(rrr);
						roomReservation.setRooms(rrrs);
						reservation.setRoomReservation(roomReservation);
					}
					if(r.getReservationDTO().getVehicleReservation() != null){
						VehicleReservation vehicleReservation = new VehicleReservation();
						vehicleReservation.setPrice(r.getReservationDTO().getVehicleReservation().getTotalPrice());
						vehicleReservation.setReservationEndDate(
								r.getReservationDTO().getVehicleReservation().getReservationEndDate());
						vehicleReservation.setReservationStartDate(
								r.getReservationDTO().getVehicleReservation().getReservationStartDate());
						Vehicle vehicle = vehicleService
								.findOne(r.getReservationDTO().getVehicleReservation().getVehicle().getId());
						vehicleReservation.setVehicle(vehicle);
						reservation.setVehicleReservation(vehicleReservation);
					}
					ticket = ticketService.save(ticket);
					FlightReservation flightReservation = new FlightReservation(user, ticket, reservation);
					reservationService.save(flightReservation);

					// send mail only to the user who created reservation
					if (index == 0) {
						try {
							Double total = 0.0;
							if (ticket.getType().equals("first-class")) {
								total += flight.getTicketPrice() * 2;
							}else if(ticket.getType().equals("business")) {
								total += flight.getTicketPrice() * 1.5;
							}else {
								total += flight.getTicketPrice();
							}
							
							Hotel hotel = null;
							if (reservation.getRoomReservation() != null) {
								hotel = reservation.getRoomReservation().getRooms().get(0).getRoom().getHotel();
								total += reservation.getRoomReservation().getPrice();
							}
							Vehicle vehicle = null;
							if (reservation.getVehicleReservation() != null) {
								vehicle = reservation.getVehicleReservation().getVehicle();
								total += reservation.getVehicleReservation().getPrice();
							}

							this.emailService.sendReservationMail(user, flight, hotel, reservation.getRoomReservation(), vehicle, reservation.getVehicleReservation(), total);

						} catch (MailException | MessagingException e) {
						}
					}
				}

				index++;
			}

			return new ResponseEntity<>(reservationsDTO, HttpStatus.OK);

		} catch (Exception e) {
//			e.printStackTrace();
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FlightReservationDTO>> getReservations(@PathVariable String username) {

		User user = userService.findByUsernameUser(username);
		if (user != null) {

			List<FlightReservation> flightReservations = user.getReservations();

			List<FlightReservationDTO> reservationsDTO = new ArrayList<>();
			for (FlightReservation fr : flightReservations) {
				FlightReservationDTO flightReservationDTO = new FlightReservationDTO();
				AirCompanyRating airCompanyRating = this.airCompanyService
						.findOneRating(new AirCompanyRatingId(fr.getTicket().getFlight().getCompany(), user));
				if (airCompanyRating != null) {
					flightReservationDTO.setAircompanyRating(airCompanyRating.getRating());
				}
				FlightRating flightRating = this.flightService
						.findOneRating(new FlightRatingId(fr.getTicket().getFlight(), user));
				if (flightRating != null) {
					flightReservationDTO.setFlightRating(flightRating.getRating());
				}
				ReservationDTO reservationDTO = new ReservationDTO(fr.getReservation());
				if (fr.getReservation().getRoomReservation() != null) {
					RoomReservationDTO roomReservationDTO = new RoomReservationDTO(
							fr.getReservation().getRoomReservation());
					HotelRating hotelRating = this.hotelService.findOneRating(new HotelRatingId(
							fr.getReservation().getRoomReservation().getRooms().get(0).getRoom().getHotel(), user));
					if (hotelRating != null) {
						roomReservationDTO.setHotelRating(hotelRating.getRating());
					}
					List<RoomRatingDTO> roomRatingDTOs = new ArrayList<>();
					for (RoomReservationRooms room : fr.getReservation().getRoomReservation().getRooms()) {
						RoomRating roomRating = this.roomService.findOneRating(new RoomRatingId(room.getRoom(), user));
						RoomRatingDTO roomRatingDTO = new RoomRatingDTO();
						roomRatingDTO.setRoom(new RoomDTO(room.getRoom()));
						if (roomRating != null) {
							roomRatingDTO.setRating(roomRating.getRating());
						}
						roomRatingDTOs.add(roomRatingDTO);
					}
					roomReservationDTO.setRoomsAndRatings(roomRatingDTOs);
					reservationDTO.setRoomReservation(roomReservationDTO);
				}

				if (fr.getReservation().getVehicleReservation() != null) {
					VehicleReservationDTO vehicleReservationDTO = new VehicleReservationDTO(
							fr.getReservation().getVehicleReservation());
					RentACarRating rentACarRating = this.rentACarService.findOneRating(new RentACarRatingId(
							fr.getReservation().getVehicleReservation().getVehicle().getRentACar(), user));
					if (rentACarRating != null) {
						vehicleReservationDTO.setRentACarRating(rentACarRating.getRating());
					}
					VehicleRating vehicleRating = this.vehicleService.findOneRating(
							new VehicleRatingId(fr.getReservation().getVehicleReservation().getVehicle(), user));
					if (vehicleRating != null) {
						vehicleReservationDTO.setVehicleRating(vehicleRating.getRating());
					}
					reservationDTO.setVehicleReservation(vehicleReservationDTO);
				}
				flightReservationDTO.setUser(new UserDTO(fr.getUser()));
				flightReservationDTO.setTicket(new TicketDTO(fr.getTicket()));
				flightReservationDTO.setReservationDTO(reservationDTO);
				reservationsDTO.add(flightReservationDTO);
			}

			return new ResponseEntity<>(reservationsDTO, HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/vehicleReservations", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReservationDTO> createVehicleReservation(
			@RequestBody VehicleReservationDTO vehicleReservationDTO) {
		if (vehicleReservationDTO.getReservationId() == null || vehicleReservationDTO.getReservationStartDate() == null
				|| vehicleReservationDTO.getReservationEndDate() == null
				|| vehicleReservationDTO.getTotalPrice() == null || vehicleReservationDTO.getVehicle() == null) {
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
				
				Vehicle vehicle = new Vehicle(vehicleReservationDTO.getVehicle());
				vehicle.setId(vehicleReservationDTO.getVehicle().getId());
				vehicle.setVersion(vehicleReservationDTO.getVehicle().getVersion());
				vehicle.setReserved(true);
				
				RentACar rentACar = this.rentACarService.findOne(vehicleReservationDTO.getVehicle().getRentACar().getId());
				vehicle.setRentACar(rentACar);
				
				//Optimistic lock exception is expected when two users try to make a reservation of the same vehicle in the same time
				try
				{
					vehicle = this.vehicleService.save(vehicle);					
				}
				catch(Exception e)
				{
					return new ResponseEntity<>(HttpStatus.CONFLICT);
				}
				
				vehicleReservation.setVehicle(vehicle);
				reservation.setVehicleReservation(vehicleReservation);
				reservation = this.reservationService.saveReservation(reservation);
				return new ResponseEntity<>(new ReservationDTO(reservation), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/roomReservations", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReservationDTO> createRoomReservation(@RequestBody RoomReservationDTO roomReservationDTO) {
		if (roomReservationDTO.getReservationId() == null || roomReservationDTO.getReservationStartDate() == null
				|| roomReservationDTO.getReservationEndDate() == null || roomReservationDTO.getTotalPrice() == null
				|| roomReservationDTO.getRoomsAndRatings() == null) {

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		try {
			Reservation reservation = this.reservationService.findOneReservation(roomReservationDTO.getReservationId());
			if (reservation != null) {
				RoomReservation roomReservation = new RoomReservation();
				roomReservation.setReservationStartDate(roomReservationDTO.getReservationStartDate());
				roomReservation.setReservationEndDate(roomReservationDTO.getReservationEndDate());
				roomReservation.setPrice(roomReservationDTO.getTotalPrice());

				List<RoomReservationRooms> rrrs = new ArrayList<>();
				List<Long> ids = new ArrayList<>();
				for (RoomRatingDTO roomRating : roomReservationDTO.getRoomsAndRatings()) {
					Room room = roomService.findOne(roomRating.getRoom().getId());
					RoomReservationRooms rrr = new RoomReservationRooms();
					rrr.setRoomReservation(roomReservation);
					if (!ids.contains(roomRating.getRoom().getId())) {
						Room newRoom = new Room(roomRating.getRoom());
						newRoom.setId(roomRating.getRoom().getId());
						newRoom.setVersion(roomRating.getRoom().getVersion());
						newRoom.setIsReserved(true);
						Hotel hotel = hotelService.findOne(roomRating.getRoom().getHotel().getId());
						newRoom.setHotel(hotel);
						try {
							room = this.roomService.save(newRoom);
						} catch (Exception e) {
							return new ResponseEntity<>(HttpStatus.CONFLICT);
						}
					}
					ids.add(roomRating.getRoom().getId());
					rrr.setRoom(room);
					rrrs.add(rrr);
				}
				roomReservation.setRooms(rrrs);
				reservation.setRoomReservation(roomReservation);
				reservation = this.reservationService.saveReservation(reservation);
				return new ResponseEntity<>(new ReservationDTO(reservation), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/getNumberOfUsers/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> numberOfUsersInReservation(@PathVariable Long id) {
		if (id != null) {
			Reservation r = reservationService.findOneReservation(id);
			List<FlightReservation> frs = r.getFlightReservations();
			return new ResponseEntity<>(frs.size(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/cancelFlightReservation", method = RequestMethod.PUT)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> cancelFlightReservation(@RequestBody ReservationDTO reservationDTO) 
	{
		Reservation reservation = this.reservationService.findOneReservation(reservationDTO.getId());
		List<FlightReservation> flightReservations = reservation.getFlightReservations();
		if (LocalDateTime.now().isAfter(flightReservations.get(0).getTicket().getFlight().getTakeOffDate().minusHours(3)))
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		try 
		{
			for (FlightReservation flightReservation : flightReservations)
			{
				Ticket ticket = this.ticketService.findOne(flightReservation.getTicket().getId());
				ticket.setIsReserved(false);
				this.ticketService.save(ticket);				
			}
			this.reservationService.deleteReservation(reservationDTO.getId());
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/cancelVehicleReservation", method = RequestMethod.PUT)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> cancelVehicleReservation(@RequestBody ReservationDTO reservationDTO) 
	{
		if (reservationDTO.getVehicleReservation() == null || LocalDate.now().isAfter(reservationDTO.getVehicleReservation().getReservationStartDate().minusDays(2)))
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		try 
		{
			Reservation reservation = this.reservationService.findOneReservation(reservationDTO.getId());
			reservation.setVehicleReservation(null);
			this.reservationService.saveReservation(reservation);
			this.vehicleReservationService.delete(reservationDTO.getVehicleReservation().getId());
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/cancelRoomReservation", method = RequestMethod.PUT)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> cancelRoomReservation(@RequestBody ReservationDTO reservationDTO) 
	{
		if (reservationDTO.getRoomReservation() == null || LocalDate.now().isAfter(reservationDTO.getRoomReservation().getReservationStartDate().minusDays(2)))
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		try 
		{
			Reservation reservation = this.reservationService.findOneReservation(reservationDTO.getId());
			reservation.setRoomReservation(null);
			this.reservationService.saveReservation(reservation);
			this.roomReservationService.delete(reservationDTO.getRoomReservation().getId());
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}