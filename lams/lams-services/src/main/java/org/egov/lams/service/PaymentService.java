package org.egov.lams.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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

@Service
public class PaymentService {

	private static final Logger LOGGER = Logger.getLogger(PaymentService.class);

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
			billInfo.setBoundaryNumber(agreement.getAsset()
					.getLocationDetails().getElectionWard());
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
			demandSearchCriteria.setDemandId(1L);

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
			for (DemandDetails demandDetail : demand.getDemandDetails()) {
				orderNo++;
				totalAmount = totalAmount.add(demandDetail.getTaxAmount());
				billDetailInfos.addAll(getBilldetails(demandDetail,
						functionCode, orderNo, requestInfo));
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
			final DemandDetails demandDetail, String functionCode, int orderNo, RequestInfo requestInfo) {
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
			billdetail.setPurpose(billRepository.getPurpose()
					.get("CURRENT_AMOUNT").toString());
			LOGGER.info("getPurpose after >>>>>>>"
					+ billRepository.getPurpose().get("CURRENT_AMOUNT"));

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

		BillSearchCriteria billSearchCriteria = new BillSearchCriteria();
		BillReceiptReq billReceiptInfo = billReceiptInfoReq
				.getBillReceiptInfo();
		billSearchCriteria.setBillId(Long.valueOf(1));
		BillInfo billInfo = billRepository.searchBill(billSearchCriteria,
				billReceiptInfoReq.getRequestInfo());
		DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
		demandSearchCriteria.setDemandId(billInfo.getDemandId());
		Demand currentDemand = demandRepository
				.getDemandBySearch(demandSearchCriteria,
						billReceiptInfoReq.getRequestInfo()).getDemands()
				.get(0);
		if (currentDemand.getMinAmountPayable() != null
				&& currentDemand.getMinAmountPayable() > 0)
			currentDemand.setMinAmountPayable(0d);

		updateDemandDetailForReceiptCreate(currentDemand,
				billReceiptInfoReq.getBillReceiptInfo());

		currentDemand.setPaymentInfos(setPaymentInfos(billReceiptInfo));
		demandRepository
				.updateDemand(Arrays.asList(currentDemand),
						billReceiptInfoReq.getRequestInfo()).getDemands()
				.get(0);
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

		final ReceiptAmountInfo receiptAmountInfo = new ReceiptAmountInfo();
		BigDecimal currentInstallmentAmount = BigDecimal.ZERO;
		BigDecimal arrearAmount = BigDecimal.ZERO;
		final List<BillDetailInfo> billDetails = new ArrayList<>(
				billInfo.getBillDetailInfos());
		for (final ReceiptAccountInfo rcptAccInfo : billReceiptInfo
				.getAccountDetails()) {
			if (rcptAccInfo.getCreditAmount() != null
					&& BigDecimal.valueOf(rcptAccInfo.getCreditAmount())
							.compareTo(BigDecimal.ZERO) == 1) {
				if (rcptAccInfo.getPurpose().equals(
						billRepository.getPurpose().get("ARREAR_AMOUNT")
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
		}
		return receiptAmountInfoResponse;
	}
	
	private String getGlcodeById(Long id, RequestInfo requestInfo){
		ChartOfAccountContract chartOfAccountContract= new ChartOfAccountContract();
		chartOfAccountContract.setId(id);
		return financialsRepository.getChartOfAccountGlcodeById(chartOfAccountContract, requestInfo);
	}

}
