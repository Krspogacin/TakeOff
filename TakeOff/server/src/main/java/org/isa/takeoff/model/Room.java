package org.isa.takeoff.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Room {

	public enum RoomType {
		APARTMAN, STUDIO, DUPLEX
	};

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "price", nullable = false)
	private Double price;

	@Column(name = "startingDate", nullable = false)
	private LocalDate startingDate;

	@Column(name = "endingDate", nullable = false)
	private LocalDate endingDate;

	@Column(name = "discount")
	private Double discount;

	@Column(name = "isReserved", nullable = false)
	private boolean isReserved;

	@Column(name = "floor", nullable = false)
	private Integer floor;

	@Column(name = "numberOfBeds", nullable = false)
	private Integer numberOfBeds;

	@Column(name = "type", nullable = false)
	private RoomType type;

	@Column(name = "hasBalcony")
	private boolean hasBalcony;

	@Column(name = "hasAirCondition")
	private boolean hasAirCondition;

	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<RoomRating> roomRatings = new HashSet<>();

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Hotel hotel;

	public Room() {

	}

	public Room(Double price, LocalDate startingDate, LocalDate endingDate, Integer floor, Integer numberOfBeds,
			RoomType type) {
		super();
		this.price = price;
		this.startingDate = startingDate;
		this.endingDate = endingDate;
		this.floor = floor;
		this.numberOfBeds = numberOfBeds;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public LocalDate getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(LocalDate startingDate) {
		this.startingDate = startingDate;
	}

	public LocalDate getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(LocalDate endingDate) {
		this.endingDate = endingDate;
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

	public Boolean getHasBalcony() {
		return hasBalcony;
	}

	public void setHasBalcony(Boolean hasBalcony) {
		this.hasBalcony = hasBalcony;
	}

	public Boolean getHasAirCondition() {
		return hasAirCondition;
	}

	public void setHasAirCondition(Boolean hasAirCondition) {
		this.hasAirCondition = hasAirCondition;
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

	public ArrayList<RoomRating> getRoomRatings() {
		return new ArrayList<RoomRating>(roomRatings);
	}

	public void setRoomRatings(Set<RoomRating> roomRatings) {
		this.roomRatings = roomRatings;
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
