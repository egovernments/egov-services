package org.egov.property.repository;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.models.CalculationFactorResponse;
import org.egov.models.CalculationRequest;
import org.egov.models.CalculationResponse;
import org.egov.models.GuidanceValueResponse;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.TaxPeriodResponse;
import org.egov.models.TaxRatesResponse;
import org.egov.models.TransferFeeCal;
import org.egov.models.TransferFeeCalRequest;
import org.egov.models.TransferFeeCalResponse;
import org.egov.models.Unit;
import org.egov.models.demand.TaxHeadMasterResponse;
import org.egov.property.config.PropertiesManager;
import org.egov.property.exception.InvalidCodeException;
import org.egov.property.exception.InvalidFactorValueException;
import org.egov.property.exception.InvalidGuidanceValueException;
import org.egov.property.exception.ValidationUrlNotFoundException;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class CalculatorRepository {

	private static final Logger logger = LoggerFactory.getLogger(BoundaryRepository.class);

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	LogAwareRestTemplate restTemplate;

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
			throw new ValidationUrlNotFoundException(propertiesManager.getFactorsearchValidationUrl(), uri.toString(),
					requestInfoWrapper.getRequestInfo());
		}
	}

	public CalculationFactorResponse getAge(String tenantId, String code, RequestInfoWrapper requestInfoWrapper,
			String validDate, String factorType) {

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

			if (factorValueResponse != null && factorValueResponse.getCalculationFactors().size() != 0) {
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
			throw new ValidationUrlNotFoundException(propertiesManager.getFactorsearchValidationUrl(), uri.toString(),
					requestInfoWrapper.getRequestInfo());
		}
	}

	public Double getFeeFactorRates(TransferFeeCal transferFeeCal, RequestInfo requestInfo) {

		TransferFeeCalRequest transferFeeCalRequest = new TransferFeeCalRequest();
		List<TransferFeeCal> transferFeeCals = new ArrayList<TransferFeeCal>();
		transferFeeCals.add(transferFeeCal);

		transferFeeCalRequest.setRequestInfo(requestInfo);
		transferFeeCalRequest.setTransferFeeCals(transferFeeCals);

		StringBuilder TransferFeeCalURI = new StringBuilder();
		TransferFeeCalURI.append(propertiesManager.getCalculatorHostName())
				.append(propertiesManager.getTitleTransferCalculateSearchPath());

		URI uri = UriComponentsBuilder.fromUriString(TransferFeeCalURI.toString()).build(true).encode().toUri();
		logger.info("CalculatorRepository   TransferFeeCalURI---->> " + TransferFeeCalURI.toString()
				+ " \n request uri ---->> " + uri);
		try {
			TransferFeeCalResponse transferFeeCalResponse = restTemplate.postForObject(uri, transferFeeCalRequest,
					TransferFeeCalResponse.class);
			logger.info("CalculatorRepository TransferFeeCalURI ---->> " + transferFeeCalResponse);
			if (transferFeeCalResponse != null && transferFeeCalResponse.getTransferFeeCals().size() != 0) {
				return transferFeeCalResponse.getTransferFeeCals().get(0).getTransferFee();
			} else {
				throw new InvalidFactorValueException(propertiesManager.getTitleTransferTaxCalculate(),
						propertiesManager.getTitleTransferTaxCalculate(), requestInfo);
			}

		} catch (HttpClientErrorException ex) {
			throw new ValidationUrlNotFoundException(propertiesManager.getTitleTransferCalculateSearchPath(),
					uri.toString(), requestInfo);
		}
	}

	public TaxRatesResponse getTaxRates(String tenantId, String occupancyDate, RequestInfo requestInfo) {
		StringBuilder CalculatorURI = new StringBuilder();
		CalculatorURI.append(propertiesManager.getCalculatorHostName()).append(propertiesManager.getTaxRates());

		URI uri = UriComponentsBuilder.fromUriString(CalculatorURI.toString()).queryParam("tenantId", tenantId)
				.queryParam("validDate", occupancyDate).build(true).encode().toUri();
		logger.info("CalculatorRepository taxRates url ---->> " + CalculatorURI.toString() + " \n request uri ---->> "
				+ uri);
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		TaxRatesResponse taxRatesResponse = null;
		try {
			taxRatesResponse = restTemplate.postForObject(uri, requestInfoWrapper, TaxRatesResponse.class);
			logger.info("tax rates response: ---->> " + taxRatesResponse);
		} catch (Exception ex) {
			throw new InvalidCodeException(propertiesManager.getInvalidTaxRates(), requestInfo);
		}

		if (taxRatesResponse != null && taxRatesResponse.getTaxRates() != null) {
			if (!(taxRatesResponse.getTaxRates().size() > 0)) {
				throw new InvalidCodeException(propertiesManager.getInvalidTaxRates(), requestInfo);
			}
		} else {
			throw new InvalidCodeException(propertiesManager.getInvalidTaxRates(), requestInfo);
		}
		return taxRatesResponse;
	}

	public TaxPeriodResponse getTaxPeriods(String tenantId, String occupancyDate, RequestInfo requestInfo) {
		StringBuilder CalculatorURI = new StringBuilder();
		CalculatorURI.append(propertiesManager.getCalculatorHostName()).append(propertiesManager.getTaxPeriods());

		URI uri = UriComponentsBuilder.fromUriString(CalculatorURI.toString()).queryParam("tenantId", tenantId)
				.queryParam("validDate", occupancyDate).build(true).encode().toUri();
		logger.info("CalculatorRepository taxPeriods url ---->> " + CalculatorURI.toString() + " \n request uri ---->> "
				+ uri);
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		TaxPeriodResponse taxPeriodsResponse = null;
		try {
			taxPeriodsResponse = restTemplate.postForObject(uri, requestInfoWrapper, TaxPeriodResponse.class);
			logger.info("tax period response: ---->> " + taxPeriodsResponse);
		} catch (Exception ex) {
			throw new InvalidCodeException(propertiesManager.getInvalidTaxPeriods(), requestInfo);
		}

		if (taxPeriodsResponse != null && taxPeriodsResponse.getTaxPeriods() != null) {

			if (!(taxPeriodsResponse.getTaxPeriods().size() > 0)) {
				throw new InvalidCodeException(propertiesManager.getInvalidTaxPeriods(), requestInfo);
			}
		} else {
			throw new InvalidCodeException(propertiesManager.getInvalidTaxPeriods(), requestInfo);
		}
		return taxPeriodsResponse;
	}

	/**
	 * calculating tax
	 * 
	 * @param calculationRequest
	 * @return CalculationResponse
	 */
	public CalculationResponse getCalculation(CalculationRequest calculationRequest) {

		String url = propertiesManager.getCalculatorHostName() + propertiesManager.getCalculatorPath();
		logger.info("Calculator url is:" + url + "CalculationRequest is:" + calculationRequest);
		CalculationResponse calculationResponse = null;
		try {
			calculationResponse = restTemplate.postForObject(url, calculationRequest, CalculationResponse.class);
			logger.info("CalculationResponse is:" + calculationResponse);

		} catch (HttpClientErrorException ex) {
			throw new ValidationUrlNotFoundException(propertiesManager.getInvalidTaxCalculation(), url,
					calculationRequest.getRequestInfo());

		}

		return calculationResponse;
	}

	/**
	 * Fetches the TaxPeriods from pt-calculator service for the given occupancy
	 * date, till the current date
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param occupancyDateStr
	 * @return TaxPeriodResponse
	 */
	public TaxPeriodResponse getTaxPeriodsForOccupancyDateRepository(RequestInfo requestInfo, String tenantId,
			String occupancyDateStr, SimpleDateFormat dateFormat) {
		StringBuffer taxPeriodSearchUrl = new StringBuffer();
		taxPeriodSearchUrl.append(propertiesManager.getCalculatorHostName());
		taxPeriodSearchUrl.append(propertiesManager.getCalculatorTaxperiodsSearch());

		String toDate = dateFormat.format(new Date());
		logger.info("getTaxPeriodsForOccupancyDate() ----------- fromDate ---->> " + occupancyDateStr
				+ " and toDate ---->> " + toDate);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(taxPeriodSearchUrl.toString())
				.queryParam("tenantId", tenantId).queryParam("fromDate", occupancyDateStr).queryParam("toDate", toDate);

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		logger.info("PropertyServiceImpl BuilderUri : " + builder.buildAndExpand().toUri()
				+ " \n RequestInfoWrapper  : " + requestInfoWrapper);
		TaxPeriodResponse taxPeriodResponse = null;
		try {
			taxPeriodResponse = restTemplate.postForObject(builder.buildAndExpand().toUri(), requestInfoWrapper,
					TaxPeriodResponse.class);
		} catch (HttpClientErrorException ex) {
			throw new ValidationUrlNotFoundException(propertiesManager.getInvalidTaxPeriods(),
					taxPeriodSearchUrl.toString(), requestInfo);

		}
		return taxPeriodResponse;
	}

	/**
	 * API fetches TaxHeadMasters from the Billing Service for the occupancy
	 * date
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param occupancyDate
	 * @return TaxHeadMasterResponse
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public TaxHeadMasterResponse getTaxHeadMastersRepository(RequestInfo requestInfo, String tenantId,
			Date occupancyDate) throws JsonParseException, JsonMappingException, IOException {
		TaxHeadMasterResponse taxHeadResponse = null;
		StringBuffer taxHeadsUrl = new StringBuffer();
		taxHeadsUrl.append(propertiesManager.getBillingServiceHostname());
		taxHeadsUrl.append(propertiesManager.getBillingServiceSearchTaxHeads());

		URI uri = UriComponentsBuilder.fromUriString(taxHeadsUrl.toString()).queryParam("tenantId", tenantId)
				.queryParam("service", "PT").queryParam("validFrom", occupancyDate.getTime())
				.queryParam("validTill", new Date().getTime()).build(true).encode().toUri();

		logger.info("getTaxHeadMasters taxheads url --> " + uri + " taxheads request --> " + requestInfo);
		try {
			String taxHeadsResponseStr = restTemplate.postForObject(uri, requestInfo, String.class);
			logger.info("getTaxHeadMasters taxheads response string is --> " + taxHeadsResponseStr);
			if (!taxHeadsResponseStr.isEmpty()) {
				ObjectMapper objectMapper = new ObjectMapper();
				taxHeadResponse = objectMapper.readValue(taxHeadsResponseStr, TaxHeadMasterResponse.class);

			} else {
				throw new InvalidCodeException(propertiesManager.getInvalidTaxHead(), requestInfo);
			}
		} catch (HttpClientErrorException ex) {
			throw new ValidationUrlNotFoundException(propertiesManager.getInvalidTaxPeriods(), taxHeadsUrl.toString(),
					requestInfo);
		}

		return taxHeadResponse;
	}

}
