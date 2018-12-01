package org.isa.takeoff.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Flight {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="takeOffDate", nullable = false)
	private LocalDateTime takeOffDate;
	
	@Column(name="landingDate", nullable = false)
	private LocalDateTime landingDate;
	
	@Column(name="duration", nullable = false)
	private String duration;
	
	@Column(name="distance", nullable = false)
	private Double distance;
	
	@Column(name="numberOfTransfers", nullable = false)
	private Integer numberOfTransfers;
	
	@OneToMany(mappedBy = "flight", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Destination> transferDestinations = new ArrayList<>();
	
	@OneToMany(mappedBy = "flight", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Ticket> tickets = new ArrayList<>();
	
	@Column(name="ticketPrice", nullable = false)
	private Double ticketPrice;
	
	@OneToMany(mappedBy = "flight", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FlightRating> flightRatings = new ArrayList<>();
	
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AirCompany company;
	
	public Flight() { }
	
	public Flight(LocalDateTime takeOffDate, LocalDateTime landingDate, String duration,
				double distance, int numberOfTransfers) {
		this.takeOffDate = takeOffDate;
		this.landingDate = landingDate;
		this.duration = duration;
		this.distance = distance;
		this.numberOfTransfers = numberOfTransfers;
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

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public int getNumberOfTransfers() {
		return numberOfTransfers;
	}

	public void setNumberOfTransfers(Integer numberOfTransfers) {
		this.numberOfTransfers = numberOfTransfers;
	}
	
	public Destination getDestination(Integer index) {
		return transferDestinations.get(index);
	}

	public boolean addDestination(Destination e) {
		return transferDestinations.add(e);
	}

	public boolean removeDestination(Object o) {
		return transferDestinations.remove(o);
	}
	
	public Ticket getTicket(int index) {
		return tickets.get(index);
	}

	public boolean addTicket(Ticket e) {
		return tickets.add(e);
	}

	public boolean removeTicket(Object o) {
		return tickets.remove(o);
	}

	public Double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(Double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	
	public FlightRating getFlightRating(int index) {
		return flightRatings.get(index);
	}

	public boolean addFlightRating(FlightRating e) {
		return flightRatings.add(e);
	}

	public boolean removeFlightRating(Object o) {
		return flightRatings.remove(o);
	}

	public AirCompany getCompany() {
		return company;
	}

	public void setCompany(AirCompany company) {
		this.company = company;
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