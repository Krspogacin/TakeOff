package org.isa.takeoff.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.isa.takeoff.dto.AvailableRoomsDTO;
import org.isa.takeoff.dto.ChartDataDTO;
import org.isa.takeoff.dto.HotelDTO;
import org.isa.takeoff.dto.HotelRatingDTO;
import org.isa.takeoff.dto.RoomDTO;
import org.isa.takeoff.dto.RoomOnDiscountDTO;
import org.isa.takeoff.dto.RoomRatingDTO;
import org.isa.takeoff.dto.RoomSearchDTO;
import org.isa.takeoff.dto.ServiceDTO;
import org.isa.takeoff.dto.ServiceHotelDTO;
import org.isa.takeoff.dto.UserRatingHotelDTO;
import org.isa.takeoff.model.Hotel;
import org.isa.takeoff.model.HotelRating;
import org.isa.takeoff.model.HotelRatingId;
import org.isa.takeoff.model.Location;
import org.isa.takeoff.model.Room;
import org.isa.takeoff.model.RoomPrice;
import org.isa.takeoff.model.RoomRating;
import org.isa.takeoff.model.RoomReservation;
import org.isa.takeoff.model.RoomReservationRooms;
import org.isa.takeoff.model.Service;
import org.isa.takeoff.model.ServiceHotel;
import org.isa.takeoff.model.User;
import org.isa.takeoff.service.HotelService;
import org.isa.takeoff.service.LocationService;
import org.isa.takeoff.service.RoomReservationService;
import org.isa.takeoff.service.ServiceService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/hotels")
public class HotelController {

	@Autowired
	private HotelService hotelService;
	
	@Autowired
	private LocationService locationService;
	
	@Autowired 
	private ServiceService serviceService;
	
	@Autowired 
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoomReservationService roomReservationService;

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
	@PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
	public ResponseEntity<HotelDTO> addHotel(@RequestBody HotelDTO hotelDTO) {

		if(hotelDTO.getLocation() == null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Hotel hotel = new Hotel(hotelDTO);
		Location location = this.locationService.findOneByLatitudeAndLongitude(hotelDTO.getLocation().getLatitude(), hotelDTO.getLocation().getLongitude());

		if (location == null) 
		{
			location = new Location(hotelDTO.getLocation());
			location = this.locationService.save(location);
		}
		
		hotel.setLocation(location);
		hotel = hotelService.save(hotel);
		return new ResponseEntity<>(new HotelDTO(hotel), HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HOTEL_ADMIN')")
	public ResponseEntity<HotelDTO> updateHotel(@RequestBody HotelDTO hotelDTO) {
		
		if (hotelDTO.getLocation() == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		try {
			Hotel hotel = hotelService.findOne(hotelDTO.getId());
			
			if (hotelDTO.getLocation().getId() == null){
				Location location = this.locationService.findOneByLatitudeAndLongitude(hotelDTO.getLocation().getLatitude(),hotelDTO.getLocation().getLongitude());
				
				if (location == null) {
					location = new Location(hotelDTO.getLocation());
					location = this.locationService.save(location);
				}
				hotel.setLocation(location);
			}
			hotel.setName(hotelDTO.getName());
			hotel.setDescription(hotelDTO.getDescription());
			
			hotel = hotelService.save(hotel);
			return new ResponseEntity<>(new HotelDTO(hotel), HttpStatus.OK);
			
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HOTEL_ADMIN')")
	public ResponseEntity<Void> deleteHotel(@RequestBody HotelDTO hotelDTO) {
		
		Hotel hotel = hotelService.findOne(hotelDTO.getId());
		
		if(hotel == null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		hotelService.delete(hotelDTO.getId());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/rooms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RoomDTO>> getRooms(@PathVariable Long id){
		try{
			Hotel hotel = hotelService.findOne(id);
			List<Room> rooms = hotel.getRooms();
			
			List<RoomDTO> roomsDTO = new ArrayList<>();
			for (Room room : rooms){
				roomsDTO.add(new RoomDTO(room));
			}
			return new ResponseEntity<>(roomsDTO, HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{id}/getHotelRating", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Double> getHotelRating(@PathVariable Long id) 
	{
		try 
		{
			Hotel hotel = hotelService.findOne(id);
			List<HotelRating> ratings = hotel.getHotelRatings();
			
			if (ratings.isEmpty())
			{
				return new ResponseEntity<>(new Double(0), HttpStatus.OK);
			}
			
			Double sum = 0.0;
			for(HotelRating hr: ratings){
				sum += hr.getRating();
			}
			return new ResponseEntity<>(sum/ratings.size(), HttpStatus.OK);
		}
		catch (Exception e) 
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{id}/getRoomRatings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RoomRatingDTO>> getRoomRatings(@PathVariable Long id) 
	{
		try
		{
			Hotel hotel = hotelService.findOne(id);
			List<Room> rooms = hotel.getRooms();
			List<RoomRatingDTO> allRatings = new ArrayList<>();
			for (Room room : rooms)
			{
				RoomRatingDTO roomRating = new RoomRatingDTO();
				List<RoomRating> ratings = room.getRoomRatings();
				roomRating.setRoom(new RoomDTO(room));
				
				if (ratings.isEmpty())
				{
					roomRating.setRating(0.0);
					allRatings.add(roomRating);
					continue;
				}
				
				Double sum = 0.0;
				for(RoomRating hr: ratings){
					sum += hr.getRating();
				}
				roomRating.setRating(sum / ratings.size());
				allRatings.add(roomRating);
			}
			return new ResponseEntity<>(allRatings, HttpStatus.OK);			
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/getHotelRatings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<HotelRatingDTO>> getHotelRatings() 
	{
		try
		{
			List<Hotel> hotels = hotelService.findAll();
			List<HotelRatingDTO> allRatings = new ArrayList<>();
			for (Hotel hotel: hotels)
			{
				HotelRatingDTO hotelRating = new HotelRatingDTO();
				List<HotelRating> ratings = hotel.getHotelRatings();
				hotelRating.setHotel(new HotelDTO(hotel));
				
				if (ratings.isEmpty())
				{
					hotelRating.setRating(0.0);
					allRatings.add(hotelRating);
					continue;
				}
				
				Double sum = 0.0;
				for(HotelRating hr: ratings){
					sum += hr.getRating();
				}
				hotelRating.setRating(sum / ratings.size());
				allRatings.add(hotelRating);
			}
			return new ResponseEntity<>(allRatings, HttpStatus.OK);			
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/allServices", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ServiceDTO>> getServices() {

		List<Service> services = serviceService.findAll();

		// convert hotels to DTOs
		List<ServiceDTO> servicesDTO = new ArrayList<>();
		for (Service service : services) {
			servicesDTO.add(new ServiceDTO(service));
		}

		return new ResponseEntity<>(servicesDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value="/saveHotelServices", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HOTEL_ADMIN')")
	public ResponseEntity<List<ServiceHotelDTO>> saveHotelServices(@RequestBody List<ServiceHotelDTO> hotelServicesDTO){
		
		List<ServiceHotelDTO> hotelServicesDTO2 = new ArrayList<>();
		if(hotelServicesDTO.size() == 0){
			return new ResponseEntity<>(hotelServicesDTO2, HttpStatus.OK);
		}
		
		Hotel hotel = hotelService.findOne(hotelServicesDTO.get(0).getHotel().getId());
		List<ServiceHotel> allServices = serviceService.findAllHotel();
		List<ServiceHotel> hotelServices = new ArrayList<>();
		
		for(ServiceHotel service : allServices){
			if(service.getHotel().getId() == hotel.getId()){
				hotelServices.add(service);
			}
		}
		
		if(!hotelServices.isEmpty()){
			for(ServiceHotel service : hotelServices){
				serviceService.delete(service.getId());
			}
		}
		
		for(ServiceHotelDTO hotelServiceDTO : hotelServicesDTO){
			ServiceHotel hotelService = new ServiceHotel(hotelServiceDTO.getName(), hotelServiceDTO.getPrice(), hotel);
			ServiceHotel sh = serviceService.save(hotelService);
			hotelServicesDTO2.add(new ServiceHotelDTO(sh));
		}
		
		return new ResponseEntity<>(hotelServicesDTO2, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/getHotelServices", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ServiceHotelDTO>> getHotelServices(@PathVariable Long id) {

		try 
		{
			Hotel hotel = hotelService.findOne(id);
			List<ServiceHotel> services = hotel.getServices();
			List<ServiceHotelDTO> servicesDTO = new ArrayList<>();
			
			if (services.isEmpty())
			{
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
			}
			
			for (ServiceHotel service: services){
				servicesDTO.add(new ServiceHotelDTO(service));
			}
			
			return new ResponseEntity<>(servicesDTO, HttpStatus.OK);
		}
		catch (Exception e) 
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/getAvailableRooms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<AvailableRoomsDTO>> getAvailableRooms(@RequestParam String parameters) {
		
		RoomSearchDTO roomSearch = new RoomSearchDTO();
		try {
			roomSearch = objectMapper.readValue(parameters, RoomSearchDTO.class);
			Hotel hotel = hotelService.findOne(roomSearch.getHotelId());
			List<AvailableRoomsDTO> roomsDTO = new ArrayList<>();
			List<Room> rooms = hotel.getRooms();
			int counter = 0;
			boolean flag = false;
			int allRooms = 0;
			for (Room room : rooms){
				flag = false;
				counter = 0;
				if(room.getDiscount() != null && room.getDiscount() > 0){
					continue;
				}
				
				List<RoomReservationRooms> reservations = room.getRoomReservations();
				LocalDate endingDate = roomSearch.getCheckIn().plusDays(roomSearch.getNumberOfDays());
				for(RoomReservationRooms reservation: reservations){
					if((roomSearch.getCheckIn().isBefore(reservation.getRoomReservation().getReservationEndDate()) && roomSearch.getCheckIn().isAfter(reservation.getRoomReservation().getReservationStartDate())) ||
					   (endingDate.isBefore(reservation.getRoomReservation().getReservationEndDate()) && endingDate.isAfter(reservation.getRoomReservation().getReservationStartDate()))){
						counter++;
					}
				}
				int availableRooms = room.getNumberOfRooms() - counter; 
				if (availableRooms <= 0){
					continue;
				}else{
					allRooms += availableRooms;
				}
				for(int numberOfBeds : roomSearch.getNumberOfBeds()){
					if(numberOfBeds == room.getNumberOfBeds()){
						flag = true;
					}
				}
				if(!flag){
					continue;
				}
				List<RoomPrice> prices = room.getRoomPrices();
				double price = 0;
				for(RoomPrice roomPrice: prices){
					if(roomPrice.getPeriod().equals("5") && roomSearch.getNumberOfDays() < 5){
						price = roomPrice.getPrice();
						break;
					}else if(roomPrice.getPeriod().equals("10") && roomSearch.getNumberOfDays() >= 5 && roomSearch.getNumberOfDays() < 10){
						price = roomPrice.getPrice();
						break;
					}else if(roomPrice.getPeriod().equals("20") && roomSearch.getNumberOfDays() >= 10 && roomSearch.getNumberOfDays() < 20){
						price = roomPrice.getPrice();
						break;
					}else if(roomPrice.getPeriod().equals("MoreThan20") && roomSearch.getNumberOfDays() >= 20){
						price = roomPrice.getPrice();
						break;
					}
				}
				
				double totalPrice = price*roomSearch.getNumberOfDays();
				if(roomSearch.getPriceMax() != null && roomSearch.getPriceMin() != null){
					if (totalPrice*roomSearch.getNumberOfRooms() > roomSearch.getPriceMax() || totalPrice*roomSearch.getNumberOfRooms() < roomSearch.getPriceMin()){
						continue;
					}
				}else if(roomSearch.getPriceMax() != null){
					if (totalPrice*roomSearch.getNumberOfRooms() > roomSearch.getPriceMax()){
						continue;
					}
				}else if(roomSearch.getPriceMin() != null){
					if (totalPrice*roomSearch.getNumberOfRooms() < roomSearch.getPriceMin()){
						continue;
					}
				}
				
				Double sum = 0.0;
				for(RoomRating rr: room.getRoomRatings()){
					sum += rr.getRating();
				}
				Double rating = 0.0;
				if(sum != 0.0){
					rating = sum/room.getRoomRatings().size();
				}
				roomsDTO.add(new AvailableRoomsDTO(new RoomDTO(room), totalPrice, rating, availableRooms, endingDate));
			}
			if(allRooms < roomSearch.getNumberOfRooms()){
				roomsDTO = new ArrayList<>();
				return new ResponseEntity<>(roomsDTO,HttpStatus.OK);
			}
			return new ResponseEntity<>(roomsDTO,HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/roomsOnDiscount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<AvailableRoomsDTO>> roomsOnDiscount(@RequestParam String parameters) 
	{
		RoomOnDiscountDTO roomOnDiscount = new RoomOnDiscountDTO();
		try {
			roomOnDiscount = objectMapper.readValue(parameters, RoomOnDiscountDTO.class);
			List<AvailableRoomsDTO> roomsDTO = new ArrayList<>();
			List<Hotel> hotels = hotelService.findAll();
			for(Hotel hotel: hotels){
				if(hotel.getLocation().getCity().equals(roomOnDiscount.getLocation().getCity())){
					List<Room> rooms = hotel.getRooms();
					RoomDTO roomDTO = new RoomDTO();
					for (Room room : rooms)
					{
						if (room.getDiscount() != null && room.getDiscount() > 0)
						{
							int counter = 0;
							roomDTO = new RoomDTO(room);
							LocalDate endingDate = roomOnDiscount.getCheckIn().plusDays(roomOnDiscount.getNumberOfDays());
							for(RoomReservationRooms reservation: room.getRoomReservations()){
								if((roomOnDiscount.getCheckIn().isBefore(reservation.getRoomReservation().getReservationEndDate()) && roomOnDiscount.getCheckIn().isAfter(reservation.getRoomReservation().getReservationStartDate())) ||
								   (endingDate.isBefore(reservation.getRoomReservation().getReservationEndDate()) && endingDate.isAfter(reservation.getRoomReservation().getReservationStartDate()))){
									counter++;
								}
							}
							int availableRooms = room.getNumberOfRooms() - counter; 
							if (availableRooms == 0){
								continue;
							}
							double totalPrice = room.getDefaultPrice()*roomOnDiscount.getNumberOfDays();
							Double sum = 0.0;
							for(RoomRating rr: room.getRoomRatings()){
								sum += rr.getRating();
							}
							Double rating = 0.0;
							if(sum != 0.0){
								rating = sum/room.getRoomRatings().size();
							}
							roomsDTO.add(new AvailableRoomsDTO(roomDTO, totalPrice, rating, availableRooms, endingDate));
						}
					}
				}
			}
			return new ResponseEntity<>(roomsDTO, HttpStatus.OK);	
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{id}/roomsNotOnDiscount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> roomsNotOnDiscount(@PathVariable Long id) 
	{
		try 
		{
			Hotel hotel = hotelService.findOne(id);
			List<Room> rooms = hotel.getRooms();
		
			for (Room room : rooms)
			{
				if (room.getDiscount() == null || room.getDiscount() == 0)
				{
					return new ResponseEntity<>(true, HttpStatus.OK);
				}
			}
			
			return new ResponseEntity<>(false, HttpStatus.OK);
		}
		catch (Exception e) 
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value="/rateHotel", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> rateHotel(@RequestBody UserRatingHotelDTO userRatingHotelDTO) 
	{
		if (userRatingHotelDTO.getHotel() == null || userRatingHotelDTO.getRating() == null || userRatingHotelDTO.getUsername() == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		User user = null;
		try
		{
			user = this.userService.findByUsernameUser(userRatingHotelDTO.getUsername());
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		try
		{
			Hotel hotel = this.hotelService.findOne(userRatingHotelDTO.getHotel().getId());
			HotelRating hotelRating = this.hotelService.findOneRating(new HotelRatingId(hotel, user));
			if (hotelRating == null )
			{				
				hotelRating = new HotelRating();
				hotelRating.setId(new HotelRatingId(hotel, user));
			}
			hotelRating.setRating(userRatingHotelDTO.getRating());
			this.hotelService.saveRating(hotelRating);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/{id}/getHotelChartData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ChartDataDTO>> getHotelChartData(@PathVariable Long id) 
	{
		try 
		{
			Hotel hotel = hotelService.findOne(id);
			List<Room> rooms = hotel.getRooms();

			LocalDate date = LocalDate.of(2019, Month.JANUARY, 1);
			LocalDate endDate = LocalDate.of(2019, Month.APRIL, 1);
			
			List<ChartDataDTO> chartData = new ArrayList<>();
			while (date.isBefore(endDate))
			{
				int numOfReservations = 0;
				for (RoomReservation roomReservation : roomReservationService.findAllByDate(date))
				{
					for (RoomReservationRooms reservation : roomReservation.getRooms())
					{
						for (Room room : rooms)
						{
							if (reservation.getRoom().getId() == room.getId())
							{
								numOfReservations++;
							}
						}
					}
				}
				ChartDataDTO rentACarChartDataDTO = new ChartDataDTO(date, numOfReservations);  
				chartData.add(rentACarChartDataDTO);
				date = date.plusDays(1);
			}
			
			return new ResponseEntity<>(chartData, HttpStatus.OK);
		}
		catch (Exception e) 
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
