package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.User;
import org.isa.takeoff.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService 
{
	@Autowired
	private UserRepository userRepository;

	public User findOne(Long id) 
	{
		return this.userRepository.findById(id).get();
	}

	public List<User> findAll() 
	{
		return this.userRepository.findAll();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public User save(User user) 
	{
		return this.userRepository.save(user);
	}
	
	public User findByUsername(String username) 
	{
		return this.userRepository.findByUsername(username);
	}
	
	public User findByEmail(String email) 
	{
		return this.userRepository.findByEmail(email);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		User user = this.userRepository.findByUsername(username);
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