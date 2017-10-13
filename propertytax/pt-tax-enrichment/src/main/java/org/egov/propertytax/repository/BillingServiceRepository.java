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
import org.egov.propertytax.repository.builder.DemandBuilder;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
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
	private LogAwareRestTemplate restTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;

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
		Gson gson = new Gson();
		logger.info(gson.toJson(demandRequest));
		String url = propertiesManager.getBillingServiceHostName() + propertiesManager.getBillingServiceCreatedDemand();
		logger.info(
				"BillingServiceRepository createDemand(), URL - > " + url + " \n demandRequest --> " + demandRequest);

		restTemplate.postForObject(url, demandRequest, DemandResponse.class);
	}

	/**
	 * This will update the existing demand collection and tax amounts for the
	 * modification in the property
	 * 
	 * @param calculationList
	 * @param property
	 * @param requestInfo
	 * @return {@link DemandResponse}
	 */
	public DemandResponse updateDemand(List<TaxCalculation> taxCalculationList, Property property,
			RequestInfo requestInfo,Boolean isModify) throws Exception {

		Boolean isTaxIncreased = checkTaxdiffrecnce(property);
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();

		requestInfoWrapper.setRequestInfo(requestInfo);
		DemandResponse demandResposne = null;
		
		String consumerCode = "";
		
		if ( property.getPropertyDetail().getWorkFlowDetails().getAction().equalsIgnoreCase(propertiesManager.getSpecialNoticeAction()) || isModify ){
			consumerCode = property.getUpicNumber();
		}
		else{
			consumerCode = property.getPropertyDetail().getApplicationNo();
		}

		demandResposne = getDemandsByConsumerCode(consumerCode, property.getTenantId(), requestInfoWrapper);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date fromDate;
		Date toDate;
		if (isTaxIncreased) {

			for (Demand demand : demandResposne.getDemands()) {
				CommonTaxDetails taxDetails;
				for (TaxCalculation taxCalculation : taxCalculationList) {
					taxDetails = taxCalculation.getPropertyTaxes();

					fromDate = sdf.parse(taxCalculation.getFromDate());
					toDate = sdf.parse(taxCalculation.getToDate());
					if (fromDate.getTime() == demand.getTaxPeriodFrom()
							&& toDate.getTime() == demand.getTaxPeriodTo()) {
						for (HeadWiseTax headWiseTax : taxDetails.getHeadWiseTaxes()) {
							for (DemandDetail demandDetail : demand.getDemandDetails()) {
								if (demandDetail.getTaxHeadMasterCode().equalsIgnoreCase(headWiseTax.getTaxName())
										&& demandDetail.getTaxAmount().doubleValue() != headWiseTax.getTaxValue()) {
									demandDetail.setTaxAmount(new BigDecimal(headWiseTax.getTaxValue()));
									demandDetail.setCollectionAmount(BigDecimal.valueOf(headWiseTax.getTaxValue()));
									break;

								}
							}
						}
						break;
					}
				}
			}
		}

		// tax is decreased
		else if (!isTaxIncreased) {
			demandResposne.getDemands()
					.sort((demand1, demand2) -> demand1.getTaxPeriodFrom().compareTo(demand2.getTaxPeriodFrom()));
			Demand latestDemand = demandResposne.getDemands().get(demandResposne.getDemands().size() - 1);
			Double newTotalAmountTobePaid = 0.0d;
			Double prevoiusTotalCollection = 0.d;
			for (Demand demand : demandResposne.getDemands()) {

				CommonTaxDetails taxDetails;

				for (TaxCalculation taxCalculation : taxCalculationList) {
					taxDetails = taxCalculation.getPropertyTaxes();
					fromDate = sdf.parse(taxCalculation.getFromDate());
					toDate = sdf.parse(taxCalculation.getToDate());
					if (fromDate.getTime() == demand.getTaxPeriodFrom()
							&& toDate.getTime() == demand.getTaxPeriodTo()) {

						for (HeadWiseTax headWiseTax : taxDetails.getHeadWiseTaxes()) {
							for (DemandDetail demandDetail : demand.getDemandDetails()) {
								if (demandDetail.getTaxHeadMasterCode().equalsIgnoreCase(headWiseTax.getTaxName())
										&& demandDetail.getTaxAmount().doubleValue() != headWiseTax.getTaxValue()) {
									prevoiusTotalCollection = prevoiusTotalCollection
											+ demandDetail.getCollectionAmount().doubleValue();
									demandDetail.setTaxAmount(new BigDecimal(headWiseTax.getTaxValue()));
									newTotalAmountTobePaid = newTotalAmountTobePaid + headWiseTax.getTaxValue();
									demandDetail.setCollectionAmount(BigDecimal.valueOf(headWiseTax.getTaxValue()));
									break;
								}
							}

						}
						break;
					}

				}

				if (latestDemand.getId() == demand.getId()) {
					if (prevoiusTotalCollection.compareTo(newTotalAmountTobePaid) > 0) {
						prevoiusTotalCollection = prevoiusTotalCollection - newTotalAmountTobePaid.doubleValue();
						DemandDetail demandDetail = new DemandDetail();
						demandDetail.setTenantId(property.getTenantId());
						demandDetail.setTaxAmount(BigDecimal.valueOf(0l));
						demandDetail.setCollectionAmount(BigDecimal.valueOf(prevoiusTotalCollection));
						demandDetail.setTaxHeadMasterCode(propertiesManager.getBillindServiceAdvTaxHead());
						demandDetail.setDemandId(demand.getId());
						demand.getDemandDetails().add(demandDetail);
					}
				}
			}

		}

		DemandRequest demandRequest = new DemandRequest();
		demandRequest.setRequestInfo(requestInfo);
		demandRequest.setDemands(demandResposne.getDemands());
		Gson gson = new Gson();
		logger.info("Demand update request " + gson.toJson(demandRequest));

		String url = propertiesManager.getBillingServiceHostName() + propertiesManager.getBillingServiceUpdateDemand();

		DemandResponse response = null;
		try {
			response = restTemplate.postForObject(url, demandRequest, DemandResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}

	/**
	 * This Api will check whether the tax is increased or decreased during the
	 * property modification
	 * 
	 * @param property
	 * @return True/False if the tax is Increased / if the tax is decreased
	 */
	public Boolean checkTaxdiffrecnce(Property property) throws Exception {

		String propertyIdQuery = DemandBuilder.GET_property_Id;

		Long propertyId = jdbcTemplate.queryForObject(propertyIdQuery, new Object[] { property.getPropertyDetail().getApplicationNo() },
				Long.class);

		String getTaxCalculationsQuery = DemandBuilder.GET_TAX_CALC_FOR_PROPERTY;
		ObjectMapper objectMapper = new ObjectMapper();
		Double previousTotalTax = 0.0d;
		Double currentTotalTax = 0.0d;
		String perviousTaxCalculationsQuery = jdbcTemplate.queryForObject(getTaxCalculationsQuery,
				new Object[] { propertyId }, String.class);
		List<TaxCalculation> previousCalculationList = new ArrayList<>();

		TypeReference<List<TaxCalculation>> typeReference = new TypeReference<List<TaxCalculation>>() {
		};

		previousCalculationList = objectMapper.readValue(perviousTaxCalculationsQuery, typeReference);

		for (TaxCalculation taxCalculation : previousCalculationList) {
			previousTotalTax = previousTotalTax + taxCalculation.getPropertyTaxes().getTotalTax();
		}

		List<TaxCalculation> currentCalculationList = new ArrayList<>();

		currentCalculationList = new ObjectMapper().readValue(property.getPropertyDetail().getTaxCalculations(),
				typeReference);

		for (TaxCalculation taxCalculation : currentCalculationList) {
			currentTotalTax = currentTotalTax + taxCalculation.getPropertyTaxes().getTotalTax();
		}

		if (currentTotalTax > previousTotalTax) {
			return Boolean.TRUE;

		} else
			return Boolean.FALSE;

	}

	/**
	 * Description :This method will get all demands based on upic number and
	 * tenantId
	 * 
	 * @param upicNo
	 * @param tenantId
	 * @param requestInfo
	 * @return demandResponse
	 * @throws Exception
	 */

	public DemandResponse getDemandsByConsumerCode(String consumerCode, String tenantId, RequestInfoWrapper requestInfo)
			throws Exception {
		DemandResponse response = null;
		StringBuffer demandUrl = new StringBuffer();
		demandUrl.append(propertiesManager.getBillingServiceHostName());
		demandUrl.append("");
		MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<String, String>();
		requestMap.add("tenantId", tenantId);
		requestMap.add("consumerCode", consumerCode);
		requestMap.add("businessService", propertiesManager.getDemandBusinessService());
		String demandSearchUrl = propertiesManager.getBillingServiceHostName()
				+ propertiesManager.getBillingServiceSearchDemand();
		URI uri = UriComponentsBuilder.fromHttpUrl(demandSearchUrl).queryParams(requestMap).build().encode().toUri();

		logger.info("Get demand url is" + uri + " demand request is : " + requestInfo);
		Gson gson = new Gson();
		logger.info(gson.toJson(requestInfo));
		try {
			String demandResponse = restTemplate.postForObject(uri, requestInfo, String.class);
			logger.info("Get demand response is :" + demandResponse);
			if (demandResponse != null && demandResponse.contains("Demands")) {
				ObjectMapper objectMapper = new ObjectMapper();
				response = objectMapper.readValue(demandResponse, DemandResponse.class);
			}

		} catch (Exception e) {
			logger.error("Exception while searching the demands " + e.getMessage());

		}
		return response;
	}

}
