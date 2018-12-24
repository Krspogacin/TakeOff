package org.isa.takeoff.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Version;

@Entity
public class Destination {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "country", nullable = false)
	private String country;

	@Column(name = "city", nullable = false)
	private String city;

	@Column(name = "airportName", nullable = false)
	private String airportName;

	@ManyToMany(mappedBy = "destinations")
	private Set<AirCompany> companies = new HashSet<>();

	@ManyToMany(mappedBy = "transferDestinations")
	private Set<Flight> flights = new HashSet<>();

	@Version
	private Long version;

	public Destination() {
	}

	public Destination(String country, String city, String airportName) {
		this.country = country;
		this.city = city;
		this.airportName = airportName;
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

	public List<AirCompany> getCompanies() {
		return new ArrayList<>(companies);
	}

	public void setCompanies(Set<AirCompany> companies) {
		this.companies = companies;
	}

	public List<Flight> getFlights() {
		return new ArrayList<>(flights);
	}

	public void setFlights(Set<Flight> flights) {
		this.flights = flights;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
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