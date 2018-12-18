package org.isa.takeoff.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.isa.takeoff.model.User;
import org.isa.takeoff.security.AuthenticationRequest;
import org.isa.takeoff.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController 
{
	@Autowired
	TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws AuthenticationException, IOException
	{
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
																			authenticationRequest.getUsername(),
																			authenticationRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		User user = (User) authentication.getPrincipal();
		String token = tokenUtils.generateToken(user.getUsername());
		return ResponseEntity.ok(token);
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
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