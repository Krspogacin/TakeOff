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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Vehicle {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="brand", nullable = false)
	private String brand;
	
	@Column(name="model", nullable = false)
	private String model;
	
	@Column(name="year", nullable = false)
	private Integer year;
	
	@OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<VehicleRating> vehicleRatings = new HashSet<>();
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private RentACar rentACar;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
	
	public List<VehicleRating> getVehicleRatings() {
		return new ArrayList<>(vehicleRatings);
	}

	public void setVehicleRatings(List<VehicleRating> vehicleRatings) {
		this.vehicleRatings = new HashSet<>(vehicleRatings);
	}

	public RentACar getRentACar() {
		return rentACar;
	}

	public void setRentACar(RentACar rentACar) {
		this.rentACar = rentACar;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Vehicle that = (Vehicle) o;
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
