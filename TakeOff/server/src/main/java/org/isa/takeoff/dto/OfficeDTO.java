package org.isa.takeoff.dto;

import org.isa.takeoff.model.Office;

public class OfficeDTO 
{
	private Long id;
	private String name;
	private LocationDTO location;
	private RentACarDTO rentACar;
	private Long version;
	
	public OfficeDTO() { }
	
	public OfficeDTO(Office office)
	{
		this(office.getId(), office.getName(), new LocationDTO(office.getLocation()), new RentACarDTO(office.getRentACar()), office.getVersion());
	}
	
	public OfficeDTO(Long id, String name, LocationDTO location, RentACarDTO rentACar, Long version)
	{
		this.id = id;
		this.name = name;
		this.location = location;
		this.rentACar = rentACar;
		this.version = version;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocationDTO getLocation() {
		return location;
	}

	public void setLocation(LocationDTO location) {
		this.location = location;
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