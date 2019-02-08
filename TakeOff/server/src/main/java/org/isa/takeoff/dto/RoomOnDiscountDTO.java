package org.isa.takeoff.dto;

import java.time.LocalDate;

public class RoomOnDiscountDTO {
	
	private LocalDate checkIn;
	private Integer numberOfDays;
	private LocationDTO location;
	
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
	public LocationDTO getLocation() {
		return location;
	}
	public void setLocation(LocationDTO location) {
		this.location = location;
	}
	
	
}
