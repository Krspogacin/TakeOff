package org.isa.takeoff.dto;

import java.time.LocalDate;
import java.util.List;

import org.isa.takeoff.model.RoomReservation;

public class RoomReservationDTO 
{
	private Long id;
	private List<RoomRatingDTO> roomsAndRatings;
	private LocalDate reservationStartDate;
	private LocalDate reservationEndDate;
	private Double totalPrice;
	private Long reservationId;
	private Double hotelRating;
	
	public RoomReservationDTO() { }

	public RoomReservationDTO(Long id, LocalDate reservationStartDate, LocalDate reservationEndDate, Double totalPrice) {
		super();
		this.id = id;
		this.reservationStartDate = reservationStartDate;
		this.reservationEndDate = reservationEndDate;
		this.totalPrice = totalPrice;
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

	public List<RoomRatingDTO> getRoomsAndRatings() {
		return roomsAndRatings;
	}

	public void setRoomsAndRatings(List<RoomRatingDTO> roomsAndRatings) {
		this.roomsAndRatings = roomsAndRatings;
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

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Long getReservationId() {
		return reservationId;
	}

	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
	}

	public Double getHotelRating() {
		return hotelRating;
	}

	public void setHotelRating(Double hotelRating) {
		this.hotelRating = hotelRating;
	}
}