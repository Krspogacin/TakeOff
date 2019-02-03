package org.isa.takeoff.dto;

import org.isa.takeoff.model.FuelType;
import org.isa.takeoff.model.TransmissionType;
import org.isa.takeoff.model.Vehicle;

public class VehicleDTO 
{
	private Long id;
	private String brand;
	private String model;
	private Integer year;
	private FuelType fuel;
	private Integer numOfSeats;
	private TransmissionType transmission;
	private boolean reserved;
	private Double discount;
	private RentACarDTO rentACar;
	private String image;
	private Long version;
	
	public VehicleDTO() { }

	public VehicleDTO(Vehicle vehicle)
	{
		this(vehicle.getId(), vehicle.getBrand(), vehicle.getModel(), vehicle.getYear(), vehicle.getFuel(), 
			 vehicle.getNumOfSeats(), vehicle.getTransmission(), vehicle.isReserved(), vehicle.getDiscount(),
			 new RentACarDTO(vehicle.getRentACar()), vehicle.getImage() == null ? null : new String(vehicle.getImage()), vehicle.getVersion());
	}
	
	public VehicleDTO(Long id, String brand, String model, Integer year, FuelType fuel, Integer numOfSeats, 
			   		  TransmissionType transmission, boolean reserved, Double discount, RentACarDTO rentACar, String image, Long version) 
	{
		this.id = id;
		this.brand = brand;
		this.model = model;
		this.year = year;
		this.fuel = fuel;
		this.numOfSeats = numOfSeats;
		this.transmission = transmission;
		this.reserved = reserved;
		this.discount = discount;
		this.rentACar = rentACar;
		this.image = image;
		this.version = version;
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

	public FuelType getFuel() {
		return fuel;
	}

	public void setFuel(FuelType fuel) {
		this.fuel = fuel;
	}

	public Integer getNumOfSeats() {
		return numOfSeats;
	}

	public void setNumOfSeats(Integer numOfSeats) {
		this.numOfSeats = numOfSeats;
	}

	public TransmissionType getTransmission() {
		return transmission;
	}

	public void setTransmission(TransmissionType transmission) {
		this.transmission = transmission;
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
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