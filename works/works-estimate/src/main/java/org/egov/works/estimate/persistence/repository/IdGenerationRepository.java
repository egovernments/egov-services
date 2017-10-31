package org.egov.works.estimate.persistence.repository;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.works.estimate.config.WorksEstimateServiceConstants;
import org.egov.works.estimate.domain.exception.ValidationException;
import org.egov.works.estimate.web.contract.factory.RequestInfoWrapper;
import org.egov.works.estimate.web.model.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class IdGenerationRepository {

    @Autowired
    private RestTemplate restTemplate;

    private String url;

    public IdGenerationRepository(final RestTemplate restTemplate,@Value("{egov.idgen.hostname}") final String idGenHostName,
                                  @Value("{works.numbergeneration.uri}") final String url) {
        this.restTemplate = restTemplate;
        this.url = url + idGenHostName;
    }

    public String generateAbstractEstimateNumber(final String tenantId, final RequestInfo requestInfo) {
        Object response = null;
        RequestInfoWrapper idRequestWrapper = new RequestInfoWrapper();
        idRequestWrapper.setRequestInfo(requestInfo);
     try {
        response = restTemplate.postForObject(url,
                idRequestWrapper, Object.class);
    } catch (Exception e) {
        throw new ValidationException(null,
                WorksEstimateServiceConstants.ABSTRACT_ESTIMATE_NUMBER_GENERATION_ERROR, WorksEstimateServiceConstants.ABSTRACT_ESTIMATE_NUMBER_GENERATION_ERROR);

    }
    log.info("Response from id gen service: " + response.toString());

    return JsonPath.read(response, "$.idResponses[0].id");
    }
}
