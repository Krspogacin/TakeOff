package org.isa.takeoff.dto;

import org.isa.takeoff.model.Reservation;

public class ReservationDTO 
{
	private Long id;
	private RoomReservationDTO roomReservation;
	private VehicleReservationDTO vehicleReservation;
	
	public ReservationDTO() { }
	
	public ReservationDTO(Long id, RoomReservationDTO roomReservation, VehicleReservationDTO vehicleReservation) {
		super();
		this.id = id;
		this.roomReservation = roomReservation;
		this.vehicleReservation = vehicleReservation;
	}

	public ReservationDTO(Reservation reservation) {
		this(reservation.getId(), new RoomReservationDTO(reservation.getRoomReservation()), new VehicleReservationDTO(reservation.getVehicleReservation()));
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public RoomReservationDTO getRoomReservation() {
		return roomReservation;
	}
	public void setRoomReservation(RoomReservationDTO roomReservation) {
		this.roomReservation = roomReservation;
	}
	public VehicleReservationDTO getVehicleReservation() {
		return vehicleReservation;
	}
	public void setVehicleReservation(VehicleReservationDTO vehicleReservation) {
		this.vehicleReservation = vehicleReservation;
	}
}