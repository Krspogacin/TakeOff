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
public class AirCompany {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name", unique = true, nullable = false)
	private String name;
	
	@Column(name="address", nullable = false)
	private String address;
	
	@Column(name="description", nullable = true)
	private String description;
	
	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Destination> destinations = new ArrayList<>();

	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Flight> flights = new ArrayList<>();
	
	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<AirCompanyRating> companyRatings = new ArrayList<>();
	
	public AirCompany() { } 
	
	public AirCompany(String name, String address, String description) {
		this.name = name;
		this.address = address;
		this.description = description;
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

	public List<Destination> getDestinations() {
		return destinations;
	}

	public void setDestinations(List<Destination> destinations) {
		this.destinations = destinations;
	}

	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public boolean addLocation(Destination e) {
		return destinations.add(e);
	}

	public boolean removeLocation(Object o) {
		return destinations.remove(o);
	}

	public Destination getLocation(int index) {
		return destinations.get(index);
	}

	public boolean addFlight(Flight e) {
		return flights.add(e);
	}
	
	public boolean removeFlight(Object o) {
		return flights.remove(o);
	}

	public Flight getFlight(int index) {
		return flights.get(index);
	}
	
	public boolean addAirCompanyRating(AirCompanyRating e) {
		return companyRatings.add(e);
	}
	
	public boolean removeAirCompanyRating(Object o) {
		return companyRatings.remove(o);
	}

	public AirCompanyRating getAirCompanyRating(int index) {
		return companyRatings.get(index);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AirCompany that = (AirCompany) o;
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