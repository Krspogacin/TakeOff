package org.isa.takeoff.dto;

import org.isa.takeoff.model.Administrator;

public class AdministratorDTO 
{
	private Long id;
	private String username;
	private String password;
	private String email;
	private AirCompanyDTO airCompanyDTO;
	private HotelDTO hotelDTO;
	private RentACarDTO rentACarDTO;
	
	public AdministratorDTO() { }
	
	public AdministratorDTO(Long id, String username, String password, String email) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	public AdministratorDTO(Administrator admin)
	{
		this(admin.getId(), admin.getUsername(), admin.getPassword(), admin.getEmail());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AirCompanyDTO getAirCompanyDTO() {
		return airCompanyDTO;
	}

	public void setAirCompanyDTO(AirCompanyDTO airCompanyDTO) {
		this.airCompanyDTO = airCompanyDTO;
	}

	public HotelDTO getHotelDTO() {
		return hotelDTO;
	}

	public void setHotelDTO(HotelDTO hotelDTO) {
		this.hotelDTO = hotelDTO;
	}

	public RentACarDTO getRentACarDTO() {
		return rentACarDTO;
	}

	public void setRentACarDTO(RentACarDTO rentACarDTO) {
		this.rentACarDTO = rentACarDTO;
	}	
}