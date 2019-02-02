package org.isa.takeoff.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class AirCompanyRatingId implements Serializable {

	@ManyToOne
	@JoinColumn(name = "company_id", referencedColumnName = "id")
	private AirCompany company;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	public AirCompanyRatingId() {

	}

	public AirCompanyRatingId(AirCompany company, User user) {
		super();
		this.company = company;
		this.user = user;
	}

	public AirCompany getCompany() {
		return company;
	}

	public void setCompany(AirCompany company) {
		this.company = company;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		AirCompanyRatingId that = (AirCompanyRatingId) obj;
		return Objects.equals(company, that.company) && Objects.equals(user, that.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(company, user);
	}
}
