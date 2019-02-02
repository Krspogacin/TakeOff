package org.isa.takeoff.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class RentACarRatingId implements Serializable 
{	
	@ManyToOne
	@JoinColumn(name="rentacar_id", referencedColumnName="id")
	private RentACar rentACar;
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;

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
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass()) 
            return false;
 
        RentACarRatingId that = (RentACarRatingId) o;
        return Objects.equals(rentACar, that.rentACar) && Objects.equals(user, that.user);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(rentACar, user);
    }
}