package org.isa.takeoff.dto;

public class UserRatingHotelDTO 
{
	private String username;
	private Double rating;
	private HotelDTO hotel;
	
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
	public HotelDTO getHotel() {
		return hotel;
	}
	public void setHotel(HotelDTO hotel) {
		this.hotel = hotel;
	}
}
