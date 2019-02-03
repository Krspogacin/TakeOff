package org.isa.takeoff.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<FlightReservation> flightReservations = new HashSet<>();

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private RoomReservation roomReservation;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private VehicleReservation vehicleReservation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<FlightReservation> getFlightReservations() {
		return new ArrayList<>(flightReservations);
	}

	public void setFlightReservations(List<FlightReservation> flightReservations) {
		this.flightReservations = new HashSet<>(flightReservations);
	}

	public RoomReservation getRoomReservation() {
		return roomReservation;
	}

	public void setRoomReservation(RoomReservation roomReservation) {
		this.roomReservation = roomReservation;
	}

	public VehicleReservation getVehicleReservation() {
		return vehicleReservation;
	}

	public void setVehicleReservation(VehicleReservation vehicleReservation) {
		this.vehicleReservation = vehicleReservation;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		Reservation that = (Reservation) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
