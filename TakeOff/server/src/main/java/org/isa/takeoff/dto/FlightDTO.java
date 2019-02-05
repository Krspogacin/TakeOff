package org.isa.takeoff.dto;

import java.time.LocalDateTime;

import org.isa.takeoff.model.Flight;

public class FlightDTO {

	private Long id;
	private LocationDTO takeOffLocation;
	private LocationDTO landingLocation;
	private LocalDateTime takeOffDate;
	private LocalDateTime landingDate;
	private Double ticketPrice;
	private AirCompanyDTO company;
	private FlightDiagramDTO diagram;
	private Long version;

	public FlightDTO() {

	}

	public FlightDTO(Flight flight) {
		this(flight.getId(), new LocationDTO(flight.getTakeOffLocation()), new LocationDTO(flight.getLandingLocation()), flight.getTakeOffDate(), flight.getLandingDate(), flight.getTicketPrice(),
				new AirCompanyDTO(flight.getCompany()), new FlightDiagramDTO(flight.getDiagram()), flight.getVersion());
	}

	public FlightDTO(Long id, LocationDTO takeOffLocation, LocationDTO landingLocation, LocalDateTime takeOffDate, LocalDateTime landingDate, Double ticketPrice,
			AirCompanyDTO company, FlightDiagramDTO diagram, Long version) {
		super();
		this.id = id;
		this.takeOffLocation = takeOffLocation;
		this.landingLocation = landingLocation;
		this.takeOffDate = takeOffDate;
		this.landingDate = landingDate;
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

	public LocationDTO getTakeOffLocation() {
		return takeOffLocation;
	}

	public void setTakeOffLocation(LocationDTO takeOffLocation) {
		this.takeOffLocation = takeOffLocation;
	}

	public LocationDTO getLandingLocation() {
		return landingLocation;
	}

	public void setLandingLocation(LocationDTO landingLocation) {
		this.landingLocation = landingLocation;
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
