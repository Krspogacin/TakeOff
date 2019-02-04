package org.isa.takeoff.dto;

import org.isa.takeoff.model.ServiceHotel;

public class ServiceHotelDTO {

	private Long id;
	private String name;
	private Double price;
	private HotelDTO hotel;
	
	public ServiceHotelDTO(){}
	
	public ServiceHotelDTO(ServiceHotel sh){
		this(sh.getId(),sh.getName(),sh.getPrice(),new HotelDTO(sh.getHotel()));
	}
	
	public ServiceHotelDTO(Long id, String name, Double price, HotelDTO hotel) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.hotel = hotel;
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public HotelDTO getHotel() {
		return hotel;
	}
	public void setHotel(HotelDTO hotel) {
		this.hotel = hotel;
	}
	
	
}
