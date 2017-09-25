package org.egov.propertytax.repository;

import java.math.BigDecimal;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.egov.models.CommonTaxDetails;
import org.egov.models.Demand;
import org.egov.models.DemandDetail;
import org.egov.models.DemandRequest;
import org.egov.models.DemandResponse;
import org.egov.models.HeadWiseTax;
import org.egov.models.Owner;
import org.egov.models.Property;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.TaxCalculation;
import org.egov.models.TaxPeriod;
import org.egov.models.TaxPeriodResponse;
import org.egov.models.TitleTransfer;
import org.egov.models.TitleTransferRequest;
import org.egov.propertytax.config.PropertiesManager;
import org.egov.propertytax.exception.InvalidInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class BillingServiceRepository {

	private static final Logger logger = LoggerFactory.getLogger(BillingServiceRepository.class);

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	private RestTemplate restTemplate;

	public List<Demand> prepareDemand(List<TaxCalculation> taxCalculationList, Property property) {
		List<Demand> demandList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date fromDate;
		Date toDate;
		CommonTaxDetails taxDetails;
		Demand demand;
		DemandDetail demandDetail;
		String tenantId = property.getTenantId();
		String propertyType = property.getPropertyDetail().getPropertyType();
		List<DemandDetail> demandDetailsList;
		for (TaxCalculation taxCalculation : taxCalculationList) {
			taxDetails = taxCalculation.getPropertyTaxes();
			demand = new Demand();
			demand.setTenantId(tenantId);
			demand.setBusinessService(propertiesManager.getDemandBusinessService());
			demand.setConsumerType(propertyType);
			demand.setConsumerCode(property.getPropertyDetail().getApplicationNo());
			demand.setMinimumAmountPayable(BigDecimal.ONE);
			demandDetailsList = new ArrayList<>();
			for (HeadWiseTax taxes : taxDetails.getHeadWiseTaxes()) {
				demandDetail = new DemandDetail();
				demandDetail.setTaxHeadMasterCode(taxes.getTaxName());
				demandDetail.setTaxAmount(BigDecimal.valueOf(taxes.getTaxValue()));
				demandDetail.setTenantId(tenantId);
				demandDetailsList.add(demandDetail);
			}
			demand.setDemandDetails(demandDetailsList);
			try {
				logger.info("TaxCalculation fromDate = " + taxCalculation.getFromDate() + " \n toDate = "
						+ taxCalculation.getToDate());
				fromDate = sdf.parse(taxCalculation.getFromDate());
				toDate = sdf.parse(taxCalculation.getToDate());
				logger.info(" Dates, fromDate = " + fromDate + ", toDate = " + toDate + " \n Epoch values, fromDate = "
						+ fromDate.getTime() + " \n toDate = " + toDate.getTime());
				demand.setTaxPeriodFrom(fromDate.getTime());
				demand.setTaxPeriodTo(toDate.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Owner owner = new Owner();
			owner.setId(property.getOwners().get(0).getId());
			demand.setOwner(owner);
			demandList.add(demand);
		}
		return demandList;
	}

	public DemandResponse createDemand(List<Demand> demands, RequestInfo requestInfo) {
		DemandRequest demandRequest = new DemandRequest();
		demandRequest.setRequestInfo(requestInfo);
		demandRequest.setDemands(demands);

		logger.info("BillingServiceRepository createDemand(), demands --> " + demands);

		String url = propertiesManager.getBillingServiceHostName() + propertiesManager.getBillingServiceCreatedDemand();
		logger.info(
				"BillingServiceRepository createDemand(), URL - > " + url + " \n demandRequest --> " + demandRequest);
		Gson gson = new Gson();
		System.out.println(gson.toJson(demandRequest));
		DemandResponse resposne = null;
		resposne = restTemplate.postForObject(url, demandRequest, DemandResponse.class);

		return resposne;
	}

	/**
	 * current tax period for demands - title transfer
	 * 
	 * @param tenantId
	 * @param requestInfoWrapper
	 * @return
	 */
	public TaxPeriodResponse getCurrentTaxPeriod(String tenantId, RequestInfoWrapper requestInfoWrapper) {
		StringBuffer taxPeriodSearchUrl = new StringBuffer();
		taxPeriodSearchUrl.append(propertiesManager.getCalculatorHostName());
		taxPeriodSearchUrl.append(propertiesManager.getCalculatorTaxperiodsSearch());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(propertiesManager.getDefaultDateFormat());
		String currentDate = simpleDateFormat.format(new Date());
		logger.info("getTaxPeriodsForCurrentDate : " + currentDate);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(taxPeriodSearchUrl.toString())
				.queryParam("tenantId", tenantId).queryParam("validDate", currentDate);

		logger.info("taxperiod BuilderUri : " + builder.buildAndExpand().toUri() + " \n RequestInfoWrapper  : "
				+ requestInfoWrapper);
		RestTemplate restTemplate = new RestTemplate();
		TaxPeriodResponse taxPeriodResponse = restTemplate.postForObject(builder.buildAndExpand().toUri(),
				requestInfoWrapper, TaxPeriodResponse.class);
		logger.info("taxperiod response : " + taxPeriodResponse);
		;
		return taxPeriodResponse;
	}

	/**
	 * preparing demands for title transfer
	 * 
	 * @param titleTransferRequest
	 * @return
	 * @throws Exception
	 */
	public List<Demand> prepareDemandForTitleTransfer(TitleTransferRequest titleTransferRequest) throws Exception {
		List<Demand> demandList;
		String tenantId = titleTransferRequest.getTitleTransfer().getTenantId();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(titleTransferRequest.getRequestInfo());
		TaxPeriodResponse taxPeriodResponse = getCurrentTaxPeriod(tenantId, requestInfoWrapper);
		if (taxPeriodResponse != null && taxPeriodResponse.getTaxPeriods().size() > 0) {
			TaxPeriod taxPeriod = taxPeriodResponse.getTaxPeriods().get(0);
			demandList = new ArrayList<>();
			Demand demand = new Demand();
			SimpleDateFormat sdf = new SimpleDateFormat(propertiesManager.getDbDateFormat());

			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

			Date demandFromDate = sdf.parse(taxPeriod.getFromDate());
			long demandFromTime = demandFromDate.getTime();
			Date demandTodate = sdf.parse(taxPeriod.getToDate());
			long demandToTime = demandTodate.getTime();
			demand.setBusinessService(propertiesManager.getTitleTransferBusinessService());
			demand.setConsumerCode(titleTransferRequest.getTitleTransfer().getUpicNo());
			demand.setConsumerType(propertiesManager.getTitleTransfer());
			demand.setMinimumAmountPayable(BigDecimal.ONE);
			demand.setTaxPeriodFrom(demandFromTime);
			demand.setTaxPeriodTo(demandToTime);
			demand.setTenantId(tenantId);
			List<DemandDetail> demandDetailsList = new ArrayList<DemandDetail>();
			DemandDetail demandDetail = new DemandDetail();
			demandDetail.setTenantId(tenantId);
			demandDetail.setTaxAmount(BigDecimal.valueOf(titleTransferRequest.getTitleTransfer().getTitleTrasferFee()));
			demandDetail.setTaxHeadMasterCode(propertiesManager.getTitleTransferTaxhead());
			demandDetailsList.add(demandDetail);
			demand.setDemandDetails(demandDetailsList);

			Owner owner = new Owner();
			owner.setId(titleTransferRequest.getTitleTransfer().getNewOwners().get(0).getId());
			demand.setOwner(owner);
			demandList.add(demand);
		} else {
			throw new InvalidInputException(propertiesManager.getInvalidInput(), titleTransferRequest.getRequestInfo());
		}
		return demandList;
	}

	/**
	 * Getting demands for property
	 * 
	 * @param id
	 * @param tenantId
	 * @param requestInfo
	 * @return
	 * @throws Exception
	 */
	public DemandResponse getDemands(String id, String tenantId, RequestInfoWrapper requestInfo) throws Exception {
		final RestTemplate restTemplate = new RestTemplate();
		DemandResponse resonse = null;
		final StringBuffer demandUrl = new StringBuffer();
		demandUrl.append(propertiesManager.getBillingServiceHostName());
		demandUrl.append(propertiesManager.getDemandSearchPath());
		final MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<String, String>();
		requestMap.add("tenantId", tenantId);
		requestMap.add("id", id);
		requestMap.add("businessService", propertiesManager.getTitleTransferBusinessService());

		final URI uri = UriComponentsBuilder.fromHttpUrl(demandUrl.toString()).queryParams(requestMap).build().encode()
				.toUri();
		log.info("Get demand url is " + uri + " demand request is : " + requestInfo);
		try {
			final String demandResponse = restTemplate.postForObject(uri, requestInfo, String.class);
			log.info("Get demand response is :" + demandResponse);
			if (demandResponse != null && demandResponse.contains("Demands")) {
				final ObjectMapper objectMapper = new ObjectMapper();
				resonse = objectMapper.readValue(demandResponse, DemandResponse.class);
			}
			return resonse;
		} catch (final HttpClientErrorException exception) {
			exception.printStackTrace();
			throw new InvalidInputException(propertiesManager.getInvalidDemand(), requestInfo.getRequestInfo());
		}
	}

	/**
	 * Updating titleTransfer demands
	 * 
	 * @param transferRequest
	 * @throws Exception
	 */
	public void updateDemandForTittleTransfer(TitleTransferRequest transferRequest) throws Exception {
		TitleTransfer titleTransfer = transferRequest.getTitleTransfer();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(transferRequest.getRequestInfo());
		DemandResponse demandResponse = getDemands(titleTransfer.getDemandId(), titleTransfer.getTenantId(),
				requestInfoWrapper);

		Demand demand = demandResponse.getDemands().get(0);
		demand.getDemandDetails().get(0).setTaxAmount(BigDecimal.valueOf(titleTransfer.getTitleTrasferFee()));
		demand.getDemandDetails().get(0).getAuditDetail()
				.setLastModifiedBy(titleTransfer.getAuditDetails().getLastModifiedBy());
		demand.getDemandDetails().get(0).getAuditDetail().setLastModifiedTime(new Date().getTime());
		demand.getAuditDetail().setLastModifiedBy(titleTransfer.getAuditDetails().getLastModifiedBy());
		demand.getAuditDetail().setLastModifiedTime(new Date().getTime());
		StringBuffer demandUpdateUrl = new StringBuffer();
		demandUpdateUrl.append(propertiesManager.getBillingServiceHostName());
		demandUpdateUrl.append(propertiesManager.getDemandUpdatePath());
		DemandRequest demandRequest = new DemandRequest();
		List<Demand> demandList = new ArrayList<Demand>();
		demandList.add(demand);
		demandRequest.setDemands(demandList);
		demandRequest.setRequestInfo(transferRequest.getRequestInfo());
		try {
			DemandResponse demandUpdateResponse = restTemplate.postForObject(demandUpdateUrl.toString(), demandRequest,
					DemandResponse.class);
			if (demandUpdateResponse == null || demandUpdateResponse.getDemands().size() == 0) {
				throw new InvalidInputException(propertiesManager.getInvalidUpdateDemand(),
						transferRequest.getRequestInfo());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InvalidInputException(propertiesManager.getInvalidUpdateDemand(),
					transferRequest.getRequestInfo());
		}
	}

}
