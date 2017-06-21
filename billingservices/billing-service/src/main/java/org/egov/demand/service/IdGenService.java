package org.egov.demand.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IdGenService {

	@Autowired
	private RestTemplate restTemplate;
	
	public List<String> getIds(Integer numOfIds){
		List<String> list = new ArrayList<>();
		return list;
	}
}
