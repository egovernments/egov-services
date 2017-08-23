package org.egov.property.repository;

import java.net.URI;

import org.egov.models.CalculationFactorResponse;
import org.egov.models.GuidanceValueResponse;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.Unit;
import org.egov.property.config.PropertiesManager;
import org.egov.property.exception.InvalidFactorValueException;
import org.egov.property.exception.InvalidGuidanceValueException;
import org.egov.property.exception.ValidationUrlNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
public class CalculatorRepository {

    private static final Logger logger = LoggerFactory.getLogger(BoundaryRepository.class);

    @Autowired
    PropertiesManager propertiesManager;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ResponseInfoFactory responseInfoFactory;

    public Boolean isGuidanceExists(String tenantId, Unit unit, RequestInfoWrapper requestInfoWrapper,
            String boundary) {
        String structure = unit.getStructure();
        String usage = unit.getUsage();
        String occupancy = unit.getOccupancyType();
        String validDate = unit.getOccupancyDate();

        StringBuilder CalculatorURI = new StringBuilder();
        CalculatorURI.append(propertiesManager.getCalculatorHostName())
                .append(propertiesManager.getCalculatorGuidanceSearchPath());

        URI uri = UriComponentsBuilder.fromUriString(CalculatorURI.toString()).queryParam("tenantId", tenantId)
                .queryParam("boundary", boundary).queryParam("structure", structure).queryParam("usage", usage)
                .queryParam("occupancy", occupancy).queryParam("validDate", validDate).build(true).encode().toUri();
        logger.info("CalculatorRepository CalculatorURI ---->> " + CalculatorURI.toString() + " \n request uri ---->> "
                + uri);
        try {
            GuidanceValueResponse guidanceValueResponse = restTemplate.postForObject(uri, requestInfoWrapper,
                    GuidanceValueResponse.class);
            logger.info("CalculatorRepository CalculatorURI ---->> " + guidanceValueResponse);

            if (guidanceValueResponse.getResponseInfo() != null
                    && guidanceValueResponse.getGuidanceValues().size() != 0) {
                return true;
            } else {
                throw new InvalidGuidanceValueException(propertiesManager.getInvalidGuidanceVal(),
                        propertiesManager.getInvalidGuidanceValMsg(), requestInfoWrapper.getRequestInfo());
            }

        } catch (HttpClientErrorException ex) {
            throw new ValidationUrlNotFoundException(propertiesManager.getInvalidGuidanceSearchValidationUrl(),
                    uri.toString(), requestInfoWrapper.getRequestInfo());
        }

    }

    public Boolean isFactorExists(String tenantId, String code, RequestInfoWrapper requestInfoWrapper, String validDate,
            String factorType) {

        StringBuilder CalculatorFactorURI = new StringBuilder();
        CalculatorFactorURI.append(propertiesManager.getCalculatorHostName())
                .append(propertiesManager.getCalculatorFactorSearchPath());

        URI uri = UriComponentsBuilder.fromUriString(CalculatorFactorURI.toString()).queryParam("tenantId", tenantId)
                .queryParam("code", code).queryParam("validDate", validDate).queryParam("factorType", factorType)
                .build(true).encode().toUri();
        logger.info("CalculatorRepository CalculatorFactorURI ---->> " + CalculatorFactorURI.toString()
                + " \n request uri ---->> " + uri);
        try {
            CalculationFactorResponse factorValueResponse = restTemplate.postForObject(uri, requestInfoWrapper,
                    CalculationFactorResponse.class);
            logger.info("CalculatorRepository CalculatorFactorURI ---->> " + factorValueResponse);

            if (factorValueResponse.getResponseInfo() != null
                    && factorValueResponse.getCalculationFactors().size() != 0) {
                return true;
            } else {
                throw new InvalidFactorValueException(propertiesManager.getInvalidFactorValue(),
                        propertiesManager.getInvalidFactorValueMsg(), requestInfoWrapper.getRequestInfo());
            }

        } catch (HttpClientErrorException ex) {
            throw new ValidationUrlNotFoundException(propertiesManager.getFactorsearchValidationUrl(),
                    uri.toString(), requestInfoWrapper.getRequestInfo());
        }
    }

    public CalculationFactorResponse getAge(String tenantId, String code, RequestInfoWrapper requestInfoWrapper, String validDate,
            String factorType) {

        StringBuilder CalculatorFactorURI = new StringBuilder();
        CalculatorFactorURI.append(propertiesManager.getCalculatorHostName())
                .append(propertiesManager.getCalculatorFactorSearchPath());

        URI uri = UriComponentsBuilder.fromUriString(CalculatorFactorURI.toString()).queryParam("tenantId", tenantId)
                .queryParam("code", code).queryParam("validDate", validDate).queryParam("factorType", factorType)
                .build(true).encode().toUri();
        logger.info("CalculatorRepository CalculatorFactorURI ---->> " + CalculatorFactorURI.toString()
                + " \n request uri ---->> " + uri);
        try {
            CalculationFactorResponse factorValueResponse = restTemplate.postForObject(uri, requestInfoWrapper,
                    CalculationFactorResponse.class);
            logger.info("CalculatorRepository CalculatorFactorURI ---->> " + factorValueResponse);
            CalculationFactorResponse calculationFactorResponse = new CalculationFactorResponse();

            if (factorValueResponse != null
                    && factorValueResponse.getCalculationFactors().size() != 0) {
                ResponseInfo responseInfo = responseInfoFactory
                        .createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true);
                calculationFactorResponse.setCalculationFactors(factorValueResponse.getCalculationFactors());
                calculationFactorResponse.setResponseInfo(responseInfo);
                return calculationFactorResponse;
            } else {
                throw new InvalidFactorValueException(propertiesManager.getPropertyUnitAge(),
                        propertiesManager.getPropertyUnitAge(), requestInfoWrapper.getRequestInfo());
            }

        } catch (HttpClientErrorException ex) {
            throw new ValidationUrlNotFoundException(propertiesManager.getFactorsearchValidationUrl(),
                    uri.toString(), requestInfoWrapper.getRequestInfo());
        }
    }
}
