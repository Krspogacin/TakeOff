package org.isa.takeoff.dto;

import java.time.LocalDate;

import org.isa.takeoff.model.VehicleReservation;

public class VehicleReservationDTO 
{
	private Long id;
	private VehicleDTO vehicle;
	private LocalDate reservationStartDate;
	private LocalDate reservationEndDate;
	private Double price;
	private Long reservationId;
	
	public VehicleReservationDTO() { }

	public VehicleReservationDTO(Long id, VehicleDTO vehicle, LocalDate reservationStartDate,
			LocalDate reservationEndDate, Double price) {
		super();
		this.id = id;
		this.vehicle = vehicle;
		this.reservationStartDate = reservationStartDate;
		this.reservationEndDate = reservationEndDate;
		this.price = price;
	}
	
	public VehicleReservationDTO(VehicleReservation vehicleReservation) {
		this(vehicleReservation.getId(), new VehicleDTO(vehicleReservation.getVehicle()), vehicleReservation.getReservationStartDate(),
			 vehicleReservation.getReservationEndDate(), vehicleReservation.getPrice());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VehicleDTO getVehicle() {
		return vehicle;
	}

	public void setVehicle(VehicleDTO vehicle) {
		this.vehicle = vehicle;
	}

	public LocalDate getReservationStartDate() {
		return reservationStartDate;
	}

	public void setReservationStartDate(LocalDate reservationStartDate) {
		this.reservationStartDate = reservationStartDate;
	}

	public LocalDate getReservationEndDate() {
		return reservationEndDate;
	}

	public void setReservationEndDate(LocalDate reservationEndDate) {
		this.reservationEndDate = reservationEndDate;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getReservationId() {
		return reservationId;
	}

	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
	}
}