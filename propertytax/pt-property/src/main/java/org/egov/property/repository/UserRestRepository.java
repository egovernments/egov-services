package org.egov.property.repository;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.egov.property.config.PropertiesManager;
import org.egov.property.model.UserCreateRequest;
import org.egov.property.model.UserCreateResponse;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class UserRestRepository {

	@Autowired
	private LogAwareRestTemplate restTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	public UserCreateResponse createUser(UserCreateRequest request) throws Exception {
		String url = propertiesManager.getUserHostname() + propertiesManager.getUserBasepath()
				+ propertiesManager.getUserCreatepath();
		return restTemplate.postForObject(url, request, UserCreateResponse.class);
	}

	public UserCreateResponse updateUser(UserCreateRequest request) throws Exception {
		String url = propertiesManager.getUserHostname() + propertiesManager.getUserBasepath()
				+ propertiesManager.getUserUpdatepath();
		Map<String, Long> uriParams = new HashMap<String, Long>();
		uriParams.put("id", request.getUser().getId());
		URI uri = UriComponentsBuilder.fromHttpUrl(url).build().expand(uriParams).toUri();
		return restTemplate.postForObject(uri, request, UserCreateResponse.class);
	}
}
