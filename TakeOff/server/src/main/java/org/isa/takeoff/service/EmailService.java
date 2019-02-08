package org.isa.takeoff.service;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.isa.takeoff.model.Flight;
import org.isa.takeoff.model.Hotel;
import org.isa.takeoff.model.RoomReservation;
import org.isa.takeoff.model.User;
import org.isa.takeoff.model.Vehicle;
import org.isa.takeoff.model.VehicleReservation;
import org.isa.takeoff.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private Environment env;

	@Autowired
	private TokenUtils tokenUtils;

	@Async
	public void sendReservationMail(User user, Flight flight, Hotel hotel, RoomReservation roomReservation,  
			Vehicle vehicle, VehicleReservation vehicleReservation, Double total) throws MessagingException {
		
		System.out.println("Sending reservation email...");

		DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
		MimeMessage mail = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, false, "utf-8");
		try {
			helper.setTo(user.getEmail());
			helper.setFrom(env.getProperty("spring.mail.username"));
			helper.setSubject("TakeOff - Reservation info");
			String message = "Hello " + user.getUsername() + ",<br><br>"
					+ "You successfully created reservation. Here are full details:<br><br>"
					+ "<ul>"
						+ "<li>Flight details:<br><br>"
							+ "<ul>"
								+ "<li>From: " + flight.getTakeOffLocation().getCity() + ", " + flight.getTakeOffLocation().getCountry()  + "</li><br>"
								+ "<li>To: " + flight.getLandingLocation().getCity() + ", " + flight.getLandingLocation().getCountry() + "</li><br>"
								+ "<li>Departure: " + dtf.format(flight.getTakeOffDate()) + "</li><br>"
								+ "<li>Arrival: " + dtf.format(flight.getLandingDate())  + "</li><br>"
							+ "</ul>"
						+ "</li>";
			
			if(hotel != null) {
				message +="<li>Hotel details:<br><br>"
							+ "<ul>"
								+ "<li>Name: " + hotel.getName() + "</li><br>"
								+ "<li>Address: " + hotel.getLocation().getAddress() + "</li><br>"
								+ "<li>Check in date: " + roomReservation.getReservationStartDate() + "</li><br>"
								+ "<li>Check out date: " + roomReservation.getReservationEndDate() + "</li><br>"
							+ "</ul>"
						+ "</li>";
			}
			
			if(vehicle != null) {
				message +="<li>Rent-a-car details:<br><br>"
							+ "<ul>"
								+ "<li>Name: " + vehicle.getRentACar().getName() + "</li><br>"
								+ "<li>Address: " + vehicle.getRentACar().getLocation().getAddress() + "</li><br>"
								+ "<li>Vehicle: " + vehicle.getBrand() + " " + vehicle.getModel() + " " + vehicle.getYear() + "</li><br>"
								+ "<li>Starting date: " + vehicleReservation.getReservationStartDate() + "</li><br>"
								+ "<li>Ending date: " + vehicleReservation.getReservationEndDate() + "</li><br>"
							+ "</ul>"
						+ "</li>";
			}
			
			message += "</ul><br>"
					+ "<b>Total price: " + total + " €</b><br><br>"
					+ "<i>Your TakeOff Team</i>";
			
			helper.setText(message, true);
			javaMailSender.send(mail);

		} catch (MessagingException e) {
			throw new MessagingException();
		}

		System.out.println("Email sent!");

	}

	@Async
	public void sendRegistrationNotificaition(User user, HttpServletRequest request)
			throws MailException, InterruptedException, MessagingException {
		System.out.println("Sending email...");

		MimeMessage mail = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, false, "utf-8");
		try {
			helper.setTo(user.getEmail());
			helper.setFrom(env.getProperty("spring.mail.username"));
			helper.setSubject("TakeOff - Account Activation");
			String message = "Hello " + user.getUsername() + ",<br><br>"
					+ "Thank you for registering your TakeOff account. To finally activate your account please "
					+ "<a href=\"" + makeActivationLink(user.getUsername(), request) + "\">click here.</a><br><br>"
					+ "Regards, <br><br>" + "<i>Your TakeOff Team</i>";
			helper.setText(message, true);
			javaMailSender.send(mail);
		} catch (MessagingException e) {
			throw new MessagingException();
		}

		System.out.println("Email sent!");
	}

	private String makeActivationLink(String username, HttpServletRequest request) {
		StringBuilder url = new StringBuilder();
		url.append(this.getFullRequestURL(request));
		url.append("/verify?token=");
		url.append(this.tokenUtils.generateActivationToken(username));
		return url.toString();
	}

	private String getFullRequestURL(HttpServletRequest request) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String contextPath = request.getContextPath();
		String servletPath = request.getServletPath();

		// Reconstruct original requesting URL
		StringBuilder url = new StringBuilder();
		url.append(scheme).append("://").append(serverName);

		if (serverPort != 80) {
			url.append(":").append(serverPort);
		}

		url.append("/#");
		url.append(contextPath).append(servletPath);

		return url.toString();
	}

	public TokenUtils getTokenUtils() {
		return tokenUtils;
	}
}