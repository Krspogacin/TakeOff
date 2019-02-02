package org.isa.takeoff.dto;

import org.isa.takeoff.model.Room;
import org.isa.takeoff.model.Room.RoomType;

public class RoomDTO {
	
	private Long id;
	private Double defaultPrice;
	private Double discount;
	private boolean isReserved;
	private Integer floor;
	private Integer numberOfBeds;
	private RoomType type;
	private boolean hasBalcony;
	private boolean hasAirCondition;
	private Integer numberOfRooms;
	private HotelDTO hotel;
	
	public RoomDTO(){}
	
	public RoomDTO(Room room){
		this(room.getId(), room.getDefaultPrice(), room.getDiscount(), room.getIsReserved(), room.getFloor(),
		room.getNumberOfBeds(), room.getType(), room.isHasBalcony(), room.isHasAirCondition(), room.getNumberOfRooms(), new HotelDTO(room.getHotel()));
	}
	
	public RoomDTO(Long id, Double defaultPrice, Double discount, boolean isReserved, Integer floor, Integer numberOfBeds, RoomType roomType, boolean hasBalcony,
			boolean hasAirCondition, Integer numberOfRooms, HotelDTO hotel) {
		this.id = id;
		this.defaultPrice = defaultPrice;
		this.discount = discount;
		this.isReserved = isReserved;
		this.floor = floor;
		this.numberOfBeds = numberOfBeds;
		this.type = roomType;
		this.hasBalcony = hasBalcony;
		this.hasAirCondition = hasAirCondition;
		this.numberOfRooms = numberOfRooms;
		this.hotel = hotel;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getDefaultPrice() {
		return defaultPrice;
	}

	public void setDefaultPrice(Double defaultPrice) {
		this.defaultPrice = defaultPrice;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public boolean isReserved() {
		return isReserved;
	}
	public void setReserved(boolean isReserved) {
		this.isReserved = isReserved;
	}
	public Integer getFloor() {
		return floor;
	}
	public void setFloor(Integer floor) {
		this.floor = floor;
	}
	public Integer getNumberOfBeds() {
		return numberOfBeds;
	}
	public void setNumberOfBeds(Integer numberOfBeds) {
		this.numberOfBeds = numberOfBeds;
	}
	public RoomType getType() {
		return type;
	}
	public void setType(RoomType type) {
		this.type = type;
	}
	public boolean isHasBalcony() {
		return hasBalcony;
	}
	public void setHasBalcony(boolean hasBalcony) {
		this.hasBalcony = hasBalcony;
	}
	public boolean isHasAirCondition() {
		return hasAirCondition;
	}
	public void setHasAirCondition(boolean hasAirCondition) {
		this.hasAirCondition = hasAirCondition;
	}
	public Integer getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(Integer numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public HotelDTO getHotel() {
		return hotel;
	}
	public void setHotel(HotelDTO hotel) {
		this.hotel = hotel;
	}
	
	
	
}
