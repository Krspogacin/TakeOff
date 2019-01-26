package org.isa.takeoff.controller;

import java.util.ArrayList;
import java.util.List;

import org.isa.takeoff.dto.HotelDTO;
import org.isa.takeoff.model.Hotel;
import org.isa.takeoff.service.HotelService;
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
@RequestMapping(value = "/hotels")
public class HotelController {

	@Autowired
	private HotelService hotelService;

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<HotelDTO>> getHotels() {

		List<Hotel> hotels = hotelService.findAll();

		// convert hotels to DTOs
		List<HotelDTO> hotelsDTO = new ArrayList<>();
		for (Hotel hotel : hotels) {
			hotelsDTO.add(new HotelDTO(hotel));
		}

		return new ResponseEntity<>(hotelsDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HotelDTO> getHotel(@PathVariable Long id) {

		try {
			Hotel hotel = hotelService.findOne(id);
			return new ResponseEntity<>(new HotelDTO(hotel), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HotelDTO> addHotel(@RequestBody HotelDTO hotelDTO) {

		Hotel hotel = new Hotel();
		hotel.setName(hotelDTO.getName());
		hotel.setAddress(hotelDTO.getAddress());
		hotel.setDescription(hotelDTO.getDescription());

		hotel = hotelService.save(hotel);
		return new ResponseEntity<>(new HotelDTO(hotel), HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HotelDTO> updateHotel(@RequestBody HotelDTO hotelDTO) {

		Hotel hotel = hotelService.findOne(hotelDTO.getId());

		if (hotel == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		hotel.setName(hotelDTO.getName());
		hotel.setAddress(hotelDTO.getAddress());
		hotel.setDescription(hotelDTO.getDescription());

		hotel = hotelService.save(hotel);
		return new ResponseEntity<>(new HotelDTO(hotel), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteHotel(@RequestBody HotelDTO hotelDTO) {
		
		Hotel hotel = hotelService.findOne(hotelDTO.getId());
		
		if(hotel == null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		hotelService.delete(hotelDTO.getId());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
