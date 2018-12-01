package org.isa.takeoff.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class VehicleRating implements Serializable{

	@Id
	@ManyToOne
	@JoinColumn(name="vehicle_id", referencedColumnName="id")
	private Vehicle vehicle;
	
	@Id
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;
	
	@Column(name = "rating", nullable = false)
	private Double rating;

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

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass()) 
            return false;
 
        VehicleRating that = (VehicleRating) o;
        return Objects.equals(vehicle, that.vehicle) &&
        		Objects.equals(user, that.user);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(vehicle, user);
    }
	
}
