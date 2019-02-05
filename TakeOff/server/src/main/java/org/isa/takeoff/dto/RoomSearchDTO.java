package org.isa.takeoff.dto;

import java.time.LocalDate;
import java.util.List;

public class RoomSearchDTO {
	
	private Long hotelId;
	private LocalDate checkIn;
	private Integer numberOfDays;
	private Integer numberOfRooms;
	private List<Integer> numberOfBeds;
	private Double priceMin;
	private Double priceMax;
	
	public LocalDate getCheckIn() {
		return checkIn;
	}
	public void setCheckIn(LocalDate checkIn) {
		this.checkIn = checkIn;
	}
	public Integer getNumberOfDays() {
		return numberOfDays;
	}
	public void setNumberOfDays(Integer numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	public Integer getNumberOfRooms() {
		return numberOfRooms;
	}
	public void setNumberOfRooms(Integer numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}
	public List<Integer> getNumberOfBeds() {
		return numberOfBeds;
	}
	public void setNumberOfBeds(List<Integer> numberOfBeds) {
		this.numberOfBeds = numberOfBeds;
	}
	public Double getPriceMin() {
		return priceMin;
	}
	public void setPriceMin(Double priceMin) {
		this.priceMin = priceMin;
	}
	public Double getPriceMax() {
		return priceMax;
	}
	public void setPriceMax(Double priceMax) {
		this.priceMax = priceMax;
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}
}
