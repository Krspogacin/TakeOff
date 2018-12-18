package org.isa.takeoff.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.isa.takeoff.model.User;
import org.isa.takeoff.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController 
{
	@Autowired
	private UserService userService;

	@RequestMapping(method = GET, value = "/user/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public User loadById(@PathVariable Long id)
	{
		return this.userService.findOne(id);
	}

	@RequestMapping(method = GET, value = "/user/all")
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> loadAll()
	{
		return this.userService.findAll();
	}
}