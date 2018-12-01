package org.isa.takeoff.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Administrator {
	
	public enum AdminType {SYS_ADMIN, AIRCOMPANY_ADMIN, HOTEL_ADMIN, RENTACAR_ADMIN};

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="username", unique = true, nullable = false)
	private String username;
	
	@Column(name="password", nullable=false)
	private String password;
	
	@Column(name="email", unique = true, nullable=false)
	private String email;
	
	@Column(name="type", nullable=false)
	private AdminType type;
	
	public Administrator() { }

	public Administrator(String username, String password, String email, AdminType type) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.type = type;
	}

	public Long getId() {
		return id;
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
	
	public AdminType getType() {
		return type;
	}

	public void setType(AdminType type) {
		this.type = type;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Administrator that = (Administrator) o;
		if (that.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

}
