package org.isa.takeoff.dto;

import org.isa.takeoff.model.RentACarMainService;

public class RentACarMainServiceDTO 
{
	private Long id;
	private String name;
	private Integer startDay;
	private Integer endDay;
	private RentACarDTO rentACar;
	
	public RentACarMainServiceDTO() { }

	public RentACarMainServiceDTO(RentACarMainService rentACarMainService) 
	{
		this(rentACarMainService.getId(), rentACarMainService.getName(), rentACarMainService.getStartDay(), 
			 rentACarMainService.getEndDay(), new RentACarDTO(rentACarMainService.getRentACar()));
	}
	
	public RentACarMainServiceDTO(Long id, String name, Integer startDay, Integer endDay, RentACarDTO rentACar) {
		this.id = id;
		this.name = name;
		this.startDay = startDay;
		this.endDay = endDay;
		this.rentACar = rentACar;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStartDay() {
		return startDay;
	}

	public void setStartDay(Integer startDay) {
		this.startDay = startDay;
	}

	public Integer getEndDay() {
		return endDay;
	}

	public void setEndDay(Integer endDay) {
		this.endDay = endDay;
	}

	public RentACarDTO getRentACar() {
		return rentACar;
	}

	public void setRentACar(RentACarDTO rentACar) {
		this.rentACar = rentACar;
	}
}