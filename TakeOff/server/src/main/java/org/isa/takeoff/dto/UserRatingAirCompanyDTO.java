package org.isa.takeoff.dto;

public class UserRatingAirCompanyDTO 
{
	private String username;
	private Double rating;
	private AirCompanyDTO airCompany;
	
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
	public AirCompanyDTO getAirCompany() {
		return airCompany;
	}
	public void setAirCompany(AirCompanyDTO airCompany) {
		this.airCompany = airCompany;
	}
	
	
}
