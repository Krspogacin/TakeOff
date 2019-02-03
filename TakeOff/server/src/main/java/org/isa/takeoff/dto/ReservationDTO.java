package org.isa.takeoff.dto;

public class ReservationDTO {

	private UserDTO user;

	private TicketDTO ticket;

	public ReservationDTO() {

	}

	public ReservationDTO(UserDTO user, TicketDTO ticket) {
		super();
		this.user = user;
		this.ticket = ticket;
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

}
