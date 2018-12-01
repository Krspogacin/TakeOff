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
public class RentACar {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name", unique = true, nullable = false)
	private String name;
	
	@Column(name="address", nullable = false)
	private String address;
	
	@Column(name="description", nullable = true)
	private String description;
	
	@OneToMany(mappedBy = "rentACar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Service> services = new ArrayList<>();
	
	@OneToMany(mappedBy ="rentACar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Vehicle> vehicles = new ArrayList<>();
	
	@OneToMany(mappedBy = "rentACar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Office> offices = new ArrayList<>();
	
	@OneToMany(mappedBy = "rentACar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<RentACarRating> rentACarRatings = new ArrayList<>();
	
	public RentACar() { }
	
	public RentACar(String name, String address) {
		super();
		this.name = name;
		this.address = address;
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

	public boolean addService(Service e) {
		return services.add(e);
	}
	
	public boolean removeService(Object o) {
		return services.remove(o);
	}

	public Service getService(int index) {
		return services.get(index);
	}
	
	public boolean addVehicle(Vehicle e) {
		return vehicles.add(e);
	}
	
	public boolean removeVehicle(Object o) {
		return vehicles.remove(o);
	}

	public Vehicle getVehicle(int index) {
		return vehicles.get(index);
	}
	
	public boolean addOffice(Office e) {
		return offices.add(e);
	}
	
	public boolean removeOffice(Object o) {
		return offices.remove(o);
	}

	public Office getOffice(int index) {
		return offices.get(index);
	}
	
	public boolean addRentACarRating(RentACarRating e) {
		return rentACarRatings.add(e);
	}
	
	public boolean removeRentACarRating(Object o) {
		return rentACarRatings.remove(o);
	}

	public RentACarRating getRentACarRating(int index) {
		return rentACarRatings.get(index);
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
