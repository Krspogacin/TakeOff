package org.isa.takeoff.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class VehicleRating implements Serializable
{
	@EmbeddedId
	private VehicleRatingId id;
	
	@Column(name = "rating", nullable = true)
	private Double rating;

	public VehicleRatingId getId() {
		return id;
	}

	public void setId(VehicleRatingId id) {
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

		VehicleRating that = (VehicleRating) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}