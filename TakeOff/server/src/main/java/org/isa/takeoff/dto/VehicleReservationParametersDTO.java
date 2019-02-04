package org.isa.takeoff.dto;

import java.time.LocalDate;
import java.util.List;

import org.isa.takeoff.model.FuelType;
import org.isa.takeoff.model.TransmissionType;

public class VehicleReservationParametersDTO 
{
	private Long rentACarId;
	private LocalDate startDate;
	private LocalDate endDate;
	private Integer numOfPassengers;
	private Double minPrice;
	private Double maxPrice;
	private List<FuelType> fuelTypes;
	private List<TransmissionType> transmissionTypes;

	public Long getRentACarId() {
		return rentACarId;
	}

	public void setRentACarId(Long rentACarId) {
		this.rentACarId = rentACarId;
	}


	@Override
	public String toString() {
		return "VehicleReservationParametersDTO [rentACarId=" + rentACarId + ", startDate=" + startDate + ", endDate="
				+ endDate + ", numOfPassengers=" + numOfPassengers + ", minPrice=" + minPrice + ", maxPrice=" + maxPrice
				+ ", fuelTypes=" + fuelTypes + ", transmissionTypes=" + transmissionTypes + "]";
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Integer getNumOfPassengers() {
		return numOfPassengers;
	}

	public void setNumOfPassengers(Integer numOfPassengers) {
		this.numOfPassengers = numOfPassengers;
	}

	public Double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	public Double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}

	public List<FuelType> getFuelTypes() {
		return fuelTypes;
	}

	public void setFuelTypes(List<FuelType> fuelTypes) {
		this.fuelTypes = fuelTypes;
	}

	public List<TransmissionType> getTransmissionTypes() {
		return transmissionTypes;
	}

	public void setTransmissionTypes(List<TransmissionType> transmissionTypes) {
		this.transmissionTypes = transmissionTypes;
	}
}