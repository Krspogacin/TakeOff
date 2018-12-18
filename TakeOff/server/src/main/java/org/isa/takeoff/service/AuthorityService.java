package org.isa.takeoff.service;

import org.isa.takeoff.model.Authority;
import org.isa.takeoff.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService 
{
	@Autowired
	private AuthorityRepository authorityRepository;
	
	public Authority findByName(String name)
	{
		return authorityRepository.findByName(name);
	}
}