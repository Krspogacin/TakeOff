package org.isa.takeoff.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.isa.takeoff.dto.AdministratorDTO;
import org.isa.takeoff.dto.UserDTO;
import org.isa.takeoff.model.Administrator;
import org.isa.takeoff.model.Authority;
import org.isa.takeoff.model.User;
import org.isa.takeoff.service.AdministratorService;
import org.isa.takeoff.service.AuthorityService;
import org.isa.takeoff.service.EmailService;
import org.isa.takeoff.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController 
{
	@Autowired
	private UserService userService;
	
	@Autowired
	private AdministratorService administratorService;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	public PasswordEncoder passwordEncoder;
		
	@RequestMapping(method = GET, value = "/getUserByUsername/{username}")
	public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username)
	{
		try 
		{
			User user = this.userService.findByUsername(username);
			UserDTO userDTO = new UserDTO(user);
			return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
		}
		catch (Exception e) 
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(method = GET, value = "/getAdminByUsername/{username}")
	public ResponseEntity<AdministratorDTO> getAdminByUsername(@PathVariable String username)
	{
		try 
		{
			Administrator admin = this.administratorService.findByUsername(username);
			AdministratorDTO adminDTO = new AdministratorDTO(admin);
			return new ResponseEntity<AdministratorDTO>(adminDTO, HttpStatus.OK);
		}
		catch (Exception e) 
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(method = GET)
	public ResponseEntity<List<UserDTO>> getAllUsers()
	{
		List<User> users = this.userService.findAll();
		List<UserDTO> usersDTO = new ArrayList<>();
		for (User user : users)
		{
			usersDTO.add(new UserDTO(user));
		}
		return new ResponseEntity<List<UserDTO>>(usersDTO, HttpStatus.OK);
	}
	
	@RequestMapping(method = GET, value = "/checkUsername/{username}")
	public ResponseEntity<?> checkUsername(@PathVariable String username)
	{
		User user = this.userService.findByUsername(username);
		if (user != null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Administrator admin = this.administratorService.findByUsername(username);
		if (admin != null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		else
		{
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	@RequestMapping(method = GET, value = "/checkEmail/{email}")
	public ResponseEntity<?> checkEmail(@PathVariable String email)
	{
		User user = this.userService.findByEmail(email);
		if (user != null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Administrator admin = this.administratorService.findByEmail(email);
		if (admin != null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		else
		{
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	@RequestMapping(method = POST, value = "/register", consumes="application/json")
	public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO, HttpServletRequest request)
	{
		User user = new User(userDTO);
		
		//Check if user already exists
		if (this.userService.findByUsername(user.getUsername()) != null || this.userService.findByEmail(user.getEmail()) != null)
		{
			return new ResponseEntity<UserDTO>(HttpStatus.CONFLICT);
		}

		//Encode user's password
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		//Set user's authority
		Authority userAuthority = this.authorityService.findByName("ROLE_USER");
		user.setAuthority(userAuthority);
				
		//Send email to user with activation link
		try 
		{
			this.emailService.sendRegistrationNotificaition(user, request);
		} 
		catch (MailException | InterruptedException | MessagingException e) { }
		
		user = this.userService.save(user);
		return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
	}
	
	@RequestMapping(method = GET, value = "/register/verify")
	public ResponseEntity<UserDTO> verifyUser(@RequestParam String token)
	{
		String username = this.emailService.getTokenUtils().getUsernameFromToken(token);
		User user = this.userService.findByUsername(username);
		if (user != null)
		{
			long expiresIn = this.emailService.getTokenUtils().getExpirationDateFromToken(token).getTime();			
			if (user.isEnabled() || expiresIn <= new Date().getTime())
			{
				return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
			}
			else
			{
				user.setEnabled(true);	
			}
		}
		else
		{
			return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
		}
		
		user = this.userService.save(user);
		return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
	}
}