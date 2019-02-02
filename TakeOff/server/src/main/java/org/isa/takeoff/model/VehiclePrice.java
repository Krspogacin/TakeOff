package org.isa.takeoff.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class VehiclePrice 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="price", nullable = false)
	private Double price;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Vehicle vehicle;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private RentACarMainService rentACarMainService;
	
	public VehiclePrice() { }

	public VehiclePrice(Double price) {
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public RentACarMainService getRentACarMainService() {
		return rentACarMainService;
	}

	public void setRentACarMainService(RentACarMainService rentACarMainService) {
		this.rentACarMainService = rentACarMainService;
	}
}