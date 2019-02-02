package org.isa.takeoff.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class VehicleRatingId implements Serializable
{
	@ManyToOne
	@JoinColumn(name="vehicle_id", referencedColumnName="id")
	private Vehicle vehicle;
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		VehicleRatingId that = (VehicleRatingId) o;
		return Objects.equals(vehicle, that.vehicle) && Objects.equals(user, that.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(vehicle, user);
	}
}