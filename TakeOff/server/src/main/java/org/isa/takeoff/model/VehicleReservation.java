package org.isa.takeoff.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class VehicleReservation implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Id
	@ManyToOne
	@JoinColumn(name="vehicle_id", referencedColumnName="id")
	private Vehicle vehicle;
	
	@Id
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;
	
	@Column(name = "rating", nullable = true)
	private Double rating;
	
	@Column(name = "reservationStartDate", nullable = false)
	private LocalDate reservationStartDate;
	
	@Column(name = "reservationEndDate", nullable = false)
	private LocalDate reservationEndDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass()) 
            return false;
 
        VehicleReservation that = (VehicleReservation) o;
        return Objects.equals(vehicle, that.vehicle) &&
        		Objects.equals(user, that.user);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(vehicle, user);
    }
	
}
