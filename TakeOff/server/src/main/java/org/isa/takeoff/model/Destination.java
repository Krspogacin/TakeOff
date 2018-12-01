package org.isa.takeoff.model;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Destination {	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="country", nullable = false)
	private String country;
	
	@Column(name="city", nullable = false)
	private String city;
	
	@Column(name="airportName", nullable = false)
	private String airportName;
	
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Flight flight;
	
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AirCompany company;
	
	public Destination() { }
	
	public Destination(String country, String city, String airportName) {
		this.country = country;
		this.city = city;
		this.airportName = airportName;
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

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public AirCompany getCompany() {
		return company;
	}

	public void setCompany(AirCompany company) {
		this.company = company;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Destination that = (Destination) o;
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