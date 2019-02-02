package org.isa.takeoff.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class FlightRatingId implements Serializable {

	@ManyToOne
	@JoinColumn(name = "flight_id", referencedColumnName = "id")
	private Flight flight;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	public FlightRatingId() {

	}

	public FlightRatingId(Flight flight, User user) {
		this.flight = flight;
		this.user = user;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		FlightRatingId that = (FlightRatingId) obj;
		return Objects.equals(flight, that.flight) && Objects.equals(user, that.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(flight, user);
	}

}
