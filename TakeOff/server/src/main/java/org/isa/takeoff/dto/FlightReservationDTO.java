package org.isa.takeoff.dto;

public class FlightReservationDTO {

	private UserDTO user;
	private TicketDTO ticket;
	private ReservationDTO reservationDTO;

	public FlightReservationDTO() {

	}

	public FlightReservationDTO(UserDTO user, TicketDTO ticket, ReservationDTO reservationDTO) {
		super();
		this.user = user;
		this.ticket = ticket;
		this.reservationDTO = reservationDTO;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public TicketDTO getTicket() {
		return ticket;
	}

	public void setTicket(TicketDTO ticket) {
		this.ticket = ticket;
	}

	public ReservationDTO getReservationDTO() {
		return reservationDTO;
	}

	public void setReservationDTO(ReservationDTO reservationDTO) {
		this.reservationDTO = reservationDTO;
	}
}