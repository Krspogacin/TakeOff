package org.isa.takeoff.dto;

import org.isa.takeoff.model.Office;

public class OfficeDTO 
{
	private Long id;
	private String address;
	private RentACarDTO rentACar;
	
	public OfficeDTO() { }
	
	public OfficeDTO(Office office)
	{
		this(office.getId(), office.getAddress(), new RentACarDTO(office.getRentACar()));
	}
	
	public OfficeDTO(Long id, String address, RentACarDTO rentACar)
	{
		this.id = id;
		this.address = address;
		this.rentACar = rentACar;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public RentACarDTO getRentACar() {
		return rentACar;
	}

	public void setRentACar(RentACarDTO rentACar) {
		this.rentACar = rentACar;
	}
}