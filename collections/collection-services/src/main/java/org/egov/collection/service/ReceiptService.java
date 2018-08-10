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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.collection.model.*;
import org.egov.collection.model.enums.CollectionType;
import org.egov.collection.model.enums.OnlineStatusCode;
import org.egov.collection.model.enums.ReceiptStatus;
import org.egov.collection.repository.*;
import org.egov.collection.util.ReceiptEnricher;
import org.egov.collection.util.ReceiptValidator;
import org.egov.collection.web.contract.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.egov.collection.config.CollectionServiceConstants.*;

@Service
@Slf4j
public class ReceiptService {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

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
    private ReceiptValidator receiptValidator;

    @Autowired
    private ReceiptEnricher receiptEnricher;


    public Pagination<ReceiptHeader> getReceipts(ReceiptSearchCriteria receiptSearchCriteria, RequestInfo requestInfo) {

        receiptValidator.validateSearchReceiptRequest(receiptSearchCriteria);
        Pagination<ReceiptHeader> receiptHeaders = null;
        receiptHeaders = receiptRepository.findAllReceiptsByCriteria(receiptSearchCriteria, requestInfo);

        return receiptHeaders;
    }

    public Receipt createReceipt(ReceiptReq receiptReq) {
        receiptEnricher.enrichReceipt(receiptReq);
        receiptValidator.validatecreateReceiptRequest(receiptReq);

        Receipt receipt = receiptReq.getReceipt().get(0);
        Bill bill = receipt.getBill().get(0);

        WorkflowDetailsRequest workflowDetailsRequest = receipt.getWorkflowDetails();

        List<BillDetail> billDetails = apportionPaidAmount(receiptReq.getRequestInfo(), bill, receipt.getTenantId());
        bill.setBillDetails(billDetails);
        log.info("Bill object after apportioning: " + bill.toString());

        receipt = create(bill, receiptReq.getRequestInfo(),
                receipt.getTenantId(), receipt.getInstrument(),
                receipt.getOnlinePayment()); // sync call

        log.info("Pushing receipt to kafka queue");
        receipt.setWorkflowDetails(workflowDetailsRequest);
        receiptReq.setReceipt(Collections.singletonList(receipt));
        receiptRepository.pushToQueue(receiptReq);

        return receipt;
    }

    private List<BillDetail> apportionPaidAmount(RequestInfo requestInfo, Bill bill, String tenantId) {
        Bill apportionBill = new Bill(bill.getId(), bill.getPayeeName(),
                bill.getPayeeAddress(), bill.getPayeeEmail(),
                bill.getIsActive(), bill.getIsCancelled(), bill.getPaidBy(),
                new ArrayList<>(), tenantId, bill.getMobileNumber());

        List<BillDetail> apportionBillDetails = new ArrayList<>();
        for (BillDetail billDetail : bill.getBillDetails()) {
            if (billDetail.getCallBackForApportioning()) {
                apportionBill.getBillDetails().add(billDetail);
            } else {
                billDetail.setBillAccountDetails(collectionApportionerService
                        .apportionPaidAmount(billDetail
                                .getAmountPaid(), billDetail
                                .getBillAccountDetails()));
                apportionBillDetails.add(billDetail);
            }
        }
        if (!apportionBill.getBillDetails().isEmpty()) {
            apportionBillDetails.addAll(billingServiceRepository.getApportionListFromBillingService(requestInfo,
                    apportionBill).getBill().get(0).getBillDetails());
        }
        return apportionBillDetails;
    }

    public Receipt create(Bill bill, RequestInfo requestInfo, String tenantId,
                          Instrument instrument, OnlinePayment onlinePayment) {
        log.info("Persisting receipt detail");
        Receipt receipt = new Receipt();

        User user = requestInfo.getUserInfo();

        AuditDetails auditDetail = getAuditDetails(user);
        String transactionId = idGenRepository.generateTransactionNumber(requestInfo, tenantId);

        Instrument createdInstrument = null;
        for (BillDetail billDetail : bill.getBillDetails()) {
            // TODO: Revert back once the workflow is enabled
            billDetail.setStatus(ReceiptStatus.APPROVED.toString());
            Long receiptHeaderId = receiptRepository.getNextSeqForRcptHeader();
            try {
                instrument.setTransactionType(TransactionType.Debit);
                instrument.setTenantId(tenantId);

                if (instrument.getInstrumentType().getName().equalsIgnoreCase(INSTRUMENT_TYPE_CASH) || instrument
                        .getInstrumentType().getName().equalsIgnoreCase(INSTRUMENT_TYPE_ONLINE)) {

                    String transactionDate = simpleDateFormat.format(new Date());
                    instrument.setTransactionDate(simpleDateFormat.parse(transactionDate));
                    instrument.setTransactionNumber(transactionId);
                    if (onlinePayment == null) {
                        onlinePayment = new OnlinePayment();
                        onlinePayment.setTenantId(tenantId);
                        onlinePayment.setTransactionNumber(transactionId);
                        onlinePayment.setTransactionDate(simpleDateFormat.parse(transactionDate).getTime());
                        onlinePayment.setAuthorisationStatusCode(ONLINE_PAYMENT_AUTHORISATION_SUCCESS_CODE);
                        onlinePayment.setRemarks(ONLINE_PAYMENT_REMARKS);
                        onlinePayment.setStatus(OnlineStatusCode.SUCCESS.toString());
                        onlinePayment.setTransactionAmount(billDetail.getAmountPaid());
                    }
                } else {
                    DateTime transactionDate = new DateTime(instrument.getTransactionDateInput());
                    instrument.setTransactionDate(simpleDateFormat.parse(transactionDate.toString("dd/MM/yyyy")));
                }
                createdInstrument = instrumentRepository.createInstrument(requestInfo, instrument);
            } catch (Exception e) {
                log.error("Exception while creating instrument: ", e);
                throw new CustomException(
                        INSTRUMENT_EXCEPTION_MSG,
                        INSTRUMENT_EXCEPTION_DESC);
            }

            billDetail.setCollectionType(CollectionType.COUNTER);
            billDetail.setReceiptDate(new Date().getTime());
//                billDetail.setManualReceiptNumber("");

            String receiptNumber = idGenRepository.generateReceiptNumber(requestInfo, tenantId);
            billDetail.setReceiptNumber(receiptNumber);

            BusinessDetailsResponse businessDetailsRes = getBusinessDetails(billDetail.getBusinessService(), requestInfo, tenantId);

            if (validateFundAndDept(businessDetailsRes)) {
                BusinessDetailsRequestInfo businessDetails = businessDetailsRes.getBusinessDetails().get(0);
                if (null == businessDetails.getBusinessType()
                        || businessDetails.getBusinessType().isEmpty()) {
                    throw new org.egov.tracer.model.CustomException(
                            RCPT_TYPE_MISSING_CODE,
                            RCPT_TYPE_MISSING_MESSAGE);
                }

                Map<String, Object> parametersMap = prepareReceiptHeader(bill, tenantId, auditDetail, billDetail,
                        receiptHeaderId, businessDetails);
                parametersMap.put("transactionid", transactionId);

                Map<String, Object>[] parametersReceiptDetails = prepareReceiptDetails(requestInfo, tenantId, billDetail, receiptHeaderId, createdInstrument);
                try {
                    log.info("Persiting receipt to resp tables: "
                            + parametersMap.toString());
                    receiptRepository.persistReceipt(parametersMap, parametersReceiptDetails, receiptHeaderId,
                            createdInstrument.getId());
                    if (instrument
                            .getInstrumentType()
                            .getName()
                            .equalsIgnoreCase(
                                    INSTRUMENT_TYPE_ONLINE))
                        receiptRepository
                                .insertOnlinePayments(onlinePayment,
                                        requestInfo, receiptHeaderId);
                } catch (Exception e) {
                    log.error("Persisting receipt FAILED! ", e);
                    return receipt;
                }

            } else {
                throw new CustomException(
                        BUSINESSDETAILS_EXCEPTION_MSG,
                        BUSINESSDETAILS_EXCEPTION_DESC);
            }
        }
        receipt.setBill(Collections.singletonList(bill));
        receipt.setAuditDetails(auditDetail);
        receipt.setTransactionId(transactionId);
        receipt.setTenantId(tenantId);
        receipt.setInstrument(createdInstrument);
        receipt.setOnlinePayment(onlinePayment);
        return receipt;
    }


    private Boolean validateFundAndDept(BusinessDetailsResponse businessDetailsRes) {
        BusinessDetailsRequestInfo businessDetails;
        if (null != businessDetailsRes
                && !businessDetailsRes.getBusinessDetails().isEmpty()) {
            businessDetails = businessDetailsRes.getBusinessDetails().get(0);
            if (null != businessDetails) {
                String fund = businessDetails.getFund();
                String department = businessDetails.getDepartment();
                if (StringUtils.isBlank(fund)) {
                    log.error("Fund is not available");
                    return false;
                }
                if (StringUtils.isBlank(department)) {
                    log.error("Department not available");
                    return false;
                }
                log.info("FUND: " + fund + " DEPARTMENT: " + department);
            }
            return true;
        }
        log.info("Returning false");
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
//		parametersMap.put("manualreceiptnumber", null);
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
    private Map<String, Object>[] prepareReceiptDetails(RequestInfo requestInfo, String tenantId, BillDetail billDetail,
                                                        Long receiptHeaderId, Instrument instrument) {
        Map<String, Object>[] parametersReceiptDetails = new Map[billDetail.getBillAccountDetails().size() + 1];
        int parametersReceiptDetailsCount = 0;
        BigDecimal drAmount = BigDecimal.ZERO;
        for (BillAccountDetail billAccountDetails : billDetail.getBillAccountDetails()) {
            drAmount = drAmount.add(billAccountDetails.getCreditAmount());
            parametersReceiptDetails[parametersReceiptDetailsCount] = prepareBillAccountDetailsMap(
                    tenantId, receiptHeaderId, billAccountDetails);
            parametersReceiptDetailsCount++;
        }
        BillAccountDetail accountDetail = addDebitAccountHeadDetails(requestInfo, drAmount, instrument, tenantId);
        parametersReceiptDetails[parametersReceiptDetailsCount++] = prepareBillAccountDetailsMap(
                tenantId,
                receiptHeaderId,
                accountDetail);
        return parametersReceiptDetails;
    }

    private Map<String, Object> prepareBillAccountDetailsMap(String tenantId, Long receiptHeaderId,
                                                             BillAccountDetail billAccountDetails) {
        final Map<String, Object> parameterMap = new HashMap<>();
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
        return parameterMap;
    }

    private BusinessDetailsResponse getBusinessDetails(
            String businessDetailsCode, RequestInfo requestInfo, String tenantId) {
        log.info("Searching for fund aand other businessDetails based on code.");
        BusinessDetailsResponse businessDetailsResponse;
        try {
            businessDetailsResponse = businessDetailsRepository
                    .getBusinessDetails(Collections.singletonList(businessDetailsCode),
                            tenantId, requestInfo);
        } catch (Exception e) {
            log.error("Exception while fetching buisnessDetails: ", e);
            throw new CustomException(
                    BUSINESSDETAILS_EXCEPTION_MSG,
                    BUSINESSDETAILS_EXCEPTION_DESC);

        }
        if (null == businessDetailsResponse.getBusinessDetails()
                || businessDetailsResponse.getBusinessDetails().isEmpty()) {
            log.info("Buisness " + businessDetailsResponse.toString());
            throw new CustomException(
                    BUSINESSDETAILS_EXCEPTION_MSG,
                    BUSINESSDETAILS_EXCEPTION_DESC);
        }
        log.info("Response from coll-master: " + businessDetailsResponse);
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
        log.info("Pushing recieptdetails to kafka queue");
        return receiptRepository
                .pushReceiptCancelDetailsToQueue(receiptRequest);
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

    private void pushUpdateReceiptDetailsToQueque(
            WorkflowDetailsRequest workFlowDetailsRequest) {
        log.info("WorkflowDetailsRequest :" + workFlowDetailsRequest);
        receiptRepository.pushUpdateDetailsToQueque(workFlowDetailsRequest);
    }

    private BillAccountDetail addDebitAccountHeadDetails(RequestInfo requestInfo, BigDecimal drAmount,
                                                         Instrument instrument, String tenantId) {
        log.info("Fetching glcode for instrument type: " + instrument);
        String glcode = null;
        try {
            glcode = instrumentRepository.getAccountCodeId(requestInfo,
                    instrument, tenantId);
        } catch (Exception e) {
            log.error(
                    "Exception while fetching glcode from instrument service: ",
                    e);
            throw new CustomException(
                    ACCOUNT_CODE_EXCEPTION_MSG,
                    ACCOUNT_CODE_EXCEPTION_DESC);

        }
        log.info("glcode obtained is: " + glcode);
        BillAccountDetail billAccountDetail = new BillAccountDetail();
        billAccountDetail.setGlcode(glcode);
        billAccountDetail.setDebitAmount(drAmount);
        billAccountDetail.setCreditAmount(BigDecimal.ZERO);
        billAccountDetail.setPurpose(Purpose.OTHERS);

        return billAccountDetail;
    }

    public List<LegacyReceiptHeader> persistAndPushToQueue(
            LegacyReceiptReq legacyReceiptRequest) {
        log.info("LegacyReceiptReq" + legacyReceiptRequest.toString());
        LegacyReceiptReq legacyReceiptReq = receiptRepository
                .pushLegacyReceiptToDB(legacyReceiptRequest);
        return legacyReceiptReq.getLegacyReceipts();
    }

    public List<LegacyReceiptHeader> getLegacyReceiptsByCriteria(
            LegacyReceiptGetReq legacyReceiptGetReq) {
        log.info("LegacyReceiptGetReq:" + legacyReceiptGetReq.toString());
        return receiptRepository
                .getLegacyReceiptsByCriteria(legacyReceiptGetReq);
    }
}