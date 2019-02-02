package org.isa.takeoff.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.isa.takeoff.dto.RoomDTO;

@Entity
public class Room {

	public enum RoomType {
		APARTMENT, STUDIO, DUPLEX, FAMILY, SUITE
	};

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "discount")
	private Double discount;
	
	@Column(name = "defaultPrice", nullable = false)
	private Double defaultPrice;

	@Column(name = "isReserved", nullable = false)
	private boolean isReserved;

	@Column(name = "floor", nullable = false)
	private Integer floor;

	@Column(name = "numberOfBeds", nullable = false)
	private Integer numberOfBeds;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private RoomType type;
	
	@Column(name = "hasBalcony", nullable = false)
	private boolean hasBalcony;
	
	@Column(name = "hasAirCondition", nullable = false)
	private boolean hasAirCondition;
	
	@Column(name = "numberOfRooms", nullable = false)
	private Integer numberOfRooms;
	
	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<RoomPrice> roomPrices = new HashSet<>();

	@OneToMany(mappedBy = "id.room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<RoomRating> roomRatings = new HashSet<>();
	
	@ManyToMany(mappedBy = "rooms", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<RoomReservation> roomReservations = new HashSet<>();

	@ManyToOne(fetch = FetchType.EAGER)
	private Hotel hotel;

	public Room() {

	}
	
	public Room(RoomDTO roomDTO){
		this(roomDTO.getDefaultPrice(), roomDTO.getDiscount(), roomDTO.isReserved(), roomDTO.getFloor(),
			roomDTO.getNumberOfBeds(), roomDTO.getType(), roomDTO.isHasBalcony(), roomDTO.isHasAirCondition(), roomDTO.getNumberOfRooms());
	}

	public Room(Double defaultPrice, Double discount, boolean isReserved, Integer floor, Integer numberOfBeds,
			RoomType type, boolean hasBalcony, boolean hasAirCondition, Integer numberOfRooms) {
		this.defaultPrice = defaultPrice;
		this.discount = discount;
		this.isReserved = isReserved;
		this.floor = floor;
		this.numberOfBeds = numberOfBeds;
		this.type = type;
		this.hasBalcony = hasBalcony;
		this.hasAirCondition = hasAirCondition;
		this.numberOfRooms = numberOfRooms;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Boolean getIsReserved() {
		return isReserved;
	}

	public void setIsReserved(Boolean isReserved) {
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

	public Double getDefaultPrice() {
		return defaultPrice;
	}

	public void setDefaultPrice(Double defaultPrice) {
		this.defaultPrice = defaultPrice;
	}

	public boolean addRoomRating(RoomRating e) {
		return roomRatings.add(e);
	}

	public boolean removeRoomRating(Object o) {
		return roomRatings.remove(o);
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public List<RoomRating> getRoomRatings() {
		return new ArrayList<>(roomRatings);
	}

	public void setRoomRatings(List<RoomRating> roomRatings) {
		this.roomRatings = new HashSet<>(roomRatings);
	}
	
	public boolean addRoomReservation(RoomReservation e) {
		return roomReservations.add(e);
	}

	public boolean removeRoomReservation(Object o) {
		return roomReservations.remove(o);
	}
	
	public List<RoomReservation> getRoomReservations() {
		return new ArrayList<>(roomReservations);
	}

	public void setRoomReservations(List<RoomReservation> roomReservations) {
		this.roomReservations = new HashSet<>(roomReservations);
	}
	
	public boolean addRoomPrice(RoomPrice e) {
		return roomPrices.add(e);
	}

	public boolean removeRoomPrice(Object o) {
		return roomPrices.remove(o);
	}
	
	public List<RoomPrice> getRoomPrices() {
		return new ArrayList<>(roomPrices);
	}

	public void setRoomPrices(List<RoomPrice> roomPrices) {
		this.roomPrices = new HashSet<>(roomPrices);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Room that = (Room) o;
		if (that.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

}
