package org.isa.takeoff.model;

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
import javax.persistence.OneToMany;

import org.isa.takeoff.dto.HotelDTO;

@Entity
public class Hotel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "description", nullable = true)
	private String description;

	@OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Room> rooms = new HashSet<>();

	@OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<ServiceHotel> services = new HashSet<>();

	@OneToMany(mappedBy = "id.hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<HotelRating> hotelRatings = new HashSet<>();
	
	public Hotel(){}
	
	public Hotel(HotelDTO hotelDTO) {
		this(hotelDTO.getName(), hotelDTO.getAddress(), hotelDTO.getDescription());
	}
	
	public Hotel(String name, String address, String description){
		this.name = name;
		this.address = address;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Room> getRooms() {
		return new ArrayList<Room>(rooms);
	}

	public void setRooms(Set<Room> rooms) {
		this.rooms = rooms;
	}

	public List<ServiceHotel> getServices() {
		return new ArrayList<>(services);
	}

	public void setServices(List<ServiceHotel> services) {
		this.services = new HashSet<>(services);
	}

	public List<HotelRating> getHotelRatings() {
		return new ArrayList<HotelRating>(hotelRatings);
	}

	public void setHotelRatings(Set<HotelRating> hotelRatings) {
		this.hotelRatings = hotelRatings;
	}

	public boolean addRoom(Room e) {
		return rooms.add(e);
	}

	public boolean removeRoom(Object o) {
		return rooms.remove(o);
	}

	public boolean addService(ServiceHotel e) {
		return services.add(e);
	}

	public boolean removeService(Object o) {
		return services.remove(o);
	}

	public boolean addHotelRating(HotelRating e) {
		return hotelRatings.add(e);
	}

	public boolean removeHotelRating(Object o) {
		return hotelRatings.remove(o);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Hotel that = (Hotel) o;
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
