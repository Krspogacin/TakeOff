package org.isa.takeoff.dto;

import java.time.LocalDate;

public class AvailableRoomsDTO {

	private RoomDTO room;
	private Double totalPrice;
	private Double rating;
	private Integer number;
	private LocalDate endingDate;
	

	public AvailableRoomsDTO() {}
	
	public AvailableRoomsDTO(RoomDTO room, Double totalPrice, Double rating, Integer number, LocalDate endingDate) {
		this.room = room;
		this.totalPrice = totalPrice;
		this.rating = rating;
		this.number = number;
		this.endingDate = endingDate;
	}
	
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public RoomDTO getRoom() {
		return room;
	}
	public void setRoom(RoomDTO room) {
		this.room = room;
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
	public LocalDate getEndingDate() {
		return endingDate;
	}
	
	public void setEndingDate(LocalDate endingDate) {
		this.endingDate = endingDate;
	}
}
