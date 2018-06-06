
package org.egov.lams.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.exceptions.CollectionExceedException;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Demand;
import org.egov.lams.model.DemandDetails;
import org.egov.lams.model.PaymentInfo;
import org.egov.lams.model.enums.Action;
import org.egov.lams.model.enums.Source;
import org.egov.lams.repository.BillRepository;
import org.egov.lams.repository.DemandRepository;
import org.egov.lams.repository.FinancialsRepository;
import org.egov.lams.repository.builder.AgreementQueryBuilder;
import org.egov.lams.repository.rowmapper.AgreementRowMapper;
import org.egov.lams.util.BillNumberUtil;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.BillDetailInfo;
import org.egov.lams.web.contract.BillInfo;
import org.egov.lams.web.contract.BillReceiptInfoReq;
import org.egov.lams.web.contract.BillReceiptReq;
import org.egov.lams.web.contract.BillSearchCriteria;
import org.egov.lams.web.contract.BoundaryResponse;
import org.egov.lams.web.contract.ChartOfAccountContract;
import org.egov.lams.web.contract.DemandSearchCriteria;
import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.egov.lams.web.contract.ReceiptAccountInfo;
import org.egov.lams.web.contract.ReceiptAmountInfo;
import org.egov.lams.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {

	private static final Logger LOGGER = Logger.getLogger(PaymentService.class);
	private static final String ADVANCE_TAX = "ADVANCE TAX";
	private static final String GOODWILL_AMOUNT = "GOODWILL AMOUNT";
	private static final String SERVICE_TAX = "SERVICE TAX";
	private static final String CENTRAL_GST = "CGST";
	private static final String STATE_GST = "SGST";

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	AgreementService agreementService;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	LamsConfigurationService lamsConfigurationService;

	@Autowired
	DemandRepository demandRepository;

	@Autowired
	BillRepository billRepository;

	@Autowired
	BillNumberUtil billNumberService;

	@Autowired
	FinancialsRepository financialsRepository;

	public String generateBillXml(Agreement agreement, RequestInfo requestInfo) {
		String collectXML = "";
		try {
			
			Allottee allottee = agreement.getAllottee();
			LamsConfigurationGetRequest lamsGetRequest = new LamsConfigurationGetRequest();
			List<BillInfo> billInfos = new ArrayList<>();
			BillInfo billInfo = new BillInfo();
			billInfo.setId(null);
			LOGGER.info("the demands for a agreement object" + agreement.getDemands());
			if (agreement.getDemands() != null && !agreement.getDemands().isEmpty()) {
				LOGGER.info("the demand id from agreement object" + agreement.getDemands().get(0));
				billInfo.setDemandId(Long.valueOf(agreement.getDemands().get(0)));
			}
			billInfo.setCitizenName(allottee.getName());
			billInfo.setTenantId(agreement.getTenantId());
			// billInfo.setCitizenAddress(agreement.getAllottee().getAddress());
			// TODO: Fix me after the issue is fixed by user service
			
			if (allottee.getAddress() != null)
				billInfo.setCitizenAddress(allottee.getAddress());
			else
				billInfo.setCitizenAddress("NA");	
			billInfo.setBillType("AUTO");
			billInfo.setIssuedDate(new Date());
			billInfo.setLastDate(new Date());
			lamsGetRequest.setName("MODULE_NAME");
			LOGGER.info("before moduleName>>>>>>>");

			String moduleName = lamsConfigurationService.getLamsConfigurations(lamsGetRequest).get("MODULE_NAME")
					.get(0);
			LOGGER.info("after moduleName>>>>>>>" + moduleName);
			billInfo.setModuleName(moduleName);
			lamsGetRequest.setTenantId(agreement.getTenantId());
			lamsGetRequest.setName("FUND_CODE");
			String fundCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest).get("FUND_CODE").get(0);
			billInfo.setFundCode(fundCode);
			LOGGER.info("after fundCode>>>>>>>" + fundCode);

			lamsGetRequest.setName("FUNCTIONARY_CODE");
			String functionaryCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest)
					.get("FUNCTIONARY_CODE").get(0);
			LOGGER.info("after functionaryCode>>>>>>>" + functionaryCode);

			billInfo.setFunctionaryCode(Long.valueOf(functionaryCode));
			lamsGetRequest.setName("FUNDSOURCE_CODE");
			String fundSourceCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest)
					.get("FUNDSOURCE_CODE").get(0);
			LOGGER.info("after fundSourceCode>>>>>>>" + fundSourceCode);

			billInfo.setFundSourceCode(fundSourceCode);
			lamsGetRequest.setName("DEPARTMENT_CODE");
			String departmentCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest)
					.get("DEPARTMENT_CODE").get(0);
			billInfo.setDepartmentCode(departmentCode);
			LOGGER.info("after departmentCode>>>>>>>" + departmentCode);

			billInfo.setCollModesNotAllowed("");
			if (agreement.getAsset().getLocationDetails().getElectionWard() != null) {
				LOGGER.info("setting boundary details with Election ward");
				LOGGER.info("Election ward is: " +agreement.getAsset().getLocationDetails().getElectionWard());
				BoundaryResponse boundaryResponse = getBoundariesById(
						agreement.getAsset().getLocationDetails().getElectionWard(),agreement.getTenantId());
				billInfo.setBoundaryNumber(boundaryResponse.getBoundarys().get(0).getBoundaryNum());
				lamsGetRequest.setName("BOUNDARY_TYPE");
				String boundaryType = lamsConfigurationService.getLamsConfigurations(lamsGetRequest)
						.get("BOUNDARY_TYPE").get(0);
				LOGGER.info("after boundaryType>>>>>>>" + boundaryType);

				billInfo.setBoundaryType(boundaryType);
			} else {
				// Passing Admin City boundary details when election ward is not
				// available
				billInfo.setBoundaryType("City");
				billInfo.setBoundaryNumber(1l);
			}
			lamsGetRequest.setName("SERVICE_CODE");
			String serviceCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest).get("SERVICE_CODE")
					.get(0);
			LOGGER.info("after serviceCode>>>>>>>" + serviceCode);

			billInfo.setServiceCode(serviceCode);
			billInfo.setPartPaymentAllowed('N');
			billInfo.setOverrideAccHeadAllowed('N');
			billInfo.setDescription("Leases And Agreements : " + (StringUtils.isBlank(agreement.getAgreementNumber())
					? agreement.getAcknowledgementNumber() : agreement.getAgreementNumber()));
			LOGGER.info("after billInfo.setDescription>>>>>>>" + billInfo.getDescription());

			billInfo.setConsumerCode(StringUtils.isBlank(agreement.getAgreementNumber())
					? agreement.getAcknowledgementNumber() : agreement.getAgreementNumber());
			billInfo.setCallbackForApportion('N');
			LOGGER.info("after billInfo.setConsumerCode>>>>>>>" + billInfo.getConsumerCode());

			billInfo.setEmailId(agreement.getAllottee().getEmailId());
			billInfo.setConsumerType("Agreement");
			LOGGER.info("before Bill Number" + billNumberService.generateBillNumber());
			billInfo.setBillNumber(billNumberService.generateBillNumber());
			LOGGER.info("after Bill Number" + billNumberService.generateBillNumber());
			DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
			demandSearchCriteria.setDemandId(Long.valueOf(agreement.getDemands().get(0)));

			LOGGER.info("demand before>>>>>>>" + demandSearchCriteria);

			Demand demand = demandRepository.getDemandBySearch(demandSearchCriteria, requestInfo).getDemands().get(0);
			LOGGER.info("demand>>>>>>>" + demand);

			billInfo.setDisplayMessage(demand.getModuleName());
			billInfo.setMinAmountPayable(demand.getMinAmountPayable());

			lamsGetRequest.setName("FUNCTION_CODE");
			String functionCode = lamsConfigurationService.getLamsConfigurations(lamsGetRequest).get("FUNCTION_CODE")
					.get(0);
			BigDecimal totalAmount = BigDecimal.ZERO;
			List<BillDetailInfo> billDetailInfos = new ArrayList<>();
			List<DemandDetails> demandDetails = getOrderedDemandDetails(demand.getDemandDetails());
			
			int orderNo = 0;
			LOGGER.info("PaymentService- generateBillXml - getting purpose");
			Map<String, String> purposeMap = billRepository.getPurpose(billInfo.getTenantId());
			for (DemandDetails demandDetail : demandDetails) {
				if(demandDetail!=null){
				LOGGER.info("the reason for demanddetail : "+ demandDetail.getTaxReason());
				if (ADVANCE_TAX.equalsIgnoreCase(demandDetail.getTaxReason())
						|| GOODWILL_AMOUNT.equalsIgnoreCase(demandDetail.getTaxReason())
						|| (demandDetail.getPeriodStartDate().compareTo(new Date()) <= 0)) {
					orderNo++;
					totalAmount = totalAmount
							.add(demandDetail.getTaxAmount().subtract(demandDetail.getCollectionAmount()));
					LOGGER.info("the amount added to bill : "+totalAmount);
					billDetailInfos
							.addAll(getBilldetails(demandDetail, functionCode, orderNo, requestInfo, purposeMap));
				}
				}
			}
			billDetailInfos.sort((b1, b2) -> b1.getPeriod().compareTo(b2.getPeriod()));
			billInfo.setTotalAmount(totalAmount.doubleValue());
			billInfo.setBillAmount(totalAmount.doubleValue());
			if (billDetailInfos.isEmpty()) {
				LOGGER.info("No bill details for collection");
				throw new CollectionExceedException();
			} else
			billInfo.setBillDetailInfos(billDetailInfos);
			LOGGER.info("billInfo before>>>>>>>" + billInfo);
			billInfos.add(billInfo);
			final String billXml = billRepository.createBillAndGetXml(billInfos, requestInfo);

			try {
				collectXML = URLEncoder.encode(billXml, "UTF-8");
			} catch (final UnsupportedEncodingException e) {
				throw new RuntimeException(e.getMessage());
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return collectXML;
	}

	
	private List<DemandDetails> getOrderedDemandDetails(List<DemandDetails> demandDetails) {

		List<DemandDetails> newDemandList = new LinkedList<>();
		Map<Date, List<DemandDetails>> demandDetailsMap;

		demandDetails.sort((d1, d2) -> d1.getPeriodStartDate().compareTo(d2.getPeriodStartDate()));
		demandDetailsMap = getDemandDetailsMap(demandDetails);

		Set<Date> installmentDates = demandDetailsMap.keySet();
		for (Date installment : installmentDates) {
			Set<DemandDetails> demandList = new LinkedHashSet<>();
			Map<String, DemandDetails> ddMap = new HashMap<>();
			List<DemandDetails> installmentDemands = demandDetailsMap.get(installment);
			for (DemandDetails demandDetail : installmentDemands) {

				ddMap.put(demandDetail.getTaxReasonCode(), demandDetail);

			}
			if (ddMap.containsKey("ADVANCE_TAX")) {
				demandList.add(ddMap.get("ADVANCE_TAX"));
			}
			if (ddMap.containsKey("GOODWILL_AMOUNT")) {
				demandList.add(ddMap.get("GOODWILL_AMOUNT"));
			}
			if (ddMap.containsKey("RENT")) {
				demandList.add(ddMap.get("RENT"));
			}
			if (ddMap.containsKey("PENALTY")) {
				demandList.add(ddMap.get("PENALTY"));
			}
			if (ddMap.containsKey("SERVICE_TAX")) {
				demandList.add(ddMap.get("SERVICE_TAX"));
			}
			if (ddMap.containsKey("CENTRAL_GST")) {
				demandList.add(ddMap.get("CENTRAL_GST"));
			}
			if (ddMap.containsKey("STATE_GST")) {
				demandList.add(ddMap.get("STATE_GST"));
			}
			newDemandList.addAll(demandList);

		}
		return newDemandList;

	}
	public List<BillDetailInfo> getBilldetails(final DemandDetails demandDetail, String functionCode, int orderNo,
			RequestInfo requestInfo, Map<String, String> purpose) {
		final List<BillDetailInfo> billDetails = new ArrayList<>();
		BigDecimal balance = BigDecimal.ZERO;
		LOGGER.info("paymentservice demand detail ::"+demandDetail);
		try {
			BillDetailInfo billdetail = new BillDetailInfo();
			// TODO: Fix me: As per the rules for the order no.
			String description = getInstallmentDescription(demandDetail);
			balance=demandDetail.getTaxAmount().subtract(demandDetail.getCollectionAmount());
			billdetail.setOrderNo(orderNo);
			billdetail.setCreditAmount(balance);
			billdetail.setDebitAmount(BigDecimal.ZERO);
			LOGGER.info("getGlCode before>>>>>>>" + demandDetail.getGlCode());
			billdetail.setGlCode(demandDetail.getGlCode());
			LOGGER.info("getGlCode after >>>>>>>" + demandDetail.getGlCode());
			billdetail.setDescription(description);
			billdetail.setPeriod(demandDetail.getTaxPeriod());
			if (SERVICE_TAX.equalsIgnoreCase(demandDetail.getTaxReason())) {
				billdetail.setPurpose(purpose.get("SERVICETAX"));
			} else if (CENTRAL_GST.equalsIgnoreCase(demandDetail.getTaxReason())) {
				billdetail.setPurpose(purpose.get("CG_SERVICETAX"));
			} else if (STATE_GST.equalsIgnoreCase(demandDetail.getTaxReason())) {
				billdetail.setPurpose(purpose.get("SG_SERVICETAX"));
			} else
				billdetail.setPurpose(purpose.get("CURRENT_AMOUNT"));
			LOGGER.info("getPurpose after >>>>>>>" + purpose.get("CURRENT_AMOUNT"));

			billdetail.setIsActualDemand(demandDetail.getIsActualDemand());
			billdetail.setFunctionCode(functionCode);
			if (balance.compareTo(BigDecimal.ZERO) > 0) {
				billDetails.add(billdetail);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return billDetails;
	}

	public ResponseEntity<ReceiptAmountInfo> updateDemand(BillReceiptInfoReq billReceiptInfoReq) {
		LOGGER.info("PaymentService- updateDemand - billReceiptInfoReq::: - "
				+ billReceiptInfoReq.getBillReceiptInfo().getBillReferenceNum());

		RequestInfo requestInfo = billReceiptInfoReq.getRequestInfo();
		BillSearchCriteria billSearchCriteria = new BillSearchCriteria();
		DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
		BillReceiptReq billReceiptInfo = billReceiptInfoReq.getBillReceiptInfo();
		billSearchCriteria.setBillId(Long.valueOf(billReceiptInfo.getBillReferenceNum()));
		BillInfo billInfo = billRepository.searchBill(billSearchCriteria, requestInfo);
		LOGGER.info("PaymentService- updateDemand - billInfo - " + billInfo.getBillNumber());
		demandSearchCriteria.setDemandId(billInfo.getDemandId());
		Demand currentDemand = demandRepository.getDemandBySearch(demandSearchCriteria, requestInfo).getDemands()
				.get(0);
		LOGGER.info("PaymentService- updateDemand - currentDemand - " + currentDemand.getId());
		if (currentDemand.getMinAmountPayable() != null && currentDemand.getMinAmountPayable() > 0)
			currentDemand.setMinAmountPayable(0d);

		updateDemandDetailForReceiptCreate(currentDemand, billReceiptInfoReq.getBillReceiptInfo());
		LOGGER.info("PaymentService- updateDemand - updateDemandDetailForReceiptCreate done");
		LOGGER.info("The amount collected from citizen is ::: " + currentDemand.getCollectionAmount());
		currentDemand.setPaymentInfos(setPaymentInfos(billReceiptInfo));
		demandRepository.updateDemandForCollection(Arrays.asList(currentDemand), requestInfo).getDemands().get(0);
		LOGGER.info("PaymentService- updateDemand - setPaymentInfos done");

		// / FIXME put update workflow here here
		
		updateWorkflow(billInfo.getConsumerCode(), requestInfo);
		isAdvanceCollection(billInfo.getConsumerCode(),currentDemand);
		LOGGER.info("the consumer code from bill object ::: " + billInfo.getConsumerCode());
		return receiptAmountBifurcation(billReceiptInfo, billInfo);
	}

	private void updateWorkflow(String consumerCode, RequestInfo requestInfo) {

		// FIXME get the query String from query builder //FIXME do the
		// jdbctemplate in repository
		String sql = AgreementQueryBuilder.BASE_SEARCH_QUERY + " where agreement.acknowledgementnumber='" 
					+ consumerCode + "' OR agreement.agreement_no='" + consumerCode + "' order by agreement.id desc ";

		LOGGER.info("the sql query for fetching agreement using consumercode ::: " + sql);
		List<Agreement> agreements = null;
		try {
			agreements = jdbcTemplate.query(sql, new AgreementRowMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
			LOGGER.info("exception while fetching agreemment in paymentService");
		}
		LOGGER.info("the result form jdbc query ::: " + agreements);
		agreements.sort((agreement1, agreement2) -> agreement2.getId().compareTo(agreement1.getId()));
		Agreement agreement = agreements.get(0);
		LOGGER.info("agreement under workflow --> "+agreement.getId());
		if (Source.SYSTEM.equals(agreement.getSource())
					|| (Source.DATA_ENTRY.equals(agreement.getSource()) && Action.RENEWAL.equals(agreement.getAction()))) {
			AgreementRequest agreementRequest = new AgreementRequest();
			agreementRequest.setRequestInfo(requestInfo);
			agreementRequest.setAgreement(agreement);
			LOGGER.info("calling agreement service todo agreement update");
			agreementService.updateAgreement(agreementRequest);
			LOGGER.info("Workflow update for collection has been put into Kafka Queue");
		}
	}

	private List<PaymentInfo> setPaymentInfos(BillReceiptReq billReceiptInfo) {

		List<PaymentInfo> paymentInfos = new ArrayList<>();
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setReceiptAmount(billReceiptInfo.getTotalAmount());
		paymentInfo.setReceiptDate(billReceiptInfo.getReceiptDate());
		paymentInfo.setReceiptNumber(billReceiptInfo.getReceiptNum());
		paymentInfo.setStatus(billReceiptInfo.getReceiptStatus());
		paymentInfos.add(paymentInfo);
		LOGGER.info("paymengtinfo"+ paymentInfos);
		return paymentInfos;
	}

	private void updateDemandDetailForReceiptCreate(Demand demand, BillReceiptReq billReceiptInfo) {
		BigDecimal totalAmountCollected = BigDecimal.ZERO;
		LOGGER.info("the size of objects ::: " + billReceiptInfo.getAccountDetails().size()
				+ "the size of demand details ::" + demand.getDemandDetails().size());
		prepareDemandDetailsForCollectionUpdate(demand);
		for (final ReceiptAccountInfo rcptAccInfo : billReceiptInfo.getAccountDetails()) {

			totalAmountCollected = totalAmountCollected.add(updateDemandDetails(demand, rcptAccInfo));
		}
		LOGGER.info("updateDemandDetailForReceiptCreate  ::: totalAmountCollected " + totalAmountCollected);
		demand.setCollectionAmount(totalAmountCollected);
	}
     
	/*
	 * making collection amount zero if rent is fully paid 
	 * and actual collection should be updated in back update of collection 
	 */
	private void prepareDemandDetailsForCollectionUpdate(Demand demand) {

		for (DemandDetails demandDetail : demand.getDemandDetails()) {
			demandDetail.setCollectionAmount(BigDecimal.ZERO);
		}

	}
	private BigDecimal updateDemandDetails(Demand demand, final ReceiptAccountInfo rcptAccInfo) {

		BigDecimal totalAmountCollected = BigDecimal.ZERO;

		LOGGER.info("updateDemandDetailForReceiptCreate rcptAccInfo ::: " + rcptAccInfo);
		if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount() > 0 && !rcptAccInfo.isRevenueAccount()

				&& rcptAccInfo.getDescription() != null) {
			String[] description = rcptAccInfo.getDescription().split(":");
			String taxPeriod = description[0];
			String taxReason = description[1];
			LOGGER.info("taxPeriod  ::: " + taxPeriod + "taxReason ::::::" + taxReason);
			// updating the existing demand detail..
			for (final DemandDetails demandDetail : demand.getDemandDetails()) {
				LOGGER.info("demandDetail.getTaxPeriod()  ::: " + demandDetail.getTaxPeriod()
						+ "demandDetail.getTaxReason() ::::::" + demandDetail.getTaxReason());
				if (demandDetail.getTaxPeriod() != null && demandDetail.getTaxPeriod().equalsIgnoreCase(taxPeriod)
						&& demandDetail.getTaxReason() != null
						&& demandDetail.getTaxReason().equalsIgnoreCase(taxReason)) {
					demandDetail.setCollectionAmount(BigDecimal.valueOf(rcptAccInfo.getCrAmount()));
					totalAmountCollected = totalAmountCollected.add(BigDecimal.valueOf(rcptAccInfo.getCrAmount()));
					LOGGER.info("everytime totalAmountCollected ::: " + totalAmountCollected);
				}
			}
		}

		return totalAmountCollected;
	}

	public ResponseEntity<ReceiptAmountInfo> receiptAmountBifurcation(final BillReceiptReq billReceiptInfo,
			BillInfo billInfo) {
		ResponseEntity<ReceiptAmountInfo> receiptAmountInfoResponse = null;
		LOGGER.info("PaymentService- receiptAmountBifurcation - billReceiptInfo - " + billReceiptInfo);
		LOGGER.info("PaymentService- receiptAmountBifurcation - billInfo - " + billInfo);
		final ReceiptAmountInfo receiptAmountInfo = new ReceiptAmountInfo();
		BigDecimal currentInstallmentAmount = BigDecimal.ZERO;
		BigDecimal arrearAmount = BigDecimal.ZERO;
		LOGGER.info("PaymentService- receiptAmountBifurcation - getting purpose");
		Map<String, String> purposeMap = billRepository.getPurpose(billReceiptInfo.getTenantId());
		final List<BillDetailInfo> billDetails = new ArrayList<>(billInfo.getBillDetailInfos());
		for (final ReceiptAccountInfo rcptAccInfo : billReceiptInfo.getAccountDetails()) {
			LOGGER.info("PaymentService- receiptAmountBifurcation - rcptAccInfo - " + rcptAccInfo);
			if (rcptAccInfo.getCrAmount() != null
					&& BigDecimal.valueOf(rcptAccInfo.getCrAmount()).compareTo(BigDecimal.ZERO) == 1) {
				if (rcptAccInfo.getPurpose().equals(purposeMap.get("ARREAR_AMOUNT").toString()))
					arrearAmount = arrearAmount.add(BigDecimal.valueOf(rcptAccInfo.getCrAmount()));
				else
					currentInstallmentAmount = currentInstallmentAmount
							.add(BigDecimal.valueOf(rcptAccInfo.getCrAmount()));

				for (final BillDetailInfo billDet : billDetails) {
					if (billDet.getOrderNo() == 1) {
						receiptAmountInfo.setInstallmentFrom(billDet.getDescription());
					}
					receiptAmountInfo.setCurrentInstallmentAmount(currentInstallmentAmount.doubleValue());
					receiptAmountInfo.setArrearsAmount(arrearAmount.doubleValue());
					receiptAmountInfoResponse = new ResponseEntity<>(receiptAmountInfo, HttpStatus.OK);
				}
			}
			LOGGER.info("PaymentService- receiptAmountBifurcation - receiptAmountInfo - " + receiptAmountInfo);
		}
		return receiptAmountInfoResponse;
	}

	private String getGlcodeById(Long id, String tenantId, RequestInfo requestInfo) {
		ChartOfAccountContract chartOfAccountContract = new ChartOfAccountContract();
		chartOfAccountContract.setId(id);
		chartOfAccountContract.setTenantId(tenantId);
		return financialsRepository.getChartOfAccountGlcodeById(chartOfAccountContract, requestInfo);
	}

	private BoundaryResponse getBoundariesById(Long boundaryId,String tenantId) {

		BoundaryResponse boundaryResponse = null;
		String boundaryUrl = propertiesManager.getBoundaryServiceHostName()
							+ propertiesManager.getBoundaryServiceSearchPath() 
							+ "?Boundary.id=" + boundaryId
							+ "&Boundary.tenantId=" + tenantId;
		// FIXME in boundary contract id is string
		LOGGER.info("the boundary url from payment service ::"+boundaryUrl);
		try {
			boundaryResponse = restTemplate.getForObject(boundaryUrl, BoundaryResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("the exception thrown from boundary request is :: " + e);
		}
		LOGGER.info("the response from boundary ::"+boundaryResponse);
		return boundaryResponse;
	}
	
	private void isAdvanceCollection(String acknowledgementno, Demand demand) {
		boolean isAdvancePaid = false;
		String sql = AgreementQueryBuilder.BASE_SEARCH_QUERY + " where agreement.acknowledgementnumber='"
				+ acknowledgementno + "'";
		List<Agreement> agreements = null;
		Agreement agreement;
		try {
			agreements = jdbcTemplate.query(sql, new AgreementRowMapper());
		} catch (DataAccessException e) {
			LOGGER.info("exception while fetching agreemment to update advance" + e);
		}
		LOGGER.info("the result form jdbc query ::: " + agreements);
		if (agreements != null && !agreements.isEmpty()) {
			agreement = agreements.get(0);

			for (DemandDetails demandDetail : demand.getDemandDetails()) {
				LOGGER.info("the reason for demanddetail : " + demandDetail.getTaxReason());
				if ("ADVANCE TAX".equalsIgnoreCase(demandDetail.getTaxReason())) {
					isAdvancePaid = true;
				}

			}
			if (isAdvancePaid && agreement.getAction().equals(Action.CREATE)) {
				agreementService.updateAdvanceFlag(agreement);
			}
		}

	}

	private Map<Date, List<DemandDetails>> getDemandDetailsMap(List<DemandDetails> demandDetails) {
		Map<Date, List<DemandDetails>> demandDetailsMap = new LinkedHashMap<>();

		for (DemandDetails d : demandDetails) {

			if (!demandDetailsMap.containsKey(d.getPeriodStartDate())) {
				List<DemandDetails> ddList = new ArrayList<>();
				ddList.add(d);
				demandDetailsMap.put(d.getPeriodStartDate(), ddList);

			} else {
				List<DemandDetails> ddList = new ArrayList<>();
				ddList.add(d);
				demandDetailsMap.get(d.getPeriodStartDate()).addAll(ddList);

			}

		}

		return demandDetailsMap;

	}

	private String getInstallmentDescription(DemandDetails demandDetail) {

		StringBuilder installmentDesc = new StringBuilder();
		StringBuilder timePeriod = new StringBuilder();
		Instant startInstant = Instant.ofEpochMilli(demandDetail.getPeriodStartDate().getTime());
		LocalDateTime startDate = LocalDateTime.ofInstant(startInstant, ZoneId.systemDefault());
		LocalDate installmentFromDate = startDate.toLocalDate();
		Month fromDate = installmentFromDate.getMonth();
		String startingMonth = fromDate.toString();
		int fromYear = installmentFromDate.getYear();

		Instant endInstant = Instant.ofEpochMilli(demandDetail.getPeriodEndDate().getTime());
		LocalDateTime endDate = LocalDateTime.ofInstant(endInstant, ZoneId.systemDefault());
		LocalDate installmentToDate = endDate.toLocalDate();
		Month toDate = installmentToDate.getMonth();
		String endingMonth = toDate.toString();
		int toYear = installmentToDate.getYear();

		if (startingMonth.equalsIgnoreCase(endingMonth)) {

			timePeriod.append(WordUtils.capitalizeFully(startingMonth.substring(0, 3))).append(",").append(fromYear);
		} else {
			timePeriod.append(WordUtils.capitalizeFully(startingMonth.substring(0, 3))).append(",").append(fromYear)
					.append("-").append(WordUtils.capitalizeFully(endingMonth.substring(0, 3))).append(",")
					.append(toYear);

		}

		installmentDesc.append(timePeriod.toString()).append(":").append(demandDetail.getTaxReason());

		return installmentDesc.toString();
	}
}