package org.isa.takeoff.dto;

public class AvailableVehiclesDTO 
{
	private VehicleDTO vehicle;
	private Double totalPrice;
	private Double rating;
	
	public AvailableVehiclesDTO() { }
	
	public AvailableVehiclesDTO(VehicleDTO vehicle, Double totalPrice, Double rating) {
		this.vehicle = vehicle;
		this.totalPrice = totalPrice;
		this.rating = rating;
	}

	public VehicleDTO getVehicle() {
		return vehicle;
	}
	
	public void setVehicle(VehicleDTO vehicle) {
		this.vehicle = vehicle;
	}
	
	public Double getTotalPrice() {
		return totalPrice;
	}
	
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}
}