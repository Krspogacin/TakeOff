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

import org.isa.takeoff.dto.LocationDTO;

@Entity
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "address", nullable = false)
	private String address;
	
	@Column(name = "country", nullable = false)
	private String country;

	@Column(name = "city", nullable = false)
	private String city;
	
	@Column(name = "latitude", nullable = false)
	private Double latitude;
	
	@Column(name = "longitude", nullable = false)
	private Double longitude;

	@ManyToMany(mappedBy = "destinations")
	private Set<AirCompany> companies = new HashSet<>();

	@ManyToMany(mappedBy = "transferDestinations")
	private Set<Flight> flights = new HashSet<>();
	
	@Version
	private Long version;

	public Location() {
	}

	public Location(String address, String country, String city, Double latitude, Double longitude) {
		this.address = address;
		this.country = country;
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Location(LocationDTO locationDTO) {
		this(locationDTO.getAddress(), locationDTO.getCountry(), locationDTO.getCity(), locationDTO.getLatitude(), locationDTO.getLongitude());
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
		Location that = (Location) o;
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