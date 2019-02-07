package org.isa.takeoff.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class RoomReservationRooms {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Room room;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private RoomReservation roomReservation;
	
	public RoomReservationRooms() {
	}
	
	public RoomReservationRooms(Long id, Room room, RoomReservation roomReservation) {
		this.id = id;
		this.room = room;
		this.roomReservation = roomReservation;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public RoomReservation getRoomReservation() {
		return roomReservation;
	}

	public void setRoomReservation(RoomReservation roomReservation) {
		this.roomReservation = roomReservation;
	}
	
	
	
	
}
