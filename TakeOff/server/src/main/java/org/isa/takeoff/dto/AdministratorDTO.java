package org.isa.takeoff.dto;

import org.isa.takeoff.model.Administrator;

public class AdministratorDTO 
{
	private Long id;
	private String username;
	private String password;
	private Boolean enabled;
	private AirCompanyDTO airCompanyDTO;
	private HotelDTO hotelDTO;
	private RentACarDTO rentACarDTO;
	
	public AdministratorDTO() { }
	
	public AdministratorDTO(Long id, String username, String password, Boolean enabled) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}
	
	public AdministratorDTO(Administrator admin)
	{
		this(admin.getId(), admin.getUsername(), admin.getPassword(), admin.isEnabled());
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

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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