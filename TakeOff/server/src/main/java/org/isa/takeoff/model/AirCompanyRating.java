package org.isa.takeoff.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class AirCompanyRating implements Serializable {

	@EmbeddedId
	private AirCompanyRatingId id;

	@Column(name = "rating", nullable = false)
	private Double rating;

	public AirCompanyRatingId getId() {
		return id;
	}

	public void setId(AirCompanyRatingId id) {
		this.id = id;
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
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
