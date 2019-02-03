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
import javax.persistence.OneToOne;
import javax.persistence.Version;

import org.isa.takeoff.dto.RentACarDTO;

@Entity
public class RentACar {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name", nullable = false)
	private String name;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Location location;	
	
	@Column(name="description", nullable = true)
	private String description;
	
	@OneToMany(mappedBy = "rentACar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<RentACarMainService> rentACarMainServices = new HashSet<>();
	
	@OneToMany(mappedBy ="rentACar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Vehicle> vehicles = new HashSet<>();
	
	@OneToMany(mappedBy = "rentACar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Office> offices = new HashSet<>();
	
	@OneToMany(mappedBy = "id.rentACar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<RentACarRating> rentACarRatings = new HashSet<>();
	
	@Version
	private Long version;
	
	public RentACar() { }
	
	public RentACar(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}
	
	public RentACar(RentACarDTO rentACarDTO)
	{
		this(rentACarDTO.getName(), rentACarDTO.getDescription());
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
		
	public List<RentACarMainService> getMainServicesRentACar() {
		return new ArrayList<>(rentACarMainServices);
	}

	public void setMainServicesRentACar(List<RentACarMainService> rentACarMainServices) {
		this.rentACarMainServices = new HashSet<>(rentACarMainServices);
	}

	public List<Vehicle> getVehicles() {
		return new ArrayList<>(vehicles);
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = new HashSet<>(vehicles);
	}

	public List<Office> getOffices() {
		return new ArrayList<>(offices);
	}

	public void setOffices(List<Office> offices) {
		this.offices = new HashSet<>(offices);
	}

	public List<RentACarRating> getRentACarRatings() {
		return new ArrayList<>(rentACarRatings);
	}

	public void setRentACarRatings(List<RentACarRating> rentACarRatings) {
		this.rentACarRatings = new HashSet<>(rentACarRatings);
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
		RentACar that = (RentACar) o;
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
