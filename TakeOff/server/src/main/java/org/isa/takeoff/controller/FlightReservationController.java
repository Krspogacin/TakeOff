package org.isa.takeoff.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.isa.takeoff.model.HotelRating;
import org.isa.takeoff.model.HotelRatingId;
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
import org.isa.takeoff.service.FlightReservationService;
import org.isa.takeoff.service.FlightService;
import org.isa.takeoff.service.HotelService;
import org.isa.takeoff.service.RentACarService;
import org.isa.takeoff.service.RoomService;
import org.isa.takeoff.service.TicketService;
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
	

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FlightReservationDTO>> addReservations(@RequestBody List<FlightReservationDTO> reservationsDTO) {

		try {
			Flight flight = flightService.findOne(reservationsDTO.get(0).getTicket().getFlight().getId());
			List<Ticket> tickets = flight.getTickets();
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
					Reservation reservation = new Reservation();
					if(r.getReservationDTO().getRoomReservation().getRoomsAndRatings() != null){
						RoomReservation roomReservation = new RoomReservation();
						roomReservation.setPrice(r.getReservationDTO().getRoomReservation().getTotalPrice());
						roomReservation.setReservationEndDate(r.getReservationDTO().getRoomReservation().getReservationEndDate());
						roomReservation.setReservationStartDate(r.getReservationDTO().getRoomReservation().getReservationStartDate());
						List<RoomReservationRooms> rrrs = new ArrayList<>();
						RoomReservationRooms rrr = new RoomReservationRooms();
						Room room = roomService.findOne(r.getReservationDTO().getRoomReservation().getRoomsAndRatings().get(0).getRoom().getId());
						rrr.setRoom(room);
						rrr.setRoomReservation(roomReservation);
						rrrs.add(rrr);
						roomReservation.setRooms(rrrs);
						reservation.setRoomReservation(roomReservation);
					}
					if(r.getReservationDTO().getVehicleReservation().getVehicle() != null){
						VehicleReservation vehicleReservation = new VehicleReservation();
						vehicleReservation.setPrice(r.getReservationDTO().getVehicleReservation().getTotalPrice());
						vehicleReservation.setReservationEndDate(r.getReservationDTO().getVehicleReservation().getReservationEndDate());
						vehicleReservation.setReservationStartDate(r.getReservationDTO().getVehicleReservation().getReservationStartDate());
						Vehicle vehicle = vehicleService.findOne(r.getReservationDTO().getVehicleReservation().getVehicle().getId());
						vehicleReservation.setVehicle(vehicle);
						reservation.setVehicleReservation(vehicleReservation);
					}
					ticket = ticketService.save(ticket);
					FlightReservation flightReservation = new FlightReservation(user, ticket, reservation);
					reservationService.save(flightReservation);
				}
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
				AirCompanyRating airCompanyRating = this.airCompanyService.findOneRating(new AirCompanyRatingId(fr.getTicket().getFlight().getCompany(), user));
				if (airCompanyRating != null) {
					flightReservationDTO.setAircompanyRating(airCompanyRating.getRating());
				}
				FlightRating flightRating = this.flightService.findOneRating(new FlightRatingId(fr.getTicket().getFlight(), user));
				if (flightRating != null ) {
					flightReservationDTO.setFlightRating(flightRating.getRating());
				}
				ReservationDTO reservationDTO = new ReservationDTO(fr.getReservation());
				if (fr.getReservation().getRoomReservation() != null) {
					RoomReservationDTO roomReservationDTO = new RoomReservationDTO(fr.getReservation().getRoomReservation());
					HotelRating hotelRating = this.hotelService.findOneRating(
													new HotelRatingId(fr.getReservation().getRoomReservation().getRooms().get(0).getRoom().getHotel(), user));
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
					VehicleReservationDTO vehicleReservationDTO = new VehicleReservationDTO(fr.getReservation().getVehicleReservation());
					RentACarRating rentACarRating = this.rentACarService.findOneRating(
														new RentACarRatingId(fr.getReservation().getVehicleReservation().getVehicle().getRentACar(), user));
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
	public ResponseEntity<ReservationDTO> createVehicleReservation(@RequestBody VehicleReservationDTO vehicleReservationDTO) 
	{	
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
	
	@RequestMapping(value = "/roomReservations", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReservationDTO> createRoomReservation(@RequestBody RoomReservationDTO roomReservationDTO){
		if (roomReservationDTO.getReservationId() == null || roomReservationDTO.getReservationStartDate() == null ||
			roomReservationDTO.getReservationEndDate() == null || roomReservationDTO.getTotalPrice() == null ||  roomReservationDTO.getRoomsAndRatings() == null){
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		try{
			Reservation reservation = this.reservationService.findOneReservation(roomReservationDTO.getReservationId());
			if(reservation != null){
				RoomReservation roomReservation = new RoomReservation();
				roomReservation.setReservationStartDate(roomReservationDTO.getReservationStartDate());
				roomReservation.setReservationEndDate(roomReservationDTO.getReservationEndDate());
				roomReservation.setPrice(roomReservationDTO.getTotalPrice());
				
				List<RoomReservationRooms> rrrs = new ArrayList<>();
				for(RoomRatingDTO roomRating: roomReservationDTO.getRoomsAndRatings()){
					Room room = roomService.findOne(roomRating.getRoom().getId());
					RoomReservationRooms rrr = new RoomReservationRooms();
					rrr.setRoomReservation(roomReservation);
					rrr.setRoom(room);
					rrrs.add(rrr);
				}
				roomReservation.setRooms(rrrs);
				reservation.setRoomReservation(roomReservation);
				reservation = this.reservationService.saveReservation(reservation);
				return new ResponseEntity<>(new ReservationDTO(reservation),HttpStatus.OK);
			}else{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}