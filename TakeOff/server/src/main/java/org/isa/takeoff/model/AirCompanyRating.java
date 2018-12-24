package org.isa.takeoff.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class AirCompanyRating implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name = "company_id", referencedColumnName = "id")
	private AirCompany company;

	@Id
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@Column(name = "rating", nullable = false)
	private Double rating;

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

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		AirCompanyRating that = (AirCompanyRating) o;
		return Objects.equals(company, that.company) && Objects.equals(user, that.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(company, user);
	}

}
