package org.egov.property.util;

import java.net.URI;

import org.egov.models.Property;
import org.egov.property.exception.InvalidPropertyBoundaryException;
import org.egov.property.model.BoundaryResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class PropertyValidator {
	
	@Autowired
	private Environment env;

	@Autowired
	RestTemplate restTemplate;
	
	public Boolean validatePropertyBoundary(Property property) throws InvalidPropertyBoundaryException{
		
		URI uri = UriComponentsBuilder.fromUriString(env.getProperty("boundary.boundaryUrl"))
				.queryParam("Boundary.tenantId", property.getBoundary().getTenantId())
				.queryParam("Boundary.id", property.getBoundary().getId()).build(true).encode().toUri();
		
		try {
			BoundaryResponseInfo boundaryResponseInfo = restTemplate.getForObject(uri, BoundaryResponseInfo.class);
			if(boundaryResponseInfo.getResponseInfo().getStatus().equalsIgnoreCase(env.getProperty("statusCode")) && boundaryResponseInfo.getBoundary().size()!=0){
				return true;
			} else {
				throw new InvalidPropertyBoundaryException();
			}
		} catch (HttpClientErrorException ex){
          throw new InvalidPropertyBoundaryException();
		}
	}
	
	public Boolean validateProperty(Property property){
		return validatePropertyBoundary(property);
	}
}
