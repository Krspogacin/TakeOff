package org.isa.takeoff.dto;

public class AvailableVehiclesDTO 
{
	private VehicleDTO vehicle;
	private Double totalPrice;
	
	public AvailableVehiclesDTO() { }
	
	public AvailableVehiclesDTO(VehicleDTO vehicle, Double totalPrice) {
		this.vehicle = vehicle;
		this.totalPrice = totalPrice;
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
}