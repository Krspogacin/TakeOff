package org.isa.takeoff.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import org.isa.takeoff.dto.TicketDTO;

@Entity
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "number", nullable = false)
	private Integer number;

	@Column(name = "type")
	private String type;

	@Column(name = "discount")
	private Double discount;

	@Column(name = "isOnDiscount")
	private boolean isOnDiscount;

	@Column(name = "isReserved", nullable = false)
	private boolean isReserved;

	@ManyToOne(fetch = FetchType.EAGER)
	private Flight flight;

	@Version
	private Long version;
	
	public Ticket() {
		
	}

	public Ticket(TicketDTO ticketDTO) {
		super();
		this.id = ticketDTO.getId();
		this.number = ticketDTO.getNumber();
		this.type = ticketDTO.getType();
		this.discount = ticketDTO.getDiscount();
		this.isOnDiscount = ticketDTO.isOnDiscount();
		this.isReserved = ticketDTO.isReserved();
		this.version = ticketDTO.getVersion();
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

	public boolean getIsOnDiscount() {
		return isOnDiscount;
	}

	public void setIsOnDiscount(boolean isOnDiscount) {
		this.isOnDiscount = isOnDiscount;
	}

	public boolean getIsReserved() {
		return isReserved;
	}

	public void setIsReserved(boolean isReserved) {
		this.isReserved = isReserved;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Ticket that = (Ticket) o;
		if (that.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

}
