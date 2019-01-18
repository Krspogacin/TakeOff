package org.isa.takeoff.dto;

import org.isa.takeoff.model.Vehicle;

public class VehicleDTO 
{
	private Long id;
	private String brand;
	private String model;
	private Integer year;
	private boolean reserved;
	private RentACarDTO rentACar;
	private String image;
	private Long version;
	
	public VehicleDTO() { }

	public VehicleDTO(Vehicle vehicle)
	{
		this(vehicle.getId(), vehicle.getBrand(), vehicle.getModel(), vehicle.getYear(), vehicle.isReserved(), new RentACarDTO(vehicle.getRentACar()), vehicle.getImage() == null ? null : new String(vehicle.getImage()), vehicle.getVersion());
	}
	
	public VehicleDTO(Long id, String brand, String model, Integer year, boolean reserved, RentACarDTO rentACar, String image, Long version) 
	{
		this.id = id;
		this.brand = brand;
		this.model = model;
		this.year = year;
		this.rentACar = rentACar;
		this.version = version;
		this.reserved = reserved;
		this.image = image;
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

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

	public RentACarDTO getRentACar() {
		return rentACar;
	}

	public void setRentACar(RentACarDTO rentACar) {
		this.rentACar = rentACar;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}