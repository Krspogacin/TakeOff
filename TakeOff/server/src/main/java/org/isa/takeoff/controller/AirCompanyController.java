package org.isa.takeoff.controller;

import java.util.ArrayList;
import java.util.List;

import org.isa.takeoff.dto.AirCompanyDTO;
import org.isa.takeoff.model.AirCompany;
import org.isa.takeoff.service.AirCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AirCompanyController {

	@Autowired
	private AirCompanyService airCompanyService;

	@RequestMapping(value = "/companies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AirCompanyDTO>> getCompanies() {

		List<AirCompany> companies = airCompanyService.findAll();

		// convert companies to DTOs
		List<AirCompanyDTO> companiesDTO = new ArrayList<>();
		for (AirCompany company : companies) {
			companiesDTO.add(new AirCompanyDTO(company));
		}

		return new ResponseEntity<>(companiesDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/companies/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AirCompanyDTO> getCompany(@PathVariable Long id){
		
		try {
			AirCompany company = airCompanyService.findOne(id);
			AirCompanyDTO companyDTO = new AirCompanyDTO(company);
			
			return new ResponseEntity<>(companyDTO, HttpStatus.OK);
			
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
