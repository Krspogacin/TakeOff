package org.isa.takeoff.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Version;

@Entity
public class AirCompany {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "description", nullable = true)
	private String description;

	@ManyToMany
	@JoinTable(name = "air_company_destination", joinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "dest_id", referencedColumnName = "id"))
	private Set<Location> destinations = new HashSet<>();

	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Flight> flights = new HashSet<>();

	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<AirCompanyRating> companyRatings = new HashSet<>();

	@Version
	private Long version;

	public AirCompany() {
	}

	public AirCompany(String name, String address, String description) {
		this.name = name;
		this.address = address;
		this.description = description;
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

	public List<Location> getDestinations() {
		return new ArrayList<>(destinations);
	}

	public void setDestinations(List<Location> destinations) {
		this.destinations = new HashSet<>(destinations);
	}

	public List<Flight> getFlights() {
		return new ArrayList<>(flights);
	}

	public void setFlights(List<Flight> flights) {
		this.flights = new HashSet<>(flights);
	}

	public Set<AirCompanyRating> getCompanyRatings() {
		return companyRatings;
	}

	public void setCompanyRatings(List<AirCompanyRating> companyRatings) {
		this.companyRatings = new HashSet<>(companyRatings);
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AirCompany that = (AirCompany) o;
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