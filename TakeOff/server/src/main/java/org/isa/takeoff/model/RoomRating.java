package org.isa.takeoff.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class RoomRating implements Serializable{
	
	@Id
	@ManyToOne
	@JoinColumn(name="room_id", referencedColumnName="id")
	private Room room;
	
	@Id
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;
	
	@Column(name = "rating", nullable = false)
	private Double rating;

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
 
        RoomRating that = (RoomRating) o;
        return Objects.equals(room, that.room) &&
        		Objects.equals(user, that.user);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(room, user);
    }

}
