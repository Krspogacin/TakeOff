package org.isa.takeoff.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Hotel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name", unique = true, nullable = false)
	private String name;
	
	@Column(name="address", nullable = false)
	private String address;
	
	@Column(name="description", nullable = true)
	private String description;
	
	@OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Room> rooms = new ArrayList<>();
	
	@OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Service> services = new ArrayList<>();
	
	@OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<HotelRating> hotelRatings = new ArrayList<>();
	
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
	
	public boolean addRoom(Room e) {
		return rooms.add(e);
	}
	
	public boolean removeRoom(Object o) {
		return rooms.remove(o);
	}

	public Room getRoom(int index) {
		return rooms.get(index);
	}
	
	public boolean addService(Service e) {
		return services.add(e);
	}
	
	public boolean removeService(Object o) {
		return services.remove(o);
	}

	public Service getService(int index) {
		return services.get(index);
	}
	
	public boolean addHotelRating(HotelRating e) {
		return hotelRatings.add(e);
	}
	
	public boolean removeHotelRating(Object o) {
		return hotelRatings.remove(o);
	}

	public HotelRating getHotelRating(int index) {
		return hotelRatings.get(index);
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
