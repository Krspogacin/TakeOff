package org.isa.takeoff.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.isa.takeoff.model.Administrator;
import org.isa.takeoff.model.Authority;
import org.isa.takeoff.model.User;
import org.isa.takeoff.model.UserState;
import org.isa.takeoff.security.AuthenticationRequest;
import org.isa.takeoff.security.TokenUtils;
import org.isa.takeoff.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController 
{
	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserState> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException, IOException
	{
		UserState userState = new UserState();
		User user = this.userService.findByUsernameUser(authenticationRequest.getUsername());
		boolean notUser = false;
		if (user == null || !user.isEnabled())
		{
			notUser = true;
		}
		else
		{
			String token = tokenUtils.generateToken(user.getUsername());
			userState = new UserState(token, user.getUsername(), user.isEnabled(), 
					((Authority)((ArrayList<? extends GrantedAuthority>)user.getAuthorities()).get(0)).getName(), 
					user.getImage() == null ? null : new String(user.getImage()));
		}
		
		if (notUser)
		{
			Administrator admin = this.userService.findByUsernameAdmin(authenticationRequest.getUsername());
			if (admin == null)
			{
				return new ResponseEntity<UserState>(HttpStatus.NOT_FOUND);
			}			
			else
			{
				String token = tokenUtils.generateToken(admin.getUsername());
				userState = new UserState(token, admin.getUsername(), admin.isEnabled(), ((Authority)((ArrayList<? extends GrantedAuthority>)admin.getAuthorities()).get(0)).getName(), null);
			}
		}
		
		Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
																				authenticationRequest.getUsername(),
																				authenticationRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new ResponseEntity<UserState>(userState, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/logout_user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserState> invalidateToken() throws AuthenticationException, IOException
	{
		SecurityContextHolder.clearContext();
		return new ResponseEntity<UserState>(HttpStatus.OK);
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> refreshAuthenticationToken(HttpServletRequest request) 
	{
		String token = tokenUtils.getToken(request);

		if (this.tokenUtils.canTokenBeRefreshed(token))
		{
			String refreshedToken = tokenUtils.refreshToken(token);
			return ResponseEntity.ok(refreshedToken);
		} 
		else
		{
			return ResponseEntity.badRequest().body(null);
		}
	}
}