package org.isa.takeoff.dto;

import org.isa.takeoff.model.Hotel;
import org.isa.takeoff.model.Location;

public class HotelDTO {

	private Long id;
	private String name;
	private LocationDTO location;
	private String description;
	private Long version;

	public HotelDTO() {
	}

	public HotelDTO(Long id, String name, LocationDTO location, String description, Long version) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.description = description;
		this.version = version;
	}

	public HotelDTO(Hotel hotel) {
		this(hotel.getId(), hotel.getName(), new LocationDTO(hotel.getLocation()), hotel.getDescription(), hotel.getVersion());
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

	public LocationDTO getLocation() {
		return location;
	}

	public void setLocation(LocationDTO location) {
		this.location = location;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
