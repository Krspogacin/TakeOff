package org.isa.takeoff.dto;

import org.isa.takeoff.model.Destination;

public class DestinationDTO {

	private Long id;
	private String country;
	private String city;
	private String airportName;
	private Long version;

	public DestinationDTO() {

	}

	public DestinationDTO(Destination destination) {
		this(destination.getId(), destination.getCountry(), destination.getCity(), destination.getAirportName(),
				destination.getVersion());
	}

	public DestinationDTO(Long id, String country, String city, String airportName, Long version) {
		super();
		this.id = id;
		this.country = country;
		this.city = city;
		this.airportName = airportName;
		this.version = version;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getAirportName() {
		return airportName;
	}

	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
