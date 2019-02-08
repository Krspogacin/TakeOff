package org.isa.takeoff.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.isa.takeoff.dto.AdministratorDTO;
import org.isa.takeoff.dto.ChangePasswordDTO;
import org.isa.takeoff.dto.FriendDTO;
import org.isa.takeoff.dto.UserDTO;
import org.isa.takeoff.dto.UserTypeDTO;
import org.isa.takeoff.model.Administrator;
import org.isa.takeoff.model.AirCompany;
import org.isa.takeoff.model.Authority;
import org.isa.takeoff.model.Friend;
import org.isa.takeoff.model.Hotel;
import org.isa.takeoff.model.RentACar;
import org.isa.takeoff.model.User;
import org.isa.takeoff.security.AuthenticationRequest;
import org.isa.takeoff.service.AirCompanyService;
import org.isa.takeoff.service.AuthorityService;
import org.isa.takeoff.service.EmailService;
import org.isa.takeoff.service.HotelService;
import org.isa.takeoff.service.RentACarService;
import org.isa.takeoff.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private AirCompanyService airCompanyService;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private RentACarService rentACarService;

	@Autowired
	private EmailService emailService;

	@Autowired
	public PasswordEncoder passwordEncoder;

	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(method = GET, value = "/getUserByUsername/{username}")
	public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
		try {
			User user = this.userService.findByUsernameUser(username);
			UserDTO userDTO = new UserDTO(user);
			return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = GET, value = "/getAdminByUsername/{username}")
	public ResponseEntity<AdministratorDTO> getAdminByUsername(@PathVariable String username) {
		try {
			Administrator admin = this.userService.findByUsernameAdmin(username);
			AdministratorDTO adminDTO = new AdministratorDTO(admin);
			return new ResponseEntity<AdministratorDTO>(adminDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		List<User> users = this.userService.findAllUser();
		List<UserDTO> usersDTO = new ArrayList<>();
		for (User user : users) {
			usersDTO.add(new UserDTO(user));
		}
		return new ResponseEntity<List<UserDTO>>(usersDTO, HttpStatus.OK);
	}

	@RequestMapping(method = GET, value = "/checkUsername/{username}")
	public ResponseEntity<?> checkUsername(@PathVariable String username) {
		User user = this.userService.findByUsernameUser(username);
		if (user != null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Administrator admin = this.userService.findByUsernameAdmin(username);
		if (admin != null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	@RequestMapping(method = GET, value = "/checkEmail/{email}")
	public ResponseEntity<?> checkEmail(@PathVariable String email) {
		User user = this.userService.findByEmailUser(email);
		if (user != null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(method = POST, value = "/register", consumes = "application/json")
	public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
		User user = new User(userDTO);

		// Check if user already exists
		if (this.userService.findByUsernameUser(user.getUsername()) != null
				|| this.userService.findByEmailUser(user.getEmail()) != null) {
			return new ResponseEntity<UserDTO>(HttpStatus.CONFLICT);
		}

		// Encode user's password
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Set user's authority
		Authority userAuthority = this.authorityService.findByName("ROLE_USER");
		user.setAuthority(userAuthority);

		// Send email to user with activation link
		try {
			this.emailService.sendRegistrationNotificaition(user, request);
		} catch (MailException | InterruptedException | MessagingException e) {
		}

		user = this.userService.save(user);
		return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
	}

	@RequestMapping(method = GET, value = "/register/verify")
	public ResponseEntity<UserDTO> verifyUser(@RequestParam String token) {
		String username = this.emailService.getTokenUtils().getUsernameFromToken(token);
		User user = this.userService.findByUsernameUser(username);
		if (user != null) {
			long expiresIn = this.emailService.getTokenUtils().getExpirationDateFromToken(token).getTime();
			if (user.isEnabled() || expiresIn <= new Date().getTime()) {
				return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
			} else {
				user.setEnabled(true);
			}
		} else {
			return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
		}

		user = this.userService.save(user);
		return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
	}

	@RequestMapping(method = GET, value = "/{username}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<UserDTO> getLoggedInUser(@PathVariable String username) {

		User user = userService.findByUsernameUser(username);

		if (user != null) {
			return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.PUT)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {

		User user = userService.findByUsernameUser(userDTO.getUsername());

		if (user != null) {
			user.setFirstName(userDTO.getFirstName());
			user.setLastName(userDTO.getLastName());
			user.setPhoneNumber(userDTO.getPhoneNumber());
			user.setAddress(userDTO.getAddress());
			user.setDateOfBirth(userDTO.getDateOfBirth());
			user.setAboutMe(userDTO.getAboutMe());

			user = userService.save(user);

			return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = GET, value = "/{username}/friends")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<FriendDTO>> getUserFriends(@PathVariable String username) {

		User user1 = userService.findByUsernameUser(username);

		if (user1 != null) {
			List<Friend> friends = new ArrayList<>();
			friends.addAll(user1.getFriends());
			friends.addAll(user1.getFriendsOf());

			List<FriendDTO> friendsDTO = new ArrayList<>();
			for (Friend f : friends) {
				friendsDTO.add(new FriendDTO(f));
			}
			return new ResponseEntity<>(friendsDTO, HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = POST, value = "/friends")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<FriendDTO> sendFriendRequest(@RequestBody FriendDTO friendDTO) {

		User user1 = userService.findByUsernameUser(friendDTO.getUser1().getUsername());
		User user2 = userService.findByUsernameUser(friendDTO.getUser2().getUsername());

		if (user1 != null && user2 != null) {
			Friend friend = new Friend(user1, user2);

			user1.addFriend(friend);
			userService.save(user1);

			return new ResponseEntity<>(friendDTO, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/friends")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<FriendDTO> acceptFriendRequest(@RequestBody FriendDTO friendDTO) {

		User user1 = userService.findByUsernameUser(friendDTO.getUser1().getUsername());
		User user2 = userService.findByUsernameUser(friendDTO.getUser2().getUsername());

		if (user1 != null && user2 != null) {
			List<Friend> friends = user1.getFriends();
			for (Friend f : friends) {
				if (f.getId().getUser1().getId() == user1.getId() && f.getId().getUser2().getId() == user2.getId()) {
					f.setAccepted(true);
					break;
				}
			}
			userService.save(user1);

			return new ResponseEntity<>(friendDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// ne moze DELETE method zbog @RequestBody...
	@RequestMapping(method = RequestMethod.PUT, value = "/friends/delete")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<FriendDTO> deleteFriendRequest(@RequestBody FriendDTO friendDTO) {

		User user1 = userService.findByUsernameUser(friendDTO.getUser1().getUsername());
		User user2 = userService.findByUsernameUser(friendDTO.getUser2().getUsername());

		if (user1 != null && user2 != null) {
			List<Friend> friends = user1.getFriends();
			for (Friend f : friends) {
				if (f.getId().getUser1().getId() == user1.getId() && f.getId().getUser2().getId() == user2.getId()) {
					user1.removeFriend(f);
					break;
				}
			}
			userService.save(user1);

			return new ResponseEntity<>(friendDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = POST, value = "/addAdmin", consumes = "application/json")
	@PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
	public ResponseEntity<?> addAdministrator(@RequestBody AdministratorDTO adminDTO) {
		Administrator admin = new Administrator();
		admin.setUsername(adminDTO.getUsername());
		admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
		if (this.userService.findByUsernameAdmin(admin.getUsername()) != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		if (adminDTO.getAirCompanyDTO() != null) {
			Authority adminAuthority = this.authorityService.findByName("ROLE_AIRCOMPANY_ADMIN");
			admin.setAuthority(adminAuthority);
			admin.setEnabled(false);
			AirCompany airCompany = this.airCompanyService.findOne(adminDTO.getAirCompanyDTO().getId());
			if (airCompany != null) {
				admin.setAirCompany(airCompany);
			} else {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
		} else if (adminDTO.getHotelDTO() != null) {
			Authority adminAuthority = this.authorityService.findByName("ROLE_HOTEL_ADMIN");
			admin.setAuthority(adminAuthority);
			admin.setEnabled(false);
			Hotel hotel = this.hotelService.findOne(adminDTO.getHotelDTO().getId());
			if (hotel != null) {
				admin.setHotel(hotel);
			} else {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
		} else if (adminDTO.getRentACarDTO() != null) {
			Authority adminAuthority = this.authorityService.findByName("ROLE_RENTACAR_ADMIN");
			admin.setAuthority(adminAuthority);
			admin.setEnabled(false);
			RentACar rentACar = this.rentACarService.findOne(adminDTO.getRentACarDTO().getId());
			if (rentACar != null) {
				admin.setRentACar(rentACar);
			} else {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
		} else {
			Authority adminAuthority = this.authorityService.findByName("ROLE_SYS_ADMIN");
			admin.setAuthority(adminAuthority);
			admin.setEnabled(false);
		}
		admin = this.userService.save(admin);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(method = GET, value = "/checkUserType", produces = "application/json")
	public ResponseEntity<UserTypeDTO> checkUserType(@RequestParam String parameters) {

		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		try {
			authenticationRequest = objectMapper.readValue(parameters, AuthenticationRequest.class);
			Administrator admin = userService.findByUsernameAdmin(authenticationRequest.getUsername());
			if (admin != null && BCrypt.checkpw(authenticationRequest.getPassword(), admin.getPassword())) {
				return new ResponseEntity<>(new UserTypeDTO(admin.isEnabled(),
						((Authority) ((ArrayList<? extends GrantedAuthority>) admin.getAuthorities()).get(0))
								.getName()),
						HttpStatus.OK);
			}
			User user = userService.findByUsernameUser(authenticationRequest.getUsername());
			if (user != null && BCrypt.checkpw(authenticationRequest.getPassword(), user.getPassword())) {
				return new ResponseEntity<>(new UserTypeDTO(user.isEnabled(),
						((Authority) ((ArrayList<? extends GrantedAuthority>) user.getAuthorities()).get(0)).getName()),
						HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/updatePassword", consumes = "application/json")
	@PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasRole('ROLE_AIRCOMPANY_ADMIN') or hasRole('ROLE_HOTEL_ADMIN') or hasRole('ROLE_RENTACAR_ADMIN')")
	public ResponseEntity<?> updatePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {

		Administrator admin = userService.findByUsernameAdmin(changePasswordDTO.getUsername());
		if (admin != null) {
			if (BCrypt.checkpw(changePasswordDTO.getOldPassword(), admin.getPassword())) {
				admin.setEnabled(true);
				admin.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
				userService.save(admin);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}