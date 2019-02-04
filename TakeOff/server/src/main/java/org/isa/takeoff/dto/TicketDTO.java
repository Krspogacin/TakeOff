package org.isa.takeoff.dto;

import org.isa.takeoff.model.Ticket;

public class TicketDTO {

	private Long id;
	private Integer number;
	private String type;
	private Double discount;
	private boolean isOnDiscount;
	private boolean isReserved;
	private FlightDTO flight;
	private Long version;

	public TicketDTO() {

	}

	public TicketDTO(Ticket ticket) {
		this(ticket.getId(), ticket.getNumber(), ticket.getType(), ticket.getDiscount(), ticket.getIsOnDiscount(),
				ticket.getIsReserved(), new FlightDTO(ticket.getFlight()), ticket.getVersion());
	}

	public TicketDTO(Long id, Integer number, String type, Double discount, boolean isOnDiscount, boolean isReserved,
			FlightDTO flight, Long version) {
		super();
		this.id = id;
		this.number = number;
		this.type = type;
		this.discount = discount;
		this.isOnDiscount = isOnDiscount;
		this.isReserved = isReserved;
		this.flight = flight;
		this.version = version;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public boolean isOnDiscount() {
		return isOnDiscount;
	}

	public void setOnDiscount(boolean isOnDiscount) {
		this.isOnDiscount = isOnDiscount;
	}

	public boolean isReserved() {
		return isReserved;
	}

	public void setReserved(boolean isReserved) {
		this.isReserved = isReserved;
	}

	public FlightDTO getFlight() {
		return flight;
	}

	public void setFlight(FlightDTO flight) {
		this.flight = flight;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
