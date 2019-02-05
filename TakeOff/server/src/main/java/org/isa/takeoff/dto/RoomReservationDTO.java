package org.isa.takeoff.dto;

import java.time.LocalDate;
import java.util.List;

import org.isa.takeoff.model.RoomReservation;

public class RoomReservationDTO 
{
	private Long id;
	private List<RoomDTO> rooms;
	private LocalDate reservationStartDate;
	private LocalDate reservationEndDate;
	private Double price;
	private Long reservationId;
	
	public RoomReservationDTO() { }

	public RoomReservationDTO(Long id, LocalDate reservationStartDate, LocalDate reservationEndDate, Double price) {
		super();
		this.id = id;
		this.reservationStartDate = reservationStartDate;
		this.reservationEndDate = reservationEndDate;
		this.price = price;
	}
	
	public RoomReservationDTO(RoomReservation roomReservation) {
		this(roomReservation.getId(), roomReservation.getReservationStartDate(),
			 roomReservation.getReservationEndDate(), roomReservation.getPrice());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public List<RoomDTO> getRooms() {
		return rooms;
	}

	public void setRooms(List<RoomDTO> rooms) {
		this.rooms = rooms;
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