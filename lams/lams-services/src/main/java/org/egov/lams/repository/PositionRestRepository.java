package org.egov.lams.repository;

import org.egov.lams.web.contract.PositionResponse;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionRestRepository {
    public static final Logger logger = LoggerFactory.getLogger(PositionRestRepository.class);

    private final RestTemplate restTemplate;
    private String hostName;
    private String searchPath;
    private String pathVariable;


    public PositionRestRepository(RestTemplate restTemplate,
                                  @Value("${egov.services.employee_service.hostname}") String hostName,
                                  @Value("${egov.services.employee_service.searchpath}") String searchPath,
                                  @Value("${egov.services.employee_service.searchpath.pathvariable}") String pathVariable) {
        this.restTemplate = restTemplate;
        this.hostName = hostName;
        this.searchPath = searchPath;
        this.pathVariable = pathVariable;
    }

    public PositionResponse getPositions(String allotteeId, String tenantId, RequestInfoWrapper requestInfoWrapper){

        String positionUrl = hostName + searchPath.replace(pathVariable, allotteeId)
                + "?tenantId=" + tenantId;
        logger.info("the request url to position get call :: " + positionUrl);
        logger.info("the request body to position get call :: " + requestInfoWrapper);

        try {
            PositionResponse positionResponse = restTemplate.postForObject(positionUrl, requestInfoWrapper, PositionResponse.class);
            logger.info("the response form position get call :: " + positionResponse);

            return positionResponse;
        } catch (Exception e) {
            logger.info("the exception from poisition search :: " + e);
            throw e;
        }
    }
}
