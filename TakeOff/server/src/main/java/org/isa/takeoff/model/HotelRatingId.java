package org.isa.takeoff.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class HotelRatingId implements Serializable {

	@ManyToOne
	@JoinColumn(name = "hotel_id", referencedColumnName = "id")
	private Hotel hotel;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	public HotelRatingId(Hotel hotel, User user) {
		this.hotel = hotel;
		this.user = user;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
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
		if (obj == null || getClass() != obj.getClass()){
			return false;
		}
		HotelRatingId that = (HotelRatingId) obj;
		return Objects.equals(hotel, that.hotel) &&
			   Objects.equals(user, that.user);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(hotel,user);
	}
	
}
