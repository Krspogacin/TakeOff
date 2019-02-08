package org.isa.takeoff.dto;

public class UserRatingRoomDTO
{
	private String username;
	private Double rating;
	private RoomDTO room;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	public RoomDTO getRoom() {
		return room;
	}
	public void setRoom(RoomDTO room) {
		this.room = room;
	}
}
