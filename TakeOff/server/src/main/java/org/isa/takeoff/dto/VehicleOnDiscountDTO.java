package org.isa.takeoff.dto;

import java.time.LocalDate;

public class VehicleOnDiscountDTO {

	private LocalDate startDate;
	private LocalDate endDate;
	private LocationDTO location;
	
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
	public LocationDTO getLocation() {
		return location;
	}
	public void setLocation(LocationDTO location) {
		this.location = location;
	}
}
