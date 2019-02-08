package org.isa.takeoff.dto;

public class UserRatingRentACarDTO 
{
	private String username;
	private Double rating;
	private RentACarDTO rentACar;
	
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
	public RentACarDTO getRentACar() {
		return rentACar;
	}
	public void setRentACar(RentACarDTO rentACar) {
		this.rentACar = rentACar;
	}
}
