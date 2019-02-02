package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.Administrator;
import org.isa.takeoff.model.User;
import org.isa.takeoff.repository.AdministratorRepository;
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
	
	@Autowired
	private AdministratorRepository administratorRepository;

	public User findOneUser(Long id) 
	{
		return this.userRepository.findById(id).get();
	}

	public List<User> findAllUser() 
	{
		return this.userRepository.findAll();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public User save(User user) 
	{
		return this.userRepository.save(user);
	}
	
	public User findByUsernameUser(String username) 
	{
		return this.userRepository.findByUsername(username);
	}
	
	public User findByEmailUser(String email) 
	{
		return this.userRepository.findByEmail(email);
	}

	public Administrator findOneAdmin(Long id) 
	{
		return this.administratorRepository.findById(id).get();
	}

	public List<Administrator> findAllAdmin() 
	{
		return this.administratorRepository.findAll();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Administrator save(Administrator administrator) 
	{
		return this.administratorRepository.save(administrator);
	}
	
	public Administrator findByUsernameAdmin(String username) 
	{
		return this.administratorRepository.findByUsername(username);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		User user = this.userRepository.findByUsername(username);
		if (user == null) 
		{
			Administrator administrator = this.administratorRepository.findByUsername(username);
			if (administrator == null) 
			{
				throw new UsernameNotFoundException("No user found with username '" + username + "'.");
			}
			else
			{
				return administrator;
			}
		}
		else
		{
			return user;
		}
	}
}