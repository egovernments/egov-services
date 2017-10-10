package org.egov.property.repository;

import java.net.URI;

import org.egov.models.Property;
import org.egov.models.RequestInfo;
import org.egov.property.config.PropertiesManager;
import org.egov.property.exception.InvalidPropertyBoundaryException;
import org.egov.property.exception.ValidationUrlNotFoundException;
import org.egov.property.model.BoundaryResponseInfo;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
public class BoundaryRepository {

    private static final Logger logger = LoggerFactory.getLogger(BoundaryRepository.class);

    @Autowired
    PropertiesManager propertiesManager;
     
    @Autowired
    RestTemplate restTemplate;
    
    public  Boolean isBoundaryExists(Property property,RequestInfo requestInfo, String code){
        //TODO all this logic to find/search boundary has to move to separate class say BoundaryRepository and just call the api/method here
        StringBuffer BoundaryURI = new StringBuffer();
        BoundaryURI.append(propertiesManager.getLocationHostName())
                        .append(propertiesManager.getLocationSearchpath());
        URI uri = UriComponentsBuilder.fromUriString(BoundaryURI.toString())
                        .queryParam("Boundary.tenantId", property.getTenantId()).queryParam("Boundary.code", code).build(true)
                        .encode().toUri();
        logger.info("BoundaryRepository BoundaryURI ---->> "+BoundaryURI.toString()+" \n request uri ---->> "+uri);
        try {
                BoundaryResponseInfo boundaryResponseInfo = restTemplate.getForObject(uri, BoundaryResponseInfo.class);
                logger.info("BoundaryRepository BoundaryResponseInfo ---->> "+boundaryResponseInfo);
                if (boundaryResponseInfo.getResponseInfo() != null && boundaryResponseInfo.getBoundary().size() != 0) {
                        return true;
                } else {
                        throw new InvalidPropertyBoundaryException(propertiesManager.getInvalidPropertyBoundary(),
                                        propertiesManager.getInvalidBoundaryMessage().replace("{boundaryId}", "" + code),
                                        requestInfo);
                }
        } catch (HttpClientErrorException ex) {
                throw new ValidationUrlNotFoundException(propertiesManager.getInvalidBoundaryValidationUrl(),
                                uri.toString(), requestInfo);

        }
    }

}
