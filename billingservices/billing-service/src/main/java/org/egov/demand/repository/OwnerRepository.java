package org.egov.demand.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.model.Owner;
import org.egov.demand.web.contract.User;
import org.egov.demand.web.contract.UserResponse;
import org.egov.demand.web.contract.UserSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class OwnerRepository {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ApplicationProperties applicationProperties;

	public List<Owner> getOwners(UserSearchRequest userSearchRequest) {

		String url = applicationProperties.getUserServiceHostName() + applicationProperties.getUserServiceSearchPath();
		UserResponse userResponse = null;
		try {
			userResponse = restTemplate.postForObject(url, userSearchRequest, UserResponse.class);
		} catch (HttpClientErrorException e) {
			log.error("Following exception occurred: " + e.getResponseBodyAsString());
			throw e;
		}
		List<Owner> owners = new ArrayList<>();
		for (User userRequest : userResponse.getUser()) {
			owners.add(userRequest.toOwner());
		}
		return owners;
	}
}
