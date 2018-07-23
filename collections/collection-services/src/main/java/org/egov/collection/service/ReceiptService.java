/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.collection.service;

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.exception.CustomException;
import org.egov.collection.model.*;
import org.egov.collection.model.enums.CollectionType;
import org.egov.collection.model.enums.OnlineStatusCode;
import org.egov.collection.model.enums.ReceiptStatus;
import org.egov.collection.repository.*;
import org.egov.collection.web.contract.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReceiptService {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(ReceiptService.class);

	@Autowired
	private ReceiptRepository receiptRepository;

	@Autowired
	private BusinessDetailsRepository businessDetailsRepository;

	@Autowired
	private ChartOfAccountsRepository chartOfAccountsRepository;

	@Autowired
	private CollectionApportionerService collectionApportionerService;

	@Autowired
	private BillingServiceRepository billingServiceRepository;

	@Autowired
	private InstrumentRepository instrumentRepository;

	@Autowired
	private IdGenRepository idGenRepository;

	@Autowired
	private CollectionConfigService collectionConfigService;

	public Pagination<ReceiptHeader> getReceipts(
			ReceiptSearchCriteria receiptSearchCriteria, RequestInfo requestInfo) {
		Pagination<ReceiptHeader> receiptHeaders = null;
		try {
			receiptHeaders = receiptRepository.findAllReceiptsByCriteria(
					receiptSearchCriteria, requestInfo);
		} catch (ParseException e) {
			LOGGER.error("Parse exception while parsing date" + e);
		}
		return receiptHeaders;
	}

	public Receipt apportionAndCreateReceipt(ReceiptReq receiptReq) {
		Receipt receipt = receiptReq.getReceipt().get(0);
		Bill bill = receipt.getBill().get(0);
		if(!validateBill(receiptReq.getRequestInfo(), bill)) {
			throw new CustomException(Long.valueOf(HttpStatus.BAD_REQUEST.toString()), CollectionServiceConstants.INVALID_BILL_EXCEPTION_DESC);
		}
		WorkflowDetailsRequest workflowDetailsRequest = receipt
				.getWorkflowDetails();
		bill.setBillDetails(apportionPaidAmount(receiptReq.getRequestInfo(),
				bill, receipt.getTenantId()));
		LOGGER.info("Bill object after apportioning: " + bill.toString());
		receipt = create(bill, receiptReq.getRequestInfo(),
				receipt.getTenantId(), receipt.getInstrument(),
				receipt.getOnlinePayment()); // sync call
		if (null != receipt.getBill()) {
			LOGGER.info("Pushing receipt to kafka queue");
			receipt.setWorkflowDetails(workflowDetailsRequest);
			receiptReq.setReceipt(Arrays.asList(receipt));
			receipt = receiptRepository.pushToQueue(receiptReq);
		}
		return receipt;
	}  

	private List<BillDetail> apportionPaidAmount(RequestInfo requestInfo,
			Bill bill, String tenantId) {
		Bill apportionBill = new Bill(bill.getId(), bill.getPayeeName(),
				bill.getPayeeAddress(), bill.getPayeeEmail(),
				bill.getIsActive(), bill.getIsCancelled(), bill.getPaidBy(),
				null, tenantId, bill.getMobileNumber());
		Boolean callBackForApportion = false;
		List<BillDetail> apportionBillDetails = new ArrayList<>();
		for (BillDetail billDetail : bill.getBillDetails()) {
			if (billDetail.getAmountPaid().longValueExact() > 0) {
				BusinessDetailsResponse businessDetailsRes = getBusinessDetails(
						billDetail.getBusinessService(), requestInfo, tenantId);
				if (billDetail.getAmountPaid() != billDetail.getTotalAmount()) {
					if (businessDetailsRes.getBusinessDetails().get(0)
							.getCallBackForApportioning()) {
						apportionBill.getBillDetails().add(billDetail);
						callBackForApportion = true;
					} else {
						billDetail
								.setBillAccountDetails(collectionApportionerService
										.apportionPaidAmount(billDetail
												.getAmountPaid(), billDetail
												.getBillAccountDetails()));
					}
				}
				apportionBillDetails.add(billDetail);
			}
		}
		if (callBackForApportion) {
			apportionBillDetails.addAll(billingServiceRepository
					.getApportionListFromBillingService(requestInfo,
							apportionBill).getBill().get(0).getBillDetails());
		}
		return apportionBillDetails;
	}

	public boolean validateGLCode(RequestInfo requestInfo, String tenantId,
			BillDetail billdetails) {
		for (BillAccountDetail billAccountDetail : billdetails
				.getBillAccountDetails()) {

			List<ChartOfAccount> chartOfAccounts = chartOfAccountsRepository
					.getChartOfAccounts(
							Arrays.asList(billAccountDetail.getGlcode()),
							tenantId, requestInfo);
			LOGGER.info("chartOfAccount: " + chartOfAccounts);
			if (chartOfAccounts.isEmpty()) {
				LOGGER.error("Glcode invalid!: "
						+ billAccountDetail.getGlcode());
				return false;
			}
		}
		return true;
	}

	public Boolean validateFundAndDept(
			BusinessDetailsResponse businessDetailsRes) {
		BusinessDetailsRequestInfo businessDetails;
		if (null != businessDetailsRes
				&& !businessDetailsRes.getBusinessDetails().isEmpty()) {
			businessDetails = businessDetailsRes.getBusinessDetails().get(0);
			if (null != businessDetails) {
				String fund = businessDetails.getFund();
				String department = businessDetails.getDepartment();
				if (StringUtils.isBlank(fund)) {
					LOGGER.error("Fund is not available");
					return false;
				}
				if (StringUtils.isBlank(department)) {
					LOGGER.error("Department not available");
					return false;
				}
				LOGGER.info("FUND: " + fund + " DEPARTMENT: " + department);
			}
			return true;
		}
		LOGGER.info("Returning false");
		return false;
	}

	private AuditDetails getAuditDetails(User user) {
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(user.getId());
		auditDetails.setLastModifiedBy(user.getId());
		auditDetails.setCreatedDate(new Date().getTime());
		auditDetails.setLastModifiedDate(new Date().getTime());
		return auditDetails;
	}

	public Receipt create(Bill bill, RequestInfo requestInfo, String tenantId,
			Instrument instrument, OnlinePayment onlinePayment) {
		LOGGER.info("Persisting recieptdetail");
		Receipt receipt = new Receipt();

		User user = requestInfo.getUserInfo();

		List<Role> roleList = requestInfo.getUserInfo() != null ? requestInfo
				.getUserInfo().getRoles() : new ArrayList<>();
		AuditDetails auditDetail = getAuditDetails(user);
		String transactionId = idGenRepository.generateTransactionNumber(
				requestInfo, tenantId);
		Instrument createdInstrument = null;
		Long receiptHeaderId = 0l;
		for (BillDetail billDetail : bill.getBillDetails()) {
			if (billDetail.getAmountPaid().longValueExact() > 0) {
				// TODO: Revert back once the workflow is enabled
				billDetail.setStatus(ReceiptStatus.APPROVED.toString());
				receiptHeaderId = receiptRepository.getNextSeqForRcptHeader();
				try {
					instrument.setTransactionType(TransactionType.Debit);
					instrument.setTenantId(tenantId);
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
							"dd/MM/yyyy");
					if (instrument
							.getInstrumentType()
							.getName()
							.equalsIgnoreCase(
									CollectionServiceConstants.INSTRUMENT_TYPE_CASH)
							|| instrument
									.getInstrumentType()
									.getName()
									.equalsIgnoreCase(
											CollectionServiceConstants.INSTRUMENT_TYPE_ONLINE)
                            //TODO fix for CITIZEN role
							/*&& user.getRoles() != null
							&& roleList
									.stream()
									.anyMatch(
											role -> CollectionServiceConstants.COLLECTION_ONLINE_RECEIPT_ROLE
													.equalsIgnoreCase(role.getCode()))*/) {

                        String transactionDate = simpleDateFormat
                                .format(new Date());
                        instrument.setTransactionDate(simpleDateFormat
                                .parse(transactionDate));
                        instrument.setTransactionNumber(transactionId);
                        if(onlinePayment == null) {
                            onlinePayment = new OnlinePayment();
                            onlinePayment.setTenantId(tenantId);
                            onlinePayment.setTransactionNumber(transactionId);
                            onlinePayment.setTransactionDate(simpleDateFormat
                                    .parse(transactionDate).getTime());
                            onlinePayment.setAuthorisationStatusCode(CollectionServiceConstants.ONLINE_PAYMENT_AUTHORISATION_SUCCESS_CODE);
                            onlinePayment.setRemarks(CollectionServiceConstants.ONLINE_PAYMENT_REMARKS);
                            onlinePayment.setStatus(OnlineStatusCode.SUCCESS.toString());
                            onlinePayment.setTransactionAmount(billDetail.getAmountPaid());
                        }
					} else {
						DateTime transactionDate = new DateTime(
								instrument.getTransactionDateInput());
						instrument.setTransactionDate(simpleDateFormat
								.parse(transactionDate.toString("dd/MM/yyyy")));
					}
					createdInstrument = instrumentRepository.createInstrument(
							requestInfo, instrument);
				} catch (Exception e) {
					LOGGER.error("Exception while creating instrument: ", e);
					throw new CustomException(
							Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR
									.toString()),
							CollectionServiceConstants.INSTRUMENT_EXCEPTION_MSG,
							CollectionServiceConstants.INSTRUMENT_EXCEPTION_DESC);
				}

				billDetail.setCollectionType(CollectionType.COUNTER);

				CollectionConfigGetRequest collectionConfigRequest = new CollectionConfigGetRequest();
				collectionConfigRequest.setTenantId(tenantId);
				collectionConfigRequest
						.setName(CollectionServiceConstants.MANUAL_RECEIPT_DETAILS_REQUIRED_CONFIG_KEY);
				Map<String, List<String>> manualReceiptConfiguration = collectionConfigService
						.getCollectionConfiguration(collectionConfigRequest);
				if (!manualReceiptConfiguration.isEmpty()
						&& manualReceiptConfiguration
								.get(CollectionServiceConstants.MANUAL_RECEIPT_DETAILS_REQUIRED_CONFIG_KEY)
								.get(0).equalsIgnoreCase("Yes")
						&& roleList
								.stream()
								.anyMatch(
										role -> CollectionServiceConstants.COLLECTION_LEGACY_RECEIPT_CREATOR_ROLE
												.contains(role.getName()))
						&& StringUtils.isNotEmpty(billDetail
								.getManualReceiptNumber())
						&& billDetail.getReceiptDate() != null) {
					billDetail.setReceiptDate(billDetail.getReceiptDate());
					billDetail.setManualReceiptNumber(billDetail
							.getManualReceiptNumber());
				} else {
					billDetail.setReceiptDate(new Date().getTime());
					billDetail.setManualReceiptNumber("");
				}
				String receiptNumber = idGenRepository.generateReceiptNumber(
						requestInfo, tenantId);
				try {
					validateReceiptNumber(receiptNumber, tenantId, requestInfo);
				} catch (CustomException e) {
					LOGGER.error("Duplicate Receipt: ", e);
					throw new CustomException(
							Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR
									.toString()),
							CollectionServiceConstants.DUPLICATE_RCPT_EXCEPTION_MSG,
							CollectionServiceConstants.DUPLICATE_RCPT_EXCEPTION_DESC);
				}

				if (instrument
						.getInstrumentType()
						.getName()
						.equalsIgnoreCase(
								CollectionServiceConstants.INSTRUMENT_TYPE_ONLINE)
						&& user.getRoles() != null
						&& roleList
								.stream()
								.anyMatch(
										role -> CollectionServiceConstants.COLLECTION_ONLINE_RECEIPT_ROLE
												.contains(role.getName()))) {
					billDetail.setReceiptNumber("");
				} else
					billDetail.setReceiptNumber(receiptNumber);
				Map<String, Object> parametersMap;
				BusinessDetailsResponse businessDetailsRes = getBusinessDetails(
						billDetail.getBusinessService(), requestInfo, tenantId);

				if (validateFundAndDept(businessDetailsRes)
						&& validateGLCode(requestInfo, tenantId, billDetail)) {
					BusinessDetailsRequestInfo businessDetails = businessDetailsRes
							.getBusinessDetails().get(0);
					if (null == businessDetails.getBusinessType()
							|| businessDetails.getBusinessType().isEmpty()) {
						throw new CustomException(
								Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR
										.toString()),
								CollectionServiceConstants.RCPT_TYPE_MISSING_FIELD,
								CollectionServiceConstants.RCPT_TYPE_MISSING_MESSAGE);
					}
					parametersMap = prepareReceiptHeader(bill, tenantId,
							auditDetail, billDetail, receiptHeaderId,
							businessDetails);
					parametersMap.put("transactionid", transactionId);
					LOGGER.info("Rcpt no generated: "
							+ billDetail.getReceiptNumber());
					LOGGER.info("InstrumentId: " + instrument.getId()
							+ " ReceiptHeaderId: " + receiptHeaderId);

					Map<String, Object>[] parametersReceiptDetails = prepareReceiptDetails(
							requestInfo, tenantId, billDetail, receiptHeaderId,
							createdInstrument);
					try {
						LOGGER.info("Persiting receipt to resp tables: "
								+ parametersMap.toString());
						receiptRepository.persistReceipt(parametersMap,
								parametersReceiptDetails, receiptHeaderId,
								createdInstrument.getId());
						if (instrument
								.getInstrumentType()
								.getName()
								.equalsIgnoreCase(
										CollectionServiceConstants.INSTRUMENT_TYPE_ONLINE))
							receiptRepository
									.insertOnlinePayments(onlinePayment,
											requestInfo, receiptHeaderId);
					} catch (Exception e) {
						LOGGER.error("Persisting receipt FAILED! ", e);
						return receipt;
					}

				} else {
					throw new CustomException(
							Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR
									.toString()),
							CollectionServiceConstants.BUSINESSDETAILS_EXCEPTION_MSG,
							CollectionServiceConstants.BUSINESSDETAILS_EXCEPTION_DESC);
				}
			}
		}
		receipt.setBill(Arrays.asList(bill));
		receipt.setAuditDetails(auditDetail);
		receipt.setTransactionId(transactionId);
		receipt.setTenantId(tenantId);
		receipt.setInstrument(createdInstrument);
		receipt.setOnlinePayment(onlinePayment);
		return receipt;
	}

	private Map<String, Object> prepareReceiptHeader(Bill bill,
			String tenantId, AuditDetails auditDetail, BillDetail billDetail,
			Long receiptHeaderId, BusinessDetailsRequestInfo businessDetails) {
		final Map<String, Object> parametersMap = new HashMap<>();
		String collectionModesNotAllowed = billDetail
				.getCollectionModesNotAllowed().toString().replace("[", "");
		collectionModesNotAllowed = collectionModesNotAllowed.replace("]", "");
		parametersMap.put("id", receiptHeaderId);
		parametersMap.put("payeename", bill.getPayeeName());
		parametersMap.put("payeeaddress", bill.getPayeeAddress());
		parametersMap.put("payeeemail", bill.getPayeeEmail());
		parametersMap.put("paidby", bill.getPaidBy());
		parametersMap.put("referencenumber", billDetail.getBillNumber());
		parametersMap.put("receipttype", businessDetails.getBusinessType());
		parametersMap.put("receiptdate", billDetail.getReceiptDate());
		parametersMap.put("receiptnumber", billDetail.getReceiptNumber());
		parametersMap.put("businessdetails", billDetail.getBusinessService());
		parametersMap.put("collectiontype", billDetail.getCollectionType()
				.toString());
		parametersMap.put("reasonforcancellation",
				billDetail.getReasonForCancellation());
		parametersMap.put("minimumamount", billDetail.getMinimumAmount());
		parametersMap.put("totalamount", billDetail.getAmountPaid());
		parametersMap.put("collmodesnotallwd", collectionModesNotAllowed);
		parametersMap.put("consumercode", billDetail.getConsumerCode());
		parametersMap.put("channel", billDetail.getChannel());
		parametersMap.put("fund", businessDetails.getFund());
		parametersMap.put("fundsource", businessDetails.getFundSource());
		parametersMap.put("function", businessDetails.getFunction());
		parametersMap.put("department", businessDetails.getDepartment());
		parametersMap.put("boundary", billDetail.getBoundary());
		parametersMap.put("voucherheader", billDetail.getVoucherHeader());
		// Deposited bank need not be persisted for every receipt it exists only
		// in few use cases
		parametersMap.put("depositedbranch", null);
		parametersMap.put("createdby", auditDetail.getCreatedBy());
		parametersMap.put("createddate", auditDetail.getCreatedDate());
		parametersMap.put("lastmodifiedby", auditDetail.getLastModifiedBy());
		parametersMap
				.put("lastmodifieddate", auditDetail.getLastModifiedDate());
		parametersMap.put("tenantid", tenantId);
		parametersMap.put("referencedate", billDetail.getBillDate());
		parametersMap.put("referencedesc", billDetail.getBillDescription());
		parametersMap.put("manualreceiptnumber", null);
		parametersMap.put("manualreceiptdate", null);
		parametersMap.put("reference_ch_id", null);
		parametersMap.put("stateid", null);
		parametersMap.put("location", null);
		parametersMap.put("isreconciled", false);
		parametersMap.put("status", billDetail.getStatus());
		parametersMap.put("manualreceiptnumber",
				billDetail.getManualReceiptNumber());
		return parametersMap;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object>[] prepareReceiptDetails(
			RequestInfo requestInfo, String tenantId, BillDetail billDetail,
			Long receiptHeaderId, Instrument instrument) {
		Map<String, Object>[] parametersReceiptDetails = new Map[billDetail
				.getBillAccountDetails().size() + 1];
		int parametersReceiptDetailsCount = 0;
		BigDecimal drAmount = BigDecimal.ZERO;
		for (BillAccountDetail billAccountDetails : billDetail
				.getBillAccountDetails()) {
			drAmount = drAmount.add(billAccountDetails.getCreditAmount());
			parametersReceiptDetails[parametersReceiptDetailsCount] = prepareBillAccountDetailsMap(
					requestInfo, tenantId, receiptHeaderId, billAccountDetails);
			parametersReceiptDetailsCount++;
		}
		parametersReceiptDetails[parametersReceiptDetailsCount++] = prepareBillAccountDetailsMap(
				requestInfo,
				tenantId,
				receiptHeaderId,
				addDebitAccountHeadDetails(requestInfo, drAmount, instrument,
						tenantId));
		return parametersReceiptDetails;
	}

	private Map<String, Object> prepareBillAccountDetailsMap(
			RequestInfo requestInfo, String tenantId, Long receiptHeaderId,
			BillAccountDetail billAccountDetails) {
		final Map<String, Object> parameterMap = new HashMap<>();
		List<ChartOfAccount> chartOfAccounts = chartOfAccountsRepository
				.getChartOfAccounts(
						Arrays.asList(billAccountDetails.getGlcode()),
						tenantId, requestInfo);
		if (!chartOfAccounts.isEmpty()) {
			parameterMap.put("chartofaccount", billAccountDetails.getGlcode());
			parameterMap.put("dramount", billAccountDetails.getDebitAmount());
			parameterMap.put("cramount", billAccountDetails.getCreditAmount());
			parameterMap.put("ordernumber", billAccountDetails.getOrder());
			parameterMap.put("actualcramounttobepaid",
					billAccountDetails.getCrAmountToBePaid());
			parameterMap.put("description",
					billAccountDetails.getAccountDescription());
			parameterMap.put("financialyear", null);
			parameterMap.put("isactualdemand",
					billAccountDetails.getIsActualDemand());
			parameterMap.put("purpose", billAccountDetails.getPurpose()
					.toString());
			parameterMap.put("tenantid", tenantId);
			parameterMap.put("receiptheader", receiptHeaderId);
		}
		return parameterMap;
	}

	public BusinessDetailsResponse getBusinessDetails(
			String businessDetailsCode, RequestInfo requestInfo, String tenantId) {
		LOGGER.info("Searching for fund aand other businessDetails based on code.");
		BusinessDetailsResponse businessDetailsResponse = new BusinessDetailsResponse();
		try {
			businessDetailsResponse = businessDetailsRepository
					.getBusinessDetails(Arrays.asList(businessDetailsCode),
							tenantId, requestInfo);
		} catch (Exception e) {
			LOGGER.error("Exception while fetching buisnessDetails: ", e);
			throw new CustomException(
					Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
					CollectionServiceConstants.BUSINESSDETAILS_EXCEPTION_MSG,
					CollectionServiceConstants.BUSINESSDETAILS_EXCEPTION_DESC);

		}
		if (null == businessDetailsResponse.getBusinessDetails()
				|| businessDetailsResponse.getBusinessDetails().isEmpty()) {
			LOGGER.info("Buisness " + businessDetailsResponse.toString());
			throw new CustomException(
					Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
					CollectionServiceConstants.BUSINESSDETAILS_EXCEPTION_MSG,
					CollectionServiceConstants.BUSINESSDETAILS_EXCEPTION_DESC);
		}
		LOGGER.info("Response from coll-master: " + businessDetailsResponse);
		return businessDetailsResponse;
	}

	public Boolean checkVoucherCreation(Boolean voucherCreation,
			Date voucherCutoffDate, Date receiptDate) {
		Boolean createVoucherForBillingService = Boolean.FALSE;
		if (voucherCutoffDate != null
				&& receiptDate.compareTo(voucherCutoffDate) > 0) {
			if (voucherCreation != null)
				createVoucherForBillingService = voucherCreation;
		} else if (voucherCutoffDate == null && voucherCreation != null)
			createVoucherForBillingService = voucherCreation;
		return createVoucherForBillingService;
	}

	public List<Receipt> cancelReceiptBeforeRemittance(ReceiptReq receiptRequest) {
		ReceiptReq request = receiptRepository.cancelReceipt(receiptRequest);
		return request.getReceipt();
	}

	public List<Receipt> cancelReceiptPushToQueue(ReceiptReq receiptRequest) {
		LOGGER.info("Pushing recieptdetails to kafka queue");
		return receiptRepository
				.pushReceiptCancelDetailsToQueue(receiptRequest);
	}

	public WorkflowDetailsRequest updateStateId(
			WorkflowDetailsRequest workflowDetailsRequest) {
		LOGGER.info("WorkflowDetailsRequest: "
				+ workflowDetailsRequest.toString());
		try {
			pushUpdateReceiptDetailsToQueque(workflowDetailsRequest);
		} catch (Exception e) {
			LOGGER.error("Couldn't update stateId and status: ", e);
			return null;
		}
		return workflowDetailsRequest;
	}

	public List<User> getReceiptCreators(final RequestInfo requestInfo,
			final String tenantId) {
		return receiptRepository.getReceiptCreators(requestInfo, tenantId);
	}

	public List<String> getReceiptStatus(final String tenantId) {
		return receiptRepository.getReceiptStatus(tenantId);
	}

	public void updateReceiptWithWorkFlowDetails(final ReceiptReq receiptReq)
			throws ValidationException {
		receiptRepository.updateReceipt(receiptReq);
	}

	public List<BusinessDetailsRequestInfo> getBusinessDetails(
			final String tenantId, final RequestInfo requestInfo) {
		return receiptRepository.getBusinessDetails(requestInfo, tenantId);
	}

	public List<ChartOfAccount> getChartOfAccountsForByGlCodes(
			final String tenantId, final RequestInfo requestInfo) {
		return receiptRepository.getChartOfAccounts(tenantId, requestInfo);
	}

	public void pushUpdateReceiptDetailsToQueque(
			WorkflowDetailsRequest workFlowDetailsRequest) {
		LOGGER.info("WorkflowDetailsRequest :" + workFlowDetailsRequest);
		receiptRepository.pushUpdateDetailsToQueque(workFlowDetailsRequest);
	}

	private BillAccountDetail addDebitAccountHeadDetails(
			RequestInfo requestInfo, BigDecimal drAmount,
			Instrument instrument, String tenantId) {
		LOGGER.info("Fetching glcode for instrument type: " + instrument);
		String glcode = null;
		try {
			glcode = instrumentRepository.getAccountCodeId(requestInfo,
					instrument, tenantId);
		} catch (Exception e) {
			LOGGER.error(
					"Exception while fetching glcode from instrument service: ",
					e);
			throw new CustomException(
					Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
					CollectionServiceConstants.ACCOUNT_CODE_EXCEPTION_MSG,
					CollectionServiceConstants.ACCOUNT_CODE_EXCEPTION_DESC);

		}
		LOGGER.info("glcode obtained is: " + glcode);
		BillAccountDetail billAccountDetail = new BillAccountDetail();
		billAccountDetail.setGlcode(glcode);
		billAccountDetail.setDebitAmount(drAmount);
		billAccountDetail.setCreditAmount(BigDecimal.ZERO);
		billAccountDetail.setPurpose(Purpose.OTHERS);

		return billAccountDetail;
	}

	public void validateReceiptNumber(String receiptNumber, String tenantId,
			RequestInfo requestInfo) {

		ReceiptSearchCriteria receiptSearchCriteria = new ReceiptSearchCriteria();
		receiptSearchCriteria.setTenantId(tenantId);
		List<String> receiptNumbers = new ArrayList<>();
		receiptNumbers.add(receiptNumber);
		receiptSearchCriteria.setReceiptNumbers(receiptNumbers);
		Pagination<ReceiptHeader> receipts = null;
		try {
			receipts = getReceipts(receiptSearchCriteria, requestInfo);

		} catch (Exception e) {
			throw new CustomException(
					Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
					CollectionServiceConstants.DUPLICATE_RCPT_EXCEPTION_MSG,
					CollectionServiceConstants.DUPLICATE_RCPT_EXCEPTION_DESC);
		}
		if (receipts != null && !receipts.getPagedData().isEmpty()) {
			throw new CustomException(
					Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
					CollectionServiceConstants.DUPLICATE_RCPT_EXCEPTION_MSG,
					CollectionServiceConstants.DUPLICATE_RCPT_EXCEPTION_DESC);
		}
	}

	public boolean validateBill(RequestInfo requestInfo, Bill bill) {
		boolean isBillValid = true;
		for (BillDetail billDetail : bill.getBillDetails()) {
			BillResponse billResponse = billingServiceRepository.getBillForBillId(requestInfo, bill, billDetail);
			if (null != billResponse && !CollectionUtils.isEmpty(billResponse.getBill())) {
				for(BillDetail resbillDetail: billResponse.getBill().get(0).getBillDetails()) {
					List<BillDetail> reqBillDetail = bill.getBillDetails().parallelStream()
							.filter(obj -> obj.getId() == resbillDetail.getId()).collect(Collectors.toList());
					if(CollectionUtils.isEmpty(reqBillDetail)) {
						isBillValid = false;
						break;
					}
					BillDetail billDetailTobeValidated = reqBillDetail.get(0);
					if(billDetailTobeValidated.getPartPaymentAllowed()) {
						if(resbillDetail.getMinimumAmount().longValue() > billDetailTobeValidated.getAmountPaid().longValue()) {
							isBillValid = false;
							break;
						}
					}else {
						if(!resbillDetail.getTotalAmount().equals(billDetailTobeValidated.getAmountPaid())) {
							isBillValid = false;
							break;
						}
					}

				}
			}else {
				isBillValid = false;
			}
		}
		return isBillValid;
	}

	public List<LegacyReceiptHeader> persistAndPushToQueue(
			LegacyReceiptReq legacyReceiptRequest) {
		LOGGER.info("LegacyReceiptReq" + legacyReceiptRequest.toString());
		LegacyReceiptReq legacyReceiptReq = receiptRepository
				.pushLegacyReceiptToDB(legacyReceiptRequest);
		return legacyReceiptReq.getLegacyReceipts();
	}

	public List<LegacyReceiptHeader> getLegacyReceiptsByCriteria(
			LegacyReceiptGetReq legacyReceiptGetReq) {
		LOGGER.info("LegacyReceiptGetReq:" + legacyReceiptGetReq.toString());
		return receiptRepository
				.getLegacyReceiptsByCriteria(legacyReceiptGetReq);
	}
}