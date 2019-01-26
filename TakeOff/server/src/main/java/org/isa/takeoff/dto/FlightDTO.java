package org.isa.takeoff.dto;

import java.time.LocalDateTime;

import org.isa.takeoff.model.Flight;

public class FlightDTO {

	private Long id;
	private LocalDateTime takeOffDate;
	private LocalDateTime landingDate;
	private Double distance;
	private Double ticketPrice;
	private AirCompanyDTO company;
	private FlightDiagramDTO diagram;
	private Long version;

	public FlightDTO() {

	}

	public FlightDTO(Flight flight) {
		this(flight.getId(), flight.getTakeOffDate(), flight.getLandingDate(), flight.getDistance(),
				flight.getTicketPrice(), new AirCompanyDTO(flight.getCompany()),
				new FlightDiagramDTO(flight.getDiagram()), flight.getVersion());
	}

	public FlightDTO(Long id, LocalDateTime takeOffDate, LocalDateTime landingDate, Double distance, Double ticketPrice,
			AirCompanyDTO company, FlightDiagramDTO diagram, Long version) {
		super();
		this.id = id;
		this.takeOffDate = takeOffDate;
		this.landingDate = landingDate;
		this.distance = distance;
		this.ticketPrice = ticketPrice;
		this.company = company;
		this.diagram = diagram;
		this.version = version;
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

	public Double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(Double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public AirCompanyDTO getCompany() {
		return company;
	}

	public void setCompany(AirCompanyDTO company) {
		this.company = company;
	}

	public FlightDiagramDTO getDiagram() {
		return diagram;
	}

	public void setDiagram(FlightDiagramDTO diagram) {
		this.diagram = diagram;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
