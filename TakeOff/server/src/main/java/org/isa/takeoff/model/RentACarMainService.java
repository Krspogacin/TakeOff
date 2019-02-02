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
public class RentACarMainService
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name", nullable = false)
	private String name;
	
	@Column(name="startDay", nullable = false)
	private Integer startDay;
	
	@Column(name="endDay", nullable = true)
	private Integer endDay;
	
	@OneToMany(mappedBy ="rentACarMainService", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<VehiclePrice> vehiclesAndPrices = new HashSet<>();
	
	@ManyToOne(fetch = FetchType.EAGER)
	private RentACar rentACar;
	
	public RentACarMainService() { }
	
	public RentACarMainService(String name, Integer startDay, Integer endDay) {
		super();
		this.name = name;
		this.startDay = startDay;
		this.endDay = endDay;
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

	public Integer getStartDay() {
		return startDay;
	}

	public void setStartDay(Integer startDay) {
		this.startDay = startDay;
	}

	public Integer getEndDay() {
		return endDay;
	}

	public void setEndDay(Integer endDay) {
		this.endDay = endDay;
	}

	public List<VehiclePrice> getVehiclesAndPrices() {
		return new ArrayList<>(vehiclesAndPrices);
	}

	public void setVehiclesAndPrices(List<VehiclePrice> vehiclesAndPrices) {
		this.vehiclesAndPrices = new HashSet<>(vehiclesAndPrices);
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
		RentACarMainService that = (RentACarMainService) o;
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