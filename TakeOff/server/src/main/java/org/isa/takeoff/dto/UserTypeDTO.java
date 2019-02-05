package org.isa.takeoff.dto;

import org.isa.takeoff.model.Authority;

public class UserTypeDTO {
	
	private Boolean enabled;
	private String authority;
	
	public UserTypeDTO() {}
	
	public UserTypeDTO(Boolean enabled, String authority) {
		this.enabled = enabled;
		this.authority = authority;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
}
