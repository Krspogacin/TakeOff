package org.isa.takeoff.model;

import java.time.LocalDateTime;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;

@Entity
public class Flight {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "takeOffDate", nullable = false)
	private LocalDateTime takeOffDate;

	@Column(name = "landingDate", nullable = false)
	private LocalDateTime landingDate;

	@Column(name = "distance", nullable = false)
	private Double distance;

	@ManyToMany
	@JoinTable(name = "flight_destination", joinColumns = @JoinColumn(name = "flight_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "dest_id", referencedColumnName = "id"))
	private Set<Location> transferDestinations = new HashSet<>();

	@OneToMany(mappedBy = "flight", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Ticket> tickets = new HashSet<>();

	@Column(name = "ticketPrice", nullable = false)
	private Double ticketPrice;

	@OneToMany(mappedBy = "id.flight", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<FlightRating> flightRatings = new HashSet<>();

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private AirCompany company;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private FlightDiagram diagram;

	@Version
	private Long version;

	public Flight() {
	}

	public Flight(LocalDateTime takeOffDate, LocalDateTime landingDate, double distance, Double ticketPrice) {
		this.takeOffDate = takeOffDate;
		this.landingDate = landingDate;
		this.distance = distance;
		this.ticketPrice = ticketPrice;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getTakeOffDate() {
		return takeOffDate;
	}

	public void setTakeOffDate(LocalDateTime takeOffDate) {
		this.takeOffDate = takeOffDate;
	}

	public LocalDateTime getLandingDate() {
		return landingDate;
	}

	public void setLandingDate(LocalDateTime landingDate) {
		this.landingDate = landingDate;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public List<Location> getTransferDestinations() {
		return new ArrayList<>(transferDestinations);
	}

	public void setTransferDestinations(List<Location> transferDestinations) {
		this.transferDestinations = new HashSet<>(transferDestinations);
	}

	public List<Ticket> getTickets() {
		return new ArrayList<>(tickets);
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = new HashSet<>(tickets);
	}

	public Double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(Double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public AirCompany getCompany() {
		return company;
	}

	public void setCompany(AirCompany company) {
		this.company = company;
	}

	public FlightDiagram getDiagram() {
		return diagram;
	}

	public void setDiagram(FlightDiagram diagram) {
		this.diagram = diagram;
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
		Flight that = (Flight) o;
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