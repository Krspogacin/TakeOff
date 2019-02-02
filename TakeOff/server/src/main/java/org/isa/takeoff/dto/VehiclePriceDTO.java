package org.isa.takeoff.dto;

import org.isa.takeoff.model.VehiclePrice;

public class VehiclePriceDTO 
{
	private Long id;
	private Double price;
	private VehicleDTO vehicle;
	private RentACarMainServiceDTO rentACarMainService;
	
	public VehiclePriceDTO() { }

	public VehiclePriceDTO(Long id, Double price, VehicleDTO vehicle, RentACarMainServiceDTO rentACarMainService) {
		this.id = id;
		this.price = price;
		this.vehicle = vehicle;
		this.rentACarMainService = rentACarMainService;
	}
	
	public VehiclePriceDTO(VehiclePrice vehiclePrice)
	{
		this(vehiclePrice.getId(), vehiclePrice.getPrice(), new VehicleDTO(vehiclePrice.getVehicle()), new RentACarMainServiceDTO(vehiclePrice.getRentACarMainService()));
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

	public VehicleDTO getVehicle() {
		return vehicle;
	}

	public void setVehicle(VehicleDTO vehicle) {
		this.vehicle = vehicle;
	}

	public RentACarMainServiceDTO getRentACarMainServiceDTO() {
		return rentACarMainService;
	}

	public void setRentACarMainServiceDTO(RentACarMainServiceDTO rentACarMainServiceDTO) {
		this.rentACarMainService = rentACarMainServiceDTO;
	}
}