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

import com.jayway.jsonpath.JsonPath;
import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.model.*;
import org.egov.collection.model.enums.CollectionType;
import org.egov.collection.model.enums.ReceiptStatus;
import org.egov.collection.repository.BusinessDetailsRepository;
import org.egov.collection.repository.ChartOfAccountsRepository;
import org.egov.collection.repository.ReceiptRepository;
import org.egov.collection.web.contract.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
public class ReceiptService {

	public static final Logger logger = LoggerFactory
			.getLogger(ReceiptService.class);

	@Autowired
	private ReceiptRepository receiptRepository;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private BusinessDetailsRepository businessDetailsRepository;

    @Autowired
    private ChartOfAccountsRepository chartOfAccountsRepository;

	@Autowired
	private CollectionApportionerService collectionApportionerService;

	public ReceiptCommonModel getReceipts(
			ReceiptSearchCriteria receiptSearchCriteria) {
		return receiptRepository
				.findAllReceiptsByCriteria(receiptSearchCriteria);
	}

	public Receipt apportionAndCreateReceipt(ReceiptReq receiptReq) {
		logger.info("Pushing recieptdetail to kafka queue");
		Bill bill = receiptReq.getReceipt().get(0).getBill().get(0);
		Bill apportionBill = bill;
		Boolean callBackForApportion = false;
		for (BillDetail billdetail : bill.getBillDetails()) {
			BusinessDetailsResponse businessDetailsRes = getBusinessDetails(
					billdetail.getBusinessService(), receiptReq);
			if (validateFundAndDept(businessDetailsRes)
					&& validateGLCode(receiptReq.getRequestInfo(),
							receiptReq.getTenantId(), billdetail)) {
				businessDetailsRes.getBusinessDetails().get(0).setCallBackForApportioning(false); //temporary
				if (businessDetailsRes.getBusinessDetails().get(0)
						.getCallBackForApportioning()) {
					bill.getBillDetails().remove(billdetail);
					apportionBill.getBillDetails().add(billdetail);
					callBackForApportion = true;
				} else {
					billdetail
							.setBillAccountDetails(collectionApportionerService
									.apportionPaidAmount(
											billdetail.getAmountPaid(),
											billdetail.getBillAccountDetails()));
				}
			}else{
				return null;
			}

		}
		if (callBackForApportion)
			apportionBill = getApportionListFromBillingService(
					receiptReq.getRequestInfo(), apportionBill).get(0);
		bill.getBillDetails().addAll(apportionBill.getBillDetails());
		receiptReq.getReceipt().get(0).getBill().clear();
		receiptReq.getReceipt().get(0).getBill().add(bill);
		setReceiptNumber(receiptReq);
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(receiptReq.getRequestInfo().getUserInfo()
                .getId());
		auditDetails.setLastModifiedBy(receiptReq.getRequestInfo()
                .getUserInfo().getId());
		auditDetails.setCreatedDate(new Date().getTime());
		auditDetails.setLastModifiedDate(new Date().getTime());
		receiptReq.getReceipt().get(0).setAuditDetails(auditDetails);
		// return receiptRepository.pushToQueue(receiptReq); //async call

		/*
		 * Long instrumentid = getInstrumentId(receiptReq.getReceipt().get(0));
		 * if(null == instrumentid || 0L == Long.valueOf(instrumentid)){ return
		 * null; }else{ receipt = create(receiptReq); //sync call }
		 */// uncomment while enabling instrument integration

		Receipt receipt = create(receiptReq); // sync call
		if (null != receipt)
			receiptRepository.pushToQueue(receiptReq);

		return receipt;
	}

	private void setReceiptNumber(ReceiptReq receiptReq) {
		Bill bill = receiptReq.getReceipt().get(0).getBill().get(0);
		for (BillDetail billdetail : bill.getBillDetails()) {
			String receiptNumber = generateReceiptNumber(
					receiptReq.getRequestInfo(), bill.getTenantId());
			logger.info("Receipt Number generated is: " + receiptNumber);
			billdetail.setReceiptNumber(receiptNumber);
		}
	}

	public boolean validateGLCode(RequestInfo requestInfo, String tenantId,
			BillDetail billdetails) {
		for (BillAccountDetail billAccountDetail : billdetails
				.getBillAccountDetails()) {
	
            List<ChartOfAccount> chartOfAccounts = chartOfAccountsRepository.getChartOfAccounts(Arrays.asList(billAccountDetail.getGlcode()), tenantId, requestInfo);
			logger.info("chartOfAccount: " + chartOfAccounts);
			if (chartOfAccounts.isEmpty()) {
				logger.error("Glcode invalid!: "
						+ billAccountDetail.getGlcode());
				return false;
			}
		}
		return true;
	}

	public List<Bill> getApportionListFromBillingService(
			RequestInfo requestInfo, Bill apportionBill) {
		logger.info("Apportion Paid Amount in Billing Service");
		StringBuilder uriForApportion = new StringBuilder();
		uriForApportion
				.append(applicationProperties.getBillingServiceHostName())
				.append(applicationProperties.getBillingServiceApportion())
				.append("&tenantId=").append(apportionBill.getTenantId());
		logger.info("URI For Apportioning Paid Amount in Billing Service: "
				+ uriForApportion.toString());
		BillRequest billRequest = new BillRequest();
		billRequest.setRequestInfo(requestInfo);
		billRequest.getBills().add(apportionBill);
		BillResponse response = null;
		try {
			response = restTemplate.postForObject(uriForApportion.toString(),
					billRequest, BillResponse.class);
		} catch (Exception e) {
			logger.error("Error while apportioning paid amount from billing service. "
					+ e);
		}
		logger.info("Response from coll-master: " + response);
		return response.getBill();
	}

	public Boolean validateFundAndDept(
			BusinessDetailsResponse businessDetailsRes) {
		//TODO: Vishal: After validation prepare the Response with Error Codes and Error Fields 
		if (null == businessDetailsRes) {
			logger.error("All business details fields are not available");
			return false;
		} else {
			String fund = null;
			String department = null;
			try {
				fund = businessDetailsRes.getBusinessDetails().get(0).getFund();
				department = businessDetailsRes.getBusinessDetails().get(0)
						.getDepartment();
			} catch (Exception e) {
				logger.error("Fund or Department not available" + e);
				return false;
			}
			if (null == fund || fund.isEmpty()) {
				logger.error("Fund is not available");
				return false;
			} else if (null == department || department.isEmpty()) {
				logger.error("Department not available");
				return false;
			}

			logger.info("FUND: " + fund + " DEPARTMENT: " + department);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public Receipt create(ReceiptReq receiptReq) {
		logger.info("Persisting recieptdetail");

		Receipt receiptInfo = receiptReq.getReceipt().get(0);
		long receiptHeaderId = 0L;

		for (BillDetail billdetails : receiptInfo.getBill().get(0)
				.getBillDetails()) {
			billdetails.setCollectionType(CollectionType.COUNTER);
			billdetails.setStatus((ReceiptStatus.TOBESUBMITTED).toString());
			billdetails.setReceiptDate(new Date().getTime());
			final Map<String, Object> parametersMap = new HashMap<>();
			BusinessDetailsResponse businessDetailsRes = getBusinessDetails(
					billdetails.getBusinessService(), receiptReq);

			if (validateFundAndDept(businessDetailsRes)) {
				BusinessDetailsRequestInfo businessDetails = businessDetailsRes
						.getBusinessDetails().get(0);
				parametersMap.put("payeename", receiptInfo.getBill().get(0)
						.getPayeeName());
				parametersMap.put("payeeaddress", receiptInfo.getBill().get(0)
						.getPayeeAddress());
				parametersMap.put("payeeemail", receiptInfo.getBill().get(0)
						.getPayeeEmail());
				parametersMap.put("paidby", receiptInfo.getBill().get(0)
						.getPaidBy());
				parametersMap.put("referencenumber",
						billdetails.getBillNumber());
				parametersMap.put("receipttype",
						businessDetails.getBusinessType());
				parametersMap.put("receiptdate", billdetails.getReceiptDate());
				parametersMap.put("receiptnumber",
						billdetails.getReceiptNumber());
				parametersMap.put("businessdetails",
						billdetails.getBusinessService());
				parametersMap.put("collectiontype", billdetails
						.getCollectionType().toString());
				parametersMap.put("reasonforcancellation",
						billdetails.getReasonForCancellation());
				parametersMap.put("minimumamount",
						billdetails.getMinimumAmount());
				parametersMap.put("totalamount", billdetails.getTotalAmount());
				parametersMap.put("collmodesnotallwd", billdetails
						.getCollectionModesNotAllowed().toString());
				parametersMap
						.put("consumercode", billdetails.getConsumerCode());
				parametersMap.put("channel", billdetails.getChannel());
				parametersMap.put("fund", businessDetails.getFund());
				parametersMap
						.put("fundsource", businessDetails.getFundSource());
				parametersMap.put("function", businessDetails.getFunction());
				parametersMap
						.put("department", businessDetails.getDepartment());
				parametersMap.put("boundary", billdetails.getBoundary());
				parametersMap.put("voucherheader",
						billdetails.getVoucherHeader());
				parametersMap.put("depositedbranch", receiptInfo
						.getBankAccount().getBankBranch().getName());
				parametersMap.put("createdby", receiptInfo.getAuditDetails()
						.getCreatedBy());
				parametersMap.put("createddate", receiptInfo.getAuditDetails()
						.getCreatedDate());
				parametersMap.put("lastmodifiedby", receiptInfo
						.getAuditDetails().getLastModifiedBy());
				parametersMap.put("lastmodifieddate", receiptInfo
						.getAuditDetails().getLastModifiedDate());
				parametersMap.put("tenantid", receiptInfo.getTenantId());
				parametersMap.put("referencedate", billdetails.getBillDate());
				parametersMap.put("referencedesc",
						billdetails.getBillDescription());
				parametersMap.put("manualreceiptnumber", null);
				parametersMap.put("manualreceiptdate", null);
				parametersMap.put("reference_ch_id", null);
				parametersMap.put("stateid", null);
				parametersMap.put("location", null);
				parametersMap.put("isreconciled", false);
				parametersMap.put("status", billdetails.getStatus());

				try {
					receiptHeaderId = receiptRepository.persistToReceiptHeader(
							parametersMap, receiptInfo);
				} catch (Exception e) {
					logger.info("Persisting into receiptheader failed for rcpt: "
							+ billdetails.getReceiptNumber());
				}
				if (receiptHeaderId == 0L) {
					break;
				}

				/*
				 * receiptRepository.persistIntoReceiptInstrument(Long.valueOf(
				 * receiptReq.getReceipt().get(0).getInstrument().getId()),
				 * receiptHeaderId);
				 */// should be uncommented while enabling instrument
					// integration

				Map<String, Object>[] parametersReceiptDetails = new Map[billdetails
						.getBillAccountDetails().size()];
				int parametersReceiptDetailsCount = 0;
				for (BillAccountDetail billAccountDetails : billdetails
						.getBillAccountDetails()) {
					final Map<String, Object> parameterMap = new HashMap<>();
                    List<ChartOfAccount> chartOfAccounts = chartOfAccountsRepository.getChartOfAccounts(Arrays.asList(billAccountDetails.getGlcode()),
                            receiptReq.getReceipt().get(0).getTenantId(),receiptReq.getRequestInfo());
					if (!chartOfAccounts.isEmpty()) {
						parameterMap.put("chartofaccount",
								billAccountDetails.getGlcode());
						parameterMap.put("dramount",
								billAccountDetails.getDebitAmount());
						parameterMap.put("cramount",
								billAccountDetails.getCreditAmount());
						parameterMap.put("ordernumber",
								billAccountDetails.getOrder());
						parameterMap.put("receiptheader", receiptHeaderId);
						parameterMap.put("actualcramounttobepaid",
								billAccountDetails.getCreditAmount());
						parameterMap.put("description", null);
						parameterMap.put("financialyear", null);
						parameterMap.put("isactualdemand",
								billAccountDetails.getIsActualDemand());
						parameterMap.put("purpose", billAccountDetails
								.getPurpose().toString());
						parameterMap.put("tenantid", receiptInfo.getTenantId());

						parametersReceiptDetails[parametersReceiptDetailsCount] = parameterMap;
						parametersReceiptDetailsCount++;
					} else {
						logger.info("Glcode invalid, Hence record not inserted for COA/Gl Code: "
								+ billAccountDetails.getGlcode());
						break;
					}
				}
				try {
					receiptRepository.persistToReceiptDetails(
							parametersReceiptDetails, receiptHeaderId);
				} catch (Exception e) {
					logger.info("Persisting into receiptdetails failed for rcpt: "
							+ billdetails.getReceiptNumber());
				}
			}
		}
		return receiptReq.getReceipt().get(0);
	}

	public BusinessDetailsResponse getBusinessDetails(
			String businessDetailsCode, ReceiptReq receiptReq) {
		logger.info("Searching for fund aand other businessDetails based on code.");
		BusinessDetailsResponse businessDetailsResponse = new BusinessDetailsResponse();
		try {
			businessDetailsResponse = businessDetailsRepository
					.getBusinessDetails(Arrays.asList(businessDetailsCode),
                            receiptReq.getReceipt().get(0).getTenantId(),
                            receiptReq.getRequestInfo());
		} catch (Exception e) {
			logger.error("Error while fetching buisnessDetails from coll-master service. "
					+ e);
			return null;
		}

		logger.info("Response from coll-master: "
				+ businessDetailsResponse.toString());
		return businessDetailsResponse;
	}

	public String generateReceiptNumber(RequestInfo requestInfo, String tenantId) {
		logger.info("Generating receipt number for the receipt.");

		StringBuilder builder = new StringBuilder();
		String hostname = applicationProperties.getEgovServiceHost();
		String baseUri = applicationProperties.getIdGeneration();
		builder.append(hostname).append(baseUri);

		logger.info("URI being hit: " + builder.toString());

		IdRequestWrapper idRequestWrapper = new IdRequestWrapper();
		IdGenRequestInfo idGenReq = new IdGenRequestInfo();

		// Because idGen Svc uses a slightly different form of requestInfo

		idGenReq.setAction(requestInfo.getAction());
		idGenReq.setApiId(requestInfo.getApiId());
		idGenReq.setAuthToken(requestInfo.getAuthToken());
		idGenReq.setCorrelationId(requestInfo.getCorrelationId());
		idGenReq.setDid(requestInfo.getDid());
		idGenReq.setKey(requestInfo.getKey());
		idGenReq.setMsgId(requestInfo.getMsgId());
		idGenReq.setRequesterId(requestInfo.getRequesterId());
		idGenReq.setTs(requestInfo.getTs().getTime()); // this
														// is
														// the
														// difference.
		idGenReq.setUserInfo(requestInfo.getUserInfo());
		idGenReq.setVer(requestInfo.getVer());

		IdRequest idRequest = new IdRequest();
		idRequest.setIdName(CollectionServiceConstants.COLL_ID_NAME);
		idRequest.setTenantId(tenantId);
		idRequest.setFormat(CollectionServiceConstants.COLL_ID_FORMAT);

		List<IdRequest> idRequests = new ArrayList<>();
		idRequests.add(idRequest);

		idRequestWrapper.setIdGenRequestInfo(idGenReq);
		idRequestWrapper.setIdRequests(idRequests);
		Object response = null;

		try {
			response = restTemplate.postForObject(builder.toString(),
					idRequestWrapper, Object.class);
		} catch (Exception e) {
			logger.error("Error while generating receipt number. " + e);
			return null;

		}
		logger.info("Response from id gen service: " + response.toString());

		return JsonPath.read(response, "$.idResponses[0].id");
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
		logger.info("Pushing recieptdetails to kafka queue");
		return receiptRepository
				.pushReceiptCancelDetailsToQueue(receiptRequest);
	}

	public WorkflowDetails updateStateId(WorkflowDetails workflowDetails) {
		logger.info("WorkflowDetails: " + workflowDetails.toString());
		// update repo call
		return workflowDetails;
	}

	public List<User> getReceiptCreators(final RequestInfo requestInfo,
			final String tenantId) {
		return receiptRepository.getReceiptCreators(requestInfo, tenantId);
	}

	public List<String> getReceiptStatus(final String tenantId) {
		return receiptRepository.getReceiptStatus(tenantId);
	}

	public Long getInstrumentId(Receipt receipt) {
		Long instrumentId = null;
		StringBuilder builder = new StringBuilder();
		String hostname = applicationProperties.getEgovServiceHost();
		String baseUri = applicationProperties.getCreateInstrument();
		builder.append(hostname).append(baseUri);
		Instrument instrument = receipt.getInstrument();
		Object response = null;
		try {
			response = restTemplate.postForObject(builder.toString(),
					instrument, Object.class);
		} catch (Exception e) {
			logger.error(
					"Couldn't create instrument in the instrument service.",
					e.getCause());
			return instrumentId;
		}
		logger.info("Response from instrument service: " + response.toString());

		try {
            //To do add proper code
         logger.error("Empty try block");
		} catch (Exception e) {
			logger.error(
					"Couldn't fetch instrument id from instrument service.",
					e.getCause());
			return instrumentId;
		}
		return instrumentId;
	}

	public void pushUpdateReceiptDetailsToQueque(Long id, Long stateId,
			String status, String tenantId, RequestInfo requestInfo) {
		ReceiptSearchCriteria receiptSearchCriteria = ReceiptSearchCriteria
				.builder().tenantId(tenantId).ids(Arrays.asList(id)).build();
		List<Receipt> receipts = receiptRepository.findAllReceiptsByCriteria(
				receiptSearchCriteria).toDomainContract();
		receipts.get(0).getBill().get(0).getBillDetails().get(0)
				.setStatus(status);
		receipts.get(0).setStateId(stateId);
		ReceiptReq receiptRequest = new ReceiptReq();
		receiptRequest.setRequestInfo(requestInfo);
		receiptRequest.setReceipt(receipts);
		receiptRepository.pushUpdateDetailsToQueque(receiptRequest);
	}

	public Boolean updateReceipt(ReceiptReq receiptRequest) {
		logger.info("ReceiptRequest:" + receiptRequest);
		return receiptRepository.updateReceipt(receiptRequest);

	}

	public List<BusinessDetailsRequestInfo> getBusinessDetails(
			final String tenantId, final RequestInfo requestInfo) {
		return receiptRepository.getBusinessDetails(requestInfo, tenantId);
	}

    public List<ChartOfAccount> getChartOfAccountsForByGlCodes(final String tenantId, final RequestInfo requestInfo) {
        return receiptRepository.getChartOfAccounts(tenantId,requestInfo);
    }

}
