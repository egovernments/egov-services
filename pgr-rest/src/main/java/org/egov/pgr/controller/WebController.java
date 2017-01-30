package org.egov.pgr.controller;

import org.egov.pgr.entity.Customer;
import org.egov.pgr.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
	@Autowired
	private CustomerRepository customerRepository;
	
	@RequestMapping(value = "/save", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Customer save(){
		Customer customer =  customerRepository.save(new Customer("raghu", "kumar"));
		System.out.println(customer.getFirstName()); 
		return customer;
	}
}