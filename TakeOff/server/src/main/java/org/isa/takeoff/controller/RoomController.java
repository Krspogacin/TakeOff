package org.isa.takeoff.controller;

import java.util.ArrayList;
import java.util.List;

import org.isa.takeoff.dto.RoomDTO;
import org.isa.takeoff.dto.RoomPriceDTO;
import org.isa.takeoff.dto.UserRatingRoomDTO;
import org.isa.takeoff.model.Hotel;
import org.isa.takeoff.model.Room;
import org.isa.takeoff.model.RoomPrice;
import org.isa.takeoff.model.RoomRating;
import org.isa.takeoff.model.RoomRatingId;
import org.isa.takeoff.model.User;
import org.isa.takeoff.service.HotelService;
import org.isa.takeoff.service.RoomService;
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
@RequestMapping(value = "/rooms")
public class RoomController {

	@Autowired
	private HotelService hotelService;
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<RoomDTO> getRoom(@PathVariable Long id){
		try {
			Room room = roomService.findOne(id);
			return new ResponseEntity<>(new RoomDTO(room), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HOTEL_ADMIN')")
	public ResponseEntity<RoomDTO> addRoom(@RequestBody RoomDTO roomDTO) 
	{
		if (roomDTO.getHotel() == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try 
		{
			Hotel hotel = this.hotelService.findOne(roomDTO.getHotel().getId());
			Room room = new Room(roomDTO);
			room.setHotel(hotel);
			room = roomService.save(room);
			return new ResponseEntity<>(new RoomDTO(room), HttpStatus.OK);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HOTEL_ADMIN')")
	public ResponseEntity<RoomDTO> updateRoom(@RequestBody RoomDTO roomDTO) 
	{
		try 
		{
			Room room = this.roomService.findOne(roomDTO.getId());
			room.setDefaultPrice(roomDTO.getDefaultPrice());
			room.setDiscount(roomDTO.getDiscount());
			room.setIsReserved(roomDTO.isReserved());
			room.setFloor(roomDTO.getFloor());
			room.setNumberOfBeds(roomDTO.getNumberOfBeds());
			room.setType(roomDTO.getType());
			room.setHasBalcony(roomDTO.isHasBalcony());
			room.setHasAirCondition(roomDTO.isHasAirCondition());
			room.setNumberOfRooms(roomDTO.getNumberOfRooms());
			room = roomService.save(room);
			return new ResponseEntity<>(new RoomDTO(room), HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_HOTEL_ADMIN')")
	public ResponseEntity<?> deleteRoom(@PathVariable Long id) 
	{
		try 
		{
			this.roomService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/roomPrices", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RoomPriceDTO>> getRoomPrices() {

		List<RoomPrice> roomPrices = roomService.findAllRoomPrices();

		List<RoomPriceDTO> roomPriceDTO = new ArrayList<>();
		for (RoomPrice roomPrice : roomPrices) {
			roomPriceDTO.add(new RoomPriceDTO(roomPrice));
		}

		return new ResponseEntity<>(roomPriceDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/addRoomPrice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HOTEL_ADMIN')")
	public ResponseEntity<List<RoomPriceDTO>> addRoomPrice(@RequestBody List<RoomPriceDTO> roomPricesDTO){
		for(RoomPriceDTO roomPriceDTO: roomPricesDTO){
			if (roomPriceDTO.getRoom() == null)
			{
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		try 
		{
			List<RoomPriceDTO> roomPrices = new ArrayList<>();
			for(RoomPriceDTO roomPriceDTO: roomPricesDTO){
				Room room = this.roomService.findOne(roomPriceDTO.getRoom().getId());
				RoomPrice roomPrice = new RoomPrice();
				roomPrice.setPeriod(roomPriceDTO.getPeriod());
				roomPrice.setPrice(roomPriceDTO.getPrice());
				roomPrice.setRoom(room);
				roomPrice = roomService.save(roomPrice);
				roomPrices.add(new RoomPriceDTO(roomPrice));
			}
			return new ResponseEntity<>(roomPrices, HttpStatus.OK);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/updateRoomPrices", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HOTEL_ADMIN')")
	public ResponseEntity<List<RoomPriceDTO>> updateRoomPrices(@RequestBody List<RoomPriceDTO> roomPrices){
		try 
		{
			List<RoomPriceDTO> roomPricesDTO = new ArrayList<>();
			for(RoomPriceDTO roomPriceDTO: roomPrices){
				RoomPrice roomPrice = this.roomService.findOneRoomPrice(roomPriceDTO.getId());
				roomPrice.setPeriod(roomPriceDTO.getPeriod());
				roomPrice.setPrice(roomPriceDTO.getPrice());
				roomPrice = roomService.save(roomPrice);
				roomPricesDTO.add(new RoomPriceDTO(roomPrice));
			}
			return new ResponseEntity<>(roomPricesDTO,HttpStatus.OK);
			
		}catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value="/rateRoom", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> rateRoom(@RequestBody UserRatingRoomDTO userRatingRoomDTO) 
	{
		if (userRatingRoomDTO.getRoom() == null || userRatingRoomDTO.getRating() == null || userRatingRoomDTO.getUsername() == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		User user = null;
		try
		{
			user = this.userService.findByUsernameUser(userRatingRoomDTO.getUsername());
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		try
		{
			Room room = this.roomService.findOne(userRatingRoomDTO.getRoom().getId());
			RoomRating roomRating = this.roomService.findOneRating(new RoomRatingId(room, user));
			if (roomRating == null )
			{				
				roomRating = new RoomRating();
				roomRating.setId(new RoomRatingId(room, user));
			}
			roomRating.setRating(userRatingRoomDTO.getRating());
			this.roomService.saveRating(roomRating);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
