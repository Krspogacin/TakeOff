package org.isa.takeoff.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class RoomReservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "roomReservation" ,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<RoomReservationRooms> rooms = new HashSet<>();

	@Column(name = "reservationStartDate", nullable = false)
	private LocalDate reservationStartDate;

	@Column(name = "reservationEndDate", nullable = false)
	private LocalDate reservationEndDate;
	
	@Column(name = "price", nullable = false)
	private Double price;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<RoomReservationRooms> getRooms() {
		return new ArrayList<>(rooms);
	}

	public void setRooms(List<RoomReservationRooms> rooms) {
		this.rooms = new HashSet<>(rooms);
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
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
