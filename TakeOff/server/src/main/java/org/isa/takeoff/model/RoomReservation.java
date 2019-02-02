package org.isa.takeoff.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class RoomReservation implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Room room;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
	
	@Column(name = "reservationStartDate", nullable = false)
	private LocalDate reservationStartDate;
	
	@Column(name = "reservationEndDate", nullable = false)
	private LocalDate reservationEndDate;
	
	@Column(name = "reservationPrice", nullable = false)
	private Double reservationPrice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDate getReservationStartDate() {
		return reservationStartDate;
	}

	public void setReservationStartDate(LocalDate reservationStartDate) {
		this.reservationStartDate = reservationStartDate;
	}

	public LocalDate getReservationEndDate() {
		return reservationEndDate;
	}

	public void setReservationEndDate(LocalDate reservationEndDate) {
		this.reservationEndDate = reservationEndDate;
	}

	public Double getPrice() {
		return reservationPrice;
	}

	public void setPrice(Double price) {
		this.reservationPrice = price;
	}
}
