package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.Administrator;
import org.isa.takeoff.model.User;
import org.isa.takeoff.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdministratorService implements UserDetailsService 
{
	@Autowired
	private AdministratorRepository administratorRepository;

	public Administrator findOne(Long id) 
	{
		return this.administratorRepository.findById(id).get();
	}

	public List<Administrator> findAll() 
	{
		return this.administratorRepository.findAll();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Administrator save(Administrator administrator) 
	{
		return this.administratorRepository.save(administrator);
	}
	
	public Administrator findByUsername(String username) 
	{
		return this.administratorRepository.findByUsername(username);
	}
	
	public Administrator findByEmail(String email) 
	{
		return this.administratorRepository.findByEmail(email);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		Administrator administrator = this.administratorRepository.findByUsername(username);
		if (administrator == null) 
		{
			throw new UsernameNotFoundException("No administrator found with username '" + username + "'.");
		}
		else
		{
			return administrator;
		}
	}
}