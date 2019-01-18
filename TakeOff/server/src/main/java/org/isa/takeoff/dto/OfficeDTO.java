package org.isa.takeoff.dto;

import org.isa.takeoff.model.Office;

public class OfficeDTO 
{
	private Long id;
	private String address;
	private RentACarDTO rentACar;
	private Long version;
	
	public OfficeDTO() { }
	
	public OfficeDTO(Office office)
	{
		this(office.getId(), office.getAddress(), new RentACarDTO(office.getRentACar()), office.getVersion());
	}
	
	public OfficeDTO(Long id, String address, RentACarDTO rentACar, Long version)
	{
		this.id = id;
		this.address = address;
		this.rentACar = rentACar;
		this.version = version;
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

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}