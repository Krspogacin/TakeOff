package org.isa.takeoff.dto;

import org.isa.takeoff.model.Vehicle;

public class VehicleDTO 
{
	private Long id;
	private String brand;
	private String model;
	private Integer year;
	private RentACarDTO rentACar;
	
	public VehicleDTO() { }

	public VehicleDTO(Vehicle vehicle)
	{
		this(vehicle.getId(), vehicle.getBrand(), vehicle.getModel(), vehicle.getYear(), new RentACarDTO(vehicle.getRentACar()));
	}
	
	public VehicleDTO(Long id, String brand, String model, Integer year, RentACarDTO rentACar) 
	{
		this.id = id;
		this.brand = brand;
		this.model = model;
		this.year = year;
		this.rentACar = rentACar;
	}

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

	public RentACarDTO getRentACar() {
		return rentACar;
	}

	public void setRentACar(RentACarDTO rentACar) {
		this.rentACar = rentACar;
	}
}