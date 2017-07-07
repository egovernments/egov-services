package org.egov.property.repository;

import java.net.URI;

import org.egov.models.Property;
import org.egov.models.RequestInfo;
import org.egov.property.exception.InvalidPropertyBoundaryException;
import org.egov.property.exception.ValidationUrlNotFoundException;
import org.egov.property.model.BoundaryResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
public class BoundaryRepository {
    @Autowired
    Environment env;
     
    @Autowired
    RestTemplate restTemplate;
    
    public  Boolean isBoundaryExists(Property property,RequestInfo requestInfo,Long id){
        //TODO all this logic to find/search boundary has to move to separate class say BoundaryRepository and just call the api/method here
        StringBuffer BoundaryURI = new StringBuffer();
        BoundaryURI.append(env.getProperty("egov.services.boundary_service.hostname"))
                        .append(env.getProperty("egov.services.boundary_service.searchpath"));
        URI uri = UriComponentsBuilder.fromUriString(BoundaryURI.toString())
                        .queryParam("Boundary.tenantId", property.getTenantId()).queryParam("Boundary.id", id).build(true)
                        .encode().toUri();

        try {
                BoundaryResponseInfo boundaryResponseInfo = restTemplate.getForObject(uri, BoundaryResponseInfo.class);
                if (boundaryResponseInfo.getResponseInfo() != null && boundaryResponseInfo.getBoundary().size() != 0) {
                        return true;
                } else {
                        throw new InvalidPropertyBoundaryException(env.getProperty("invalid.property.boundary"),
                                        env.getProperty("invalid.property.boundary.message").replace("{boundaryId}", "" + id),
                                        requestInfo);
                }
        } catch (HttpClientErrorException ex) {
                throw new ValidationUrlNotFoundException(env.getProperty("invalid.property.boundary.validation.url"),
                                uri.toString(), requestInfo);

        }
    }

}
