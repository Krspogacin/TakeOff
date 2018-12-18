package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.User;
import org.isa.takeoff.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService 
{
	@Autowired
	private UserRepository userRepository;

	public User findOne(Long id) throws AccessDeniedException 
	{
		User u = userRepository.findById(id).get();
		return u;
	}

	public List<User> findAll() throws AccessDeniedException 
	{
		List<User> result = userRepository.findAll();
		return result;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		User user = userRepository.findByUsername(username);
		if (user == null) 
		{
			throw new UsernameNotFoundException("No user found with username '" + username + "'.");
		}
		else
		{
			return user;
		}
	}
}