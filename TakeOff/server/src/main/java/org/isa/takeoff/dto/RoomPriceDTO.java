package org.isa.takeoff.dto;

import org.isa.takeoff.model.RoomPrice;

public class RoomPriceDTO {

	private Long id;
	private Double price;
	private String period;
	private RoomDTO room;
	
	public RoomPriceDTO(){}
	
	public RoomPriceDTO(RoomPrice roomPrice){
		this(roomPrice.getId(), roomPrice.getPrice(), roomPrice.getPeriod(), new RoomDTO(roomPrice.getRoom()));
	}
	
	public RoomPriceDTO(Long id, Double price, String period, RoomDTO room) {
		this.id = id;
		this.price = price;
		this.period = period;
		this.room = room;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public RoomDTO getRoom() {
		return room;
	}
	public void setRoom(RoomDTO roomDTO) {
		this.room = roomDTO;
	}
	
	
}
