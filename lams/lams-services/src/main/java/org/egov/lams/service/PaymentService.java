package org.egov.lams.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Demand;
import org.egov.lams.model.DemandDetails;
import org.egov.lams.model.PaymentInfo;
import org.egov.lams.repository.BillRepository;
import org.egov.lams.repository.DemandRepository;
import org.egov.lams.repository.FinancialsRepository;
import org.egov.lams.web.contract.BillDetailInfo;
import org.egov.lams.web.contract.BillInfo;
import org.egov.lams.web.contract.BillReceiptInfoReq;
import org.egov.lams.web.contract.BillReceiptReq;
import org.egov.lams.web.contract.BillSearchCriteria;
import org.egov.lams.web.contract.Boundary;
import org.egov.lams.web.contract.BoundaryRequest;
import org.egov.lams.web.contract.BoundaryResponse;
import org.egov.lams.web.contract.ChartOfAccountContract;
import org.egov.lams.web.contract.DemandSearchCriteria;
import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.egov.lams.web.contract.ReceiptAccountInfo;
import org.egov.lams.web.contract.ReceiptAmountInfo;
import org.egov.lams.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {

	private static final Logger LOGGER = Logger.getLogger(PaymentService.class);
	
	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	LamsConfigurationService lamsConfigurationService;

	@Autowired
	DemandRepository demandRepository;

	@Autowired
	BillRepository billRepository;

	@Autowired
	BillNumberService billNumberService;
	
	@Autowired
	FinancialsRepository financialsRepository;

	public String generateBillXml(Agreement agreement, RequestInfo requestInfo) {
		String collectXML = "";
		try {
			LamsConfigurationGetRequest lamsGetRequest = new LamsConfigurationGetRequest();
			List<BillInfo> billInfos = new ArrayList<>();
			BillInfo billInfo = new BillInfo();
			billInfo.setId(null);
			LOGGER.info("the demands for a agreement object"+agreement.getDemands());
			if(agreement.getDemands()!= null && !agreement.getDemands().isEmpty()){
				LOGGER.info("the demand id from agreement object"+agreement.getDemands().get(0));
			billInfo.setDemandId(Long.valueOf(agreement.getDemands().get(0)));
			}
			billInfo.setCitizenName(agreement.getAllottee().getName());
			// billInfo.setCitizenAddress(agreement.getAllottee().getAddress());
			// TODO: Fix me after the issue is fixed by user service
			billInfo.setCitizenAddress("Test");
			billInfo.setBillType("AUTO");
			billInfo.setIssuedDate(new Date());
			billInfo.setLastDate(new Date());
			lamsGetRequest.setName("MODULE_NAME");
			LOGGER.info("before moduleName>>>>>>>");

			String moduleName = lamsConfigurationService
					.getLamsConfigurations(lamsGetRequest).get("MODULE_NAME")
					.get(0);
			LOGGER.info("after moduleName>>>>>>>" + moduleName);
			billInfo.setModuleName(moduleName);
			lamsGetRequest.setTenantId(agreement.getTenantId());
			lamsGetRequest.setName("FUND_CODE");
			String fundCode = lamsConfigurationService
					.getLamsConfigurations(lamsGetRequest).get("FUND_CODE")
					.get(0);
			billInfo.setFundCode(fundCode);
			LOGGER.info("after fundCode>>>>>>>" + fundCode);

			lamsGetRequest.setName("FUNCTIONARY_CODE");
			String functionaryCode = lamsConfigurationService
					.getLamsConfigurations(lamsGetRequest)
					.get("FUNCTIONARY_CODE").get(0);
			LOGGER.info("after functionaryCode>>>>>>>" + functionaryCode);

			billInfo.setFunctionaryCode(Long.valueOf(functionaryCode));
			lamsGetRequest.setName("FUNDSOURCE_CODE");
			String fundSourceCode = lamsConfigurationService
					.getLamsConfigurations(lamsGetRequest)
					.get("FUNDSOURCE_CODE").get(0);
			LOGGER.info("after fundSourceCode>>>>>>>" + fundSourceCode);

			billInfo.setFundSourceCode(fundSourceCode);
			lamsGetRequest.setName("DEPARTMENT_CODE");
			String departmentCode = lamsConfigurationService
					.getLamsConfigurations(lamsGetRequest)
					.get("DEPARTMENT_CODE").get(0);
			billInfo.setDepartmentCode(departmentCode);
			LOGGER.info("after departmentCode>>>>>>>" + departmentCode);

			billInfo.setCollModesNotAllowed("");
			
			BoundaryResponse boundaryResponse = getBoundariesById(agreement.getAsset()
					.getLocationDetails().getElectionWard());
			billInfo.setBoundaryNumber(boundaryResponse.getBoundarys().get(0).getBoundaryNum());
			lamsGetRequest.setName("BOUNDARY_TYPE");
			String boundaryType = lamsConfigurationService
					.getLamsConfigurations(lamsGetRequest).get("BOUNDARY_TYPE")
					.get(0);
			LOGGER.info("after boundaryType>>>>>>>" + boundaryType);

			billInfo.setBoundaryType(boundaryType);
			lamsGetRequest.setName("SERVICE_CODE");
			String serviceCode = lamsConfigurationService
					.getLamsConfigurations(lamsGetRequest).get("SERVICE_CODE")
					.get(0);
			LOGGER.info("after serviceCode>>>>>>>" + serviceCode);

			billInfo.setServiceCode(serviceCode);
			billInfo.setPartPaymentAllowed('N');
			billInfo.setOverrideAccHeadAllowed('N');
			billInfo.setDescription("Leases And Agreements : "
					+ (StringUtils.isBlank(agreement.getAgreementNumber()) ? agreement
							.getAcknowledgementNumber() : agreement
							.getAgreementNumber()));
			LOGGER.info("after billInfo.setDescription>>>>>>>"
					+ billInfo.getDescription());

			billInfo.setConsumerCode(StringUtils.isBlank(agreement
					.getAgreementNumber()) ? agreement
					.getAcknowledgementNumber() : agreement
					.getAgreementNumber());
			billInfo.setCallbackForApportion('N');
			LOGGER.info("after billInfo.setConsumerCode>>>>>>>"
					+ billInfo.getConsumerCode());

			billInfo.setEmailId(agreement.getAllottee().getEmailId());
			billInfo.setConsumerType("Agreement");
			LOGGER.info("before Bill Number"
					+ billNumberService.generateBillNumber());
			billInfo.setBillNumber(billNumberService.generateBillNumber());
			LOGGER.info("after Bill Number"
					+ billNumberService.generateBillNumber());
			DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
			demandSearchCriteria.setDemandId(Long.valueOf(agreement.getDemands().get(0)));

			LOGGER.info("demand before>>>>>>>" + demandSearchCriteria);

			Demand demand = demandRepository
					.getDemandBySearch(demandSearchCriteria, requestInfo)
					.getDemands().get(0);
			LOGGER.info("demand>>>>>>>" + demand);

			billInfo.setDisplayMessage(demand.getModuleName());
			billInfo.setMinAmountPayable(demand.getMinAmountPayable());

			lamsGetRequest.setName("FUNCTION_CODE");
			String functionCode = lamsConfigurationService
					.getLamsConfigurations(lamsGetRequest).get("FUNCTION_CODE")
					.get(0);
			BigDecimal totalAmount = BigDecimal.ZERO;
			List<BillDetailInfo> billDetailInfos = new ArrayList<>();
			int orderNo = 0;
			System.out.print("PaymentService- generateBillXml - getting purpose");
			Map<String, String> purposeMap = billRepository.getPurpose();
			for (DemandDetails demandDetail : demand.getDemandDetails()) {
				orderNo++;
				totalAmount = totalAmount.add(demandDetail.getTaxAmount());
				billDetailInfos.addAll(getBilldetails(demandDetail,
						functionCode, orderNo, requestInfo, purposeMap));
			}
			billInfo.setTotalAmount(totalAmount.doubleValue());
			billInfo.setBillAmount(totalAmount.doubleValue());
			billInfo.setBillDetailInfos(billDetailInfos);
			LOGGER.info("billInfo before>>>>>>>" + billInfo);
			billInfos.add(billInfo);
			final String billXml = billRepository.createBillAndGetXml(
					billInfos, requestInfo);

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

	public List<BillDetailInfo> getBilldetails(
			final DemandDetails demandDetail, String functionCode, int orderNo, RequestInfo requestInfo, Map<String, String> purpose) {
		final List<BillDetailInfo> billDetails = new ArrayList<>();

		try {
			BillDetailInfo billdetail = new BillDetailInfo();
			// TODO: Fix me: As per the rules for the order no.
			billdetail.setOrderNo(orderNo);
			billdetail.setCreditAmount(demandDetail.getTaxAmount());
			billdetail.setDebitAmount(BigDecimal.ZERO);
			LOGGER.info("getGlCode before>>>>>>>" + demandDetail.getGlCode());
			billdetail.setGlCode(getGlcodeById(demandDetail.getGlCode(), requestInfo));
			LOGGER.info("getGlCode after >>>>>>>" + demandDetail.getGlCode());
			billdetail.setDescription(demandDetail.getTaxPeriod());
			billdetail.setPeriod(demandDetail.getTaxPeriod());
			billdetail.setPurpose(purpose.get("CURRENT_AMOUNT").toString());
			LOGGER.info("getPurpose after >>>>>>>" + purpose.get("CURRENT_AMOUNT"));

			billdetail.setIsActualDemand(demandDetail.getIsActualDemand());
			billdetail.setFunctionCode(functionCode);
			billDetails.add(billdetail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return billDetails;
	}

	public ResponseEntity<ReceiptAmountInfo> updateDemand(
			BillReceiptInfoReq billReceiptInfoReq) {
		System.out.print("PaymentService- updateDemand - billReceiptInfoReq - "+billReceiptInfoReq);
		BillSearchCriteria billSearchCriteria = new BillSearchCriteria();
		DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
		BillReceiptReq billReceiptInfo = billReceiptInfoReq
				.getBillReceiptInfo();
		billSearchCriteria.setBillId(Long.valueOf(billReceiptInfo.getBillReferenceNum()));
		BillInfo billInfo = billRepository.searchBill(billSearchCriteria,
				billReceiptInfoReq.getRequestInfo());
		System.out.print("PaymentService- updateDemand - billInfo - "+billInfo);
		demandSearchCriteria.setDemandId(billInfo.getDemandId());
		Demand currentDemand = demandRepository
				.getDemandBySearch(demandSearchCriteria,
						billReceiptInfoReq.getRequestInfo()).getDemands()
				.get(0);
		System.out.print("PaymentService- updateDemand - currentDemand - "+currentDemand);
		if (currentDemand.getMinAmountPayable() != null
				&& currentDemand.getMinAmountPayable() > 0)
			currentDemand.setMinAmountPayable(0d);

		updateDemandDetailForReceiptCreate(currentDemand,
				billReceiptInfoReq.getBillReceiptInfo());
		System.out.print("PaymentService- updateDemand - updateDemandDetailForReceiptCreate done");
		currentDemand.setPaymentInfos(setPaymentInfos(billReceiptInfo));
		demandRepository
				.updateDemand(Arrays.asList(currentDemand),
						billReceiptInfoReq.getRequestInfo()).getDemands()
				.get(0);
		System.out.print("PaymentService- updateDemand - setPaymentInfos done");
		return receiptAmountBifurcation(billReceiptInfo, billInfo);
	}

	private List<PaymentInfo> setPaymentInfos(BillReceiptReq billReceiptInfo) {

		List<PaymentInfo> paymentInfos = new ArrayList<>();
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setReceiptAmount(billReceiptInfo.getTotalAmount()
				.toString());
		paymentInfo.setReceiptDate(billReceiptInfo.getReceiptDate().toString());
		paymentInfo.setReceiptNumber(billReceiptInfo.getReceiptNum());
		paymentInfo.setStatus(billReceiptInfo.getReceiptStatus());
		return paymentInfos;

	}

	private void updateDemandDetailForReceiptCreate(Demand demand,
			BillReceiptReq billReceiptInfo) {
		BigDecimal totalAmountCollected = BigDecimal.ZERO;
		for (final ReceiptAccountInfo rcptAccInfo : billReceiptInfo
				.getAccountDetails())
			if (rcptAccInfo.getCreditAmount() != null
					&& rcptAccInfo.getCreditAmount() > 0
					&& !rcptAccInfo.isRevenueAccount()
					&& rcptAccInfo.getAccountDescription() != null) {
				// updating the existing demand detail..
				for (final DemandDetails demandDetail : demand
						.getDemandDetails())
					if (demandDetail.getTaxReason() != null
							&& demandDetail.getTaxReason().equalsIgnoreCase(
									rcptAccInfo.getDescription())) {

						demandDetail.setCollectionAmount(BigDecimal
								.valueOf(rcptAccInfo.getCreditAmount()));

						totalAmountCollected = totalAmountCollected
								.add(BigDecimal.valueOf(rcptAccInfo
										.getCreditAmount()));
					}
			}
		demand.setCollectionAmount(totalAmountCollected);
	}

	public ResponseEntity<ReceiptAmountInfo> receiptAmountBifurcation(
			final BillReceiptReq billReceiptInfo, BillInfo billInfo) {
		ResponseEntity<ReceiptAmountInfo> receiptAmountInfoResponse = null;
		System.out.print("PaymentService- receiptAmountBifurcation - billReceiptInfo - "+billReceiptInfo);
		System.out.print("PaymentService- receiptAmountBifurcation - billInfo - "+billInfo);
		final ReceiptAmountInfo receiptAmountInfo = new ReceiptAmountInfo();
		BigDecimal currentInstallmentAmount = BigDecimal.ZERO;
		BigDecimal arrearAmount = BigDecimal.ZERO;
		System.out.print("PaymentService- receiptAmountBifurcation - getting purpose");
		Map<String, String> purposeMap = billRepository.getPurpose();
		final List<BillDetailInfo> billDetails = new ArrayList<>(
				billInfo.getBillDetailInfos());
		for (final ReceiptAccountInfo rcptAccInfo : billReceiptInfo
				.getAccountDetails()) {
			System.out.print("PaymentService- receiptAmountBifurcation - rcptAccInfo - "+rcptAccInfo);
			if (rcptAccInfo.getCreditAmount() != null
					&& BigDecimal.valueOf(rcptAccInfo.getCreditAmount())
							.compareTo(BigDecimal.ZERO) == 1) {
				if (rcptAccInfo.getPurpose().equals(
						purposeMap.get("ARREAR_AMOUNT")
								.toString()))
					arrearAmount = arrearAmount.add(BigDecimal
							.valueOf(rcptAccInfo.getCreditAmount()));
				else
					currentInstallmentAmount = currentInstallmentAmount
							.add(BigDecimal.valueOf(rcptAccInfo
									.getCreditAmount()));

				for (final BillDetailInfo billDet : billDetails) {
					if (billDet.getOrderNo() == 1) {
						receiptAmountInfo.setInstallmentFrom(billDet
								.getDescription());
					}
					receiptAmountInfo
							.setCurrentInstallmentAmount(currentInstallmentAmount
									.doubleValue());
					receiptAmountInfo.setArrearsAmount(arrearAmount
							.doubleValue());
					receiptAmountInfoResponse = new ResponseEntity<>(
							receiptAmountInfo, HttpStatus.OK);
				}
			}
			System.out.print("PaymentService- receiptAmountBifurcation - receiptAmountInfo - "+receiptAmountInfo);
		}
		return receiptAmountInfoResponse;
	}
	
	private String getGlcodeById(Long id, RequestInfo requestInfo){
		ChartOfAccountContract chartOfAccountContract= new ChartOfAccountContract();
		chartOfAccountContract.setId(id);
		return financialsRepository.getChartOfAccountGlcodeById(chartOfAccountContract, requestInfo);
	}
	
	private BoundaryResponse getBoundariesById(Long boundaryId){
		
		BoundaryResponse boundaryResponse = null;
		String boundaryUrl = propertiesManager.getBoundaryserviceHostName() 
				            + propertiesManager.getBoundaryserviceSearchPath()
				            + "id=" + boundaryId.toString();
		//FIXME in boundary contract id is string
		try{
			boundaryResponse = restTemplate.getForObject(boundaryUrl,BoundaryResponse.class);
		}catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("the exception thrown from boundary request is :: "+e);
		}
		
		return boundaryResponse;
	}

}
