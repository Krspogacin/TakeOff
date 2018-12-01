package org.isa.takeoff.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class RentACarRating implements Serializable{
	
	@Id
	@ManyToOne
	@JoinColumn(name="rentacar_id", referencedColumnName="id")
	private RentACar rentACar;
	
	@Id
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;
	
	@Column(name = "rating", nullable = false)
	private Double rating;

	public RentACar getRentACar() {
		return rentACar;
	}

	public void setRentACar(RentACar rentACar) {
		this.rentACar = rentACar;
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
 
        RentACarRating that = (RentACarRating) o;
        return Objects.equals(rentACar, that.rentACar) &&
        		Objects.equals(user, that.user);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(rentACar, user);
    }

}
