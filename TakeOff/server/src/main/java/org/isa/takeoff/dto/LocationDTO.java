package org.isa.takeoff.dto;

import org.isa.takeoff.model.Location;

public class LocationDTO {

	private Long id;
	private String address;
	private String country;
	private String city;
	private Double latitude;
	private Double longitude;
	private Long version;

	public LocationDTO() {

	}

	public LocationDTO(Long id, String address, String country, String city, Double latitude, Double longitude, Long version) {
		this.id = id;
		this.address = address;
		this.country = country;
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
		this.version = version;
	}

	public LocationDTO(Location location) {
		this(location.getId(), location.getAddress(), location.getCountry(), location.getCity(), 
			 location.getLatitude(), location.getLongitude(), location.getVersion());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}