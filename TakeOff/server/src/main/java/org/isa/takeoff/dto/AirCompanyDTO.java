package org.isa.takeoff.dto;

import org.isa.takeoff.model.AirCompany;

public class AirCompanyDTO {

	private Long id;
	private String name;
	private String address;
	private String description;
	private Long version;

	public AirCompanyDTO() {

	}

	public AirCompanyDTO(AirCompany airCompany) {
		this(airCompany.getId(), airCompany.getName(), airCompany.getAddress(), airCompany.getDescription(),
				airCompany.getVersion());
	}

	public AirCompanyDTO(Long id, String name, String address, String description, Long version) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.description = description;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
