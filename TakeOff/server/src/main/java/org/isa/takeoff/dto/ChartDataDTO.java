package org.isa.takeoff.dto;

import java.time.LocalDate;

public class ChartDataDTO
{
	private LocalDate date;
	private Integer value;
	
	public ChartDataDTO() { }
	
	public ChartDataDTO(LocalDate date, Integer value) {
		super();
		this.date = date;
		this.value = value;
	}
	
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
}