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

package org.egov.collection.repository;

import lombok.AllArgsConstructor;
import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.exception.CustomException;
import org.egov.collection.model.*;
import org.egov.collection.model.enums.ReceiptStatus;
import org.egov.collection.producer.CollectionProducer;
import org.egov.collection.repository.querybuilder.ReceiptDetailQueryBuilder;
import org.egov.collection.repository.rowmapper.LegacyReceiptDetailRowMapper;
import org.egov.collection.repository.rowmapper.LegacyReceiptHeaderRowMapper;
import org.egov.collection.repository.rowmapper.ReceiptDetaiRowMapper;
import org.egov.collection.repository.rowmapper.ReceiptRowMapper;
import org.egov.collection.web.contract.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.xml.bind.ValidationException;
import java.text.ParseException;
import java.util.*;

@AllArgsConstructor
@Repository
public class ReceiptRepository {
    public static final Logger logger = LoggerFactory
            .getLogger(ReceiptRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CollectionProducer collectionProducer;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ReceiptDetailQueryBuilder receiptDetailQueryBuilder;

    @Autowired
    private ReceiptRowMapper receiptRowMapper;

    @Autowired
    private LegacyReceiptHeaderRowMapper legacyReceiptHeaderRowMapper;

    @Autowired
    private ReceiptDetaiRowMapper receiptDetaiRowMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private BusinessDetailsRepository businessDetailsRepository;

    @Autowired
    private ChartOfAccountsRepository chartOfAccountsRepository;

    @Autowired
    private LegacyReceiptDetailRowMapper legacyReceiptDetailRowMapper;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ReceiptRepository(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            JdbcTemplate jdbcTemplate, CollectionProducer collectionProducer,
            ApplicationProperties applicationProperties,
            ReceiptDetailQueryBuilder receiptDetailQueryBuilder,
            ReceiptRowMapper receiptRowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.collectionProducer = collectionProducer;
        this.applicationProperties = applicationProperties;
        this.receiptDetailQueryBuilder = receiptDetailQueryBuilder;
        this.receiptRowMapper = receiptRowMapper;

    }

    public Receipt pushToQueue(ReceiptReq receiptReq) {
        try {
            collectionProducer.producer(
                    applicationProperties.getCreateReceiptTopicName(),
                    applicationProperties.getCreateReceiptTopicKey(),
                    receiptReq);

        } catch (Exception e) {
            logger.error("Pushing to Queue FAILED! ", e.getMessage());
            throw new CustomException(Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
                    CollectionServiceConstants.KAFKA_PUSH_EXCEPTION_MSG, CollectionServiceConstants.KAFKA_PUSH_EXCEPTION_DESC);
        }
        return receiptReq.getReceipt().get(0);
    }

    public void persistToReceiptHeader(Map<String, Object> parametersMap) {
        logger.info("Inserting into receipt header");
        String query = ReceiptDetailQueryBuilder.insertReceiptHeader();
        namedParameterJdbcTemplate.update(query, parametersMap);
    }

    public void persistToReceiptDetails(Map<String, Object>[] parametersReceiptDetails) {
        String queryReceiptDetails = ReceiptDetailQueryBuilder
                .insertReceiptDetails();
        namedParameterJdbcTemplate.batchUpdate(queryReceiptDetails,
                parametersReceiptDetails);
    }

    public boolean persistReceipt(Map<String, Object> parametersMap,
            Map<String, Object>[] parametersReceiptDetails, long receiptHeader, String instrumentId) {

        persistToReceiptHeader(parametersMap);
        persistToReceiptDetails(parametersReceiptDetails);
        persistIntoReceiptInstrument(instrumentId, receiptHeader, parametersMap.get("tenantid").toString());

        return true;

    }

    private Pagination<?> getPagination(String searchQuery, Pagination<?> page, Map<String, Object> paramValues) {
        String countQuery = "select count(*) from (" + searchQuery + ") as x";
        Long count = namedParameterJdbcTemplate.queryForObject(countQuery.toString(), paramValues, Long.class);
        Integer totalpages = (int) Math.ceil((double) count / page.getPageSize());
        page.setTotalPages(totalpages);
        page.setCurrentPage(page.getOffset());
        return page;
    }

    public Pagination<ReceiptHeader> findAllReceiptsByCriteria(
            ReceiptSearchCriteria receiptSearchCriteria, RequestInfo requestInfo) throws ParseException {
        Map<String, Object> preparedStatementValues = new HashMap<>();

        String receiptHeaderQuery = receiptDetailQueryBuilder.getQuery(
                receiptSearchCriteria, preparedStatementValues);

        String receiptDetailsQuery = receiptDetailQueryBuilder.getReceiptDetailByReceiptHeader();
        Pagination<ReceiptHeader> page = new Pagination<>();
        if (receiptSearchCriteria.getOffset() != null) {
            page.setOffset(receiptSearchCriteria.getOffset());
        }
        if (receiptSearchCriteria.getPageSize() != null) {
            page.setPageSize(receiptSearchCriteria.getPageSize());
        }
        page = (Pagination<ReceiptHeader>) getPagination(receiptHeaderQuery, page, preparedStatementValues);

        receiptHeaderQuery = receiptHeaderQuery + " limit :limit OFFSET :offset";
        int pageSize = receiptSearchCriteria.getPageSize() != null ? receiptSearchCriteria.getPageSize() : Pagination.DEFAULT_PAGE_SIZE;
        preparedStatementValues.put("limit",pageSize);
        int pageNumber = receiptSearchCriteria.getOffset() != null ? receiptSearchCriteria.getOffset() : Pagination.DEFAULT_PAGE_OFFSET;
        preparedStatementValues.put("offset", pageNumber * pageSize);

        List<ReceiptHeader> listOfHeadersFromDB = namedParameterJdbcTemplate.query(
                receiptHeaderQuery, preparedStatementValues,
                receiptRowMapper);
        page.setTotalResults(listOfHeadersFromDB.size());
        List<Object> receiptDetailsPreparedStatementValues = null;
        List<ReceiptHeader> receiptHeaders = new ArrayList<ReceiptHeader>();
        for (ReceiptHeader header : listOfHeadersFromDB) {
            ReceiptHeader receiptHeader = new ReceiptHeader();
            receiptDetailsPreparedStatementValues = new ArrayList<>();
            receiptDetailsPreparedStatementValues.add(receiptSearchCriteria.getTenantId());
            receiptDetailsPreparedStatementValues.add(header.getId());

            List<BusinessDetailsRequestInfo> businessDetails = businessDetailsRepository.getBusinessDetails(
                    Collections.singletonList(header.getBusinessDetails()), header.getTenantId(), requestInfo)
                    .getBusinessDetails();
            logger.info("BusinessDetails for Receipt" + businessDetails);
            receiptHeader = header;
            receiptHeader.setBusinessDetails(businessDetails != null && !businessDetails.isEmpty() ? businessDetails.get(0).getName() : "NA");
            if(receiptSearchCriteria.isReceiptDetailsRequired()) {
                List<ReceiptDetail> receiptDetails = jdbcTemplate.query(
                        receiptDetailsQuery, receiptDetailsPreparedStatementValues.toArray(),
                        receiptDetaiRowMapper);
                receiptHeader.setReceiptDetails(new HashSet<>(receiptDetails));
            }
            receiptHeader.setReceiptInstrument(
                    searchInstrumentHeader(receiptHeader.getId(), receiptSearchCriteria.getTenantId(), requestInfo));
            receiptHeaders.add(receiptHeader);
        }

        page.setPagedData(receiptHeaders);
        return page;

    }

    public ReceiptReq cancelReceipt(ReceiptReq receiptReq) {
        List<Bill> billInfo = new ArrayList<>();

        List<BillDetail> details = new ArrayList<>();
        for (Bill bill : billInfo) {
            details.addAll(bill.getBillDetails());
        }

        List<Object[]> batchArgs = new ArrayList<>();
        for (BillDetail detail : details) {
            Object[] obj = { detail.getStatus(),
                    detail.getReasonForCancellation(),
                    detail.getCancellationRemarks(),
                    receiptReq.getRequestInfo().getUserInfo().getId(),
                    new java.sql.Date(new Date().getTime()),
                    Long.valueOf(detail.getId()), detail.getTenantId() };
            batchArgs.add(obj);
        }
        jdbcTemplate.batchUpdate(receiptDetailQueryBuilder.getCancelQuery(),
                batchArgs);
        return receiptReq;
    }

    public List<Receipt> pushReceiptCancelDetailsToQueue(ReceiptReq receiptReq) {
        List<Receipt> receiptInfo = receiptReq.getReceipt();
        List<Bill> billInfo = new ArrayList<>();
        for (Receipt receipt : receiptInfo) {
            billInfo.addAll(receipt.getBill());
        }
        List<BillDetail> details = new ArrayList<>();
        for (Bill bill : billInfo) {
            details.addAll(bill.getBillDetails());
        }

        for (BillDetail detail : details) {
            detail.setStatus(ReceiptStatus.CANCELLED.toString());
        }
        try {
            collectionProducer.producer(
                    applicationProperties.getCancelReceiptTopicName(),
                    applicationProperties.getCancelReceiptTopicKey(),
                    receiptReq);

        } catch (Exception e) {
            logger.error("Pushing to Queue FAILED! ", e);
            return null;
        }
        return receiptInfo;

    }

    public long getStateId(Long receiptHeaderId) {
        logger.info("Updating stateId..");
        long stateId = 0L;
        String query = "SELECT stateid FROM egcl_receiptheader WHERE id=?";
        try {
            Long id = jdbcTemplate.queryForObject(query,
                    new Object[] { receiptHeaderId }, Long.class);
            stateId = id;
        } catch (Exception e) {
            logger.error("Couldn't fetch stateId for the receipt: "
                    + receiptHeaderId);
            return stateId;
        }
        logger.info("StateId obtained for receipt: " + receiptHeaderId + " is: "
                + stateId);
        return stateId;
    }

    public List<User> getReceiptCreators(final RequestInfo requestInfo,
            final String tenantId) {
        String queryString = receiptDetailQueryBuilder.searchQuery();
        List<Long> receiptCreators = jdbcTemplate.queryForList(queryString,
                Long.class, tenantId);
        return userRepository.getUsersById(receiptCreators, requestInfo,
                tenantId);
    }

    public List<String> getReceiptStatus(final String tenantId) {
        String queryString = receiptDetailQueryBuilder.searchStatusQuery();
        return jdbcTemplate.queryForList(queryString,
                String.class, tenantId);
    }

    private void persistIntoReceiptInstrument(String instrumentId,
                                              Long receiptHeaderId, String tenantId) {
        logger.info("Persisting into receipt Instrument");
        String queryString = receiptDetailQueryBuilder.insertInstrumentId();
        jdbcTemplate.update(queryString, instrumentId, receiptHeaderId, tenantId);
    }

    public void updateReceipt(final ReceiptReq receiptReq) throws ValidationException {
        logger.info("Updating workflowdetails for reciept");

        String updateWorkFlowDetailsQuery = receiptDetailQueryBuilder.getQueryToUpdateReceiptWorkFlowDetails();
        List<Map<String, Object>> receiptUpdateBatchValues = new ArrayList<>(receiptReq.getReceipt().size());

        List<Receipt> receipts = receiptReq.getReceipt();
        Bill bill = receipts.get(0).getBill().get(0);
        for(BillDetail billDetail:bill.getBillDetails()) {
            if(billDetail.getStateId() == null || billDetail.getStateId() == 0)
                throw new ValidationException(CollectionServiceConstants.STATEID_NOT_UPDATED_FOR_RECEIPT);
            receiptUpdateBatchValues.add(new MapSqlParameterSource().addValue("receiptnumber", billDetail.getReceiptNumber()).addValue("status", billDetail.getStatus())
                    .addValue("tenantId", billDetail.getTenantId()).addValue("stateId", billDetail.getStateId()).getValues());
        }
        try {
            namedParameterJdbcTemplate.batchUpdate(updateWorkFlowDetailsQuery, receiptUpdateBatchValues.toArray(new Map[receiptReq.getReceipt().size()]));
        } catch (Exception e) {
            logger.error(
                    "could not update status and stateId in db for ReceiptRequest:",e);
        }
    }

    public void pushUpdateDetailsToQueque(WorkflowDetailsRequest workFlowDetailsRequest) {
        logger.info("Pushing updateReceiptDetails to queue");
        try {
            collectionProducer.producer(
                    applicationProperties.getUpdateReceiptTopicName(),
                    applicationProperties.getUpdateReceiptTopicKey(),
                    workFlowDetailsRequest);
        } catch (Exception e) {
            logger.error("Pushing To Queue Failed! ", e);
        }
    }

    public List<BusinessDetailsRequestInfo> getBusinessDetails(
            final RequestInfo requestInfo, final String tenantId) {
        String queryString = receiptDetailQueryBuilder
                .searchBusinessDetailsQuery();
        List<String> businessDetailsList = jdbcTemplate.queryForList(
                queryString, String.class, tenantId);
        return businessDetailsRepository.getBusinessDetails(
                businessDetailsList, tenantId, requestInfo)
                .getBusinessDetails();
    }

    public List<ChartOfAccount> getChartOfAccounts(final String tenantId,
            final RequestInfo requestInfo) {
        String queryString = receiptDetailQueryBuilder
                .searchChartOfAccountsQuery();
        List<String> chartOfAccountsList = jdbcTemplate.queryForList(
                queryString, String.class, tenantId);
        return chartOfAccountsRepository.getChartOfAccounts(
                chartOfAccountsList, tenantId, requestInfo);
    }

    public Long getNextSeqForRcptHeader() {
        Long sequence = null;
        String queryString = receiptDetailQueryBuilder.getNextSeqForRcptHeader();
        try {
            sequence = jdbcTemplate.queryForObject(queryString, Long.class);
        } catch (Exception e) {
            logger.error("Next sequence number for receiptheader couldn't be fetched", e);
        }
        return sequence;
    }

    private Instrument searchInstrumentHeader(final Long receiptHeader, final String tenantId, final RequestInfo requestInfo) {
        String queryString = receiptDetailQueryBuilder.searchReceiptInstrument();
        List<String> instrumentHeaders = jdbcTemplate.queryForList(
                queryString, String.class, receiptHeader, tenantId);
        return !instrumentHeaders.isEmpty()
                ? instrumentRepository.searchInstruments(instrumentHeaders.get(0), tenantId, requestInfo) : null;
    }

    /*
     * public boolean validateReceiptNumber(String receiptNumber, String tenantId){ boolean isReceiptNumberValid = false; String
     * query = receiptDetailQueryBuilder.searchReceiptOnRcptNo(); Long id = null; try{ id = jdbcTemplate.queryForObject(query, new
     * Object[] { receiptNumber,tenantId }, Long.class); }catch(Exception e){ isReceiptNumberValid = true; return
     * isReceiptNumberValid; } if(null == id || 0L == id){ isReceiptNumberValid = true; return isReceiptNumberValid; } return
     * isReceiptNumberValid; }
     */

    public void insertOnlinePayments(final OnlinePayment onlinePayment,final RequestInfo requestInfo,final Long receiptHeaderId) {
        String onlineReceiptsInsertQuery = receiptDetailQueryBuilder.insertOnlinePayments();
        List<Map<String, Object>> onlineReceiptBatchValues = new ArrayList<>();

        try {
            onlineReceiptBatchValues.add(new MapSqlParameterSource("receiptheader", receiptHeaderId)
                    .addValue("paymentgatewayname", onlinePayment.getPaymentGatewayName())
                    .addValue("transactionnumber", onlinePayment.getTransactionNumber())
                    .addValue("transactionamount", onlinePayment.getTransactionAmount())
                    .addValue("transactiondate", onlinePayment.getTransactionDate())
                    .addValue("authorisation_statuscode", onlinePayment.getAuthorisationStatusCode())
                    .addValue("status", onlinePayment.getStatus())
                    .addValue("remarks", onlinePayment.getRemarks())
                    .addValue("createdby", requestInfo.getUserInfo().getId())
                    .addValue("tenantId", onlinePayment.getTenantId())
                    .addValue("lastmodifiedby", requestInfo.getUserInfo().getId())
                    .addValue("createddate", new Date().getTime()).addValue("lastmodifieddate", new Date().getTime())
                    .getValues());

            namedParameterJdbcTemplate.batchUpdate(onlineReceiptsInsertQuery,
                    onlineReceiptBatchValues.toArray(new Map[1]));
        } catch(Exception e) {
            logger.error("Error in inserting online payment data",e);
        }

    }

    @SuppressWarnings("unchecked")
    public LegacyReceiptReq pushLegacyReceiptToDB(LegacyReceiptReq legacyReceiptRequest) {
        logger.info("LegacyReceiptReq:" + legacyReceiptRequest);
        List<LegacyReceiptHeader> listOflegacyReceiptHeaders = legacyReceiptRequest.getLegacyReceipts();
        List<LegacyReceiptDetails> listOfLegacyReceiptDetails = new ArrayList<>();
        String legacyReceiptHeaderQuery = receiptDetailQueryBuilder.getLegacyReceiptHeaderInsertQuery();
        String legacyReceiptDetailsQuery = receiptDetailQueryBuilder.getLegacyReceiptDetailInsertQuery();
        for (LegacyReceiptHeader legacyReceiptHeader : listOflegacyReceiptHeaders) {
            listOfLegacyReceiptDetails.addAll(legacyReceiptHeader.getLegacyReceiptDetails());
        }
        List<Map<String, Object>> legacyReceiptValues = new ArrayList<>(listOflegacyReceiptHeaders.size());
        List<Map<String, Object>> legacyReceiptDetailsValues = new ArrayList<>(listOfLegacyReceiptDetails.size());
        Long legacyHeaderSeqNumber;

        for (LegacyReceiptHeader legacyReceiptHeader : listOflegacyReceiptHeaders) {
            String legacyHeaderSeqNumberQuery = receiptDetailQueryBuilder.getLegacyReceiptHeaderSequenceNumber();
            Map<String, Object> seqNumberMap = new HashMap<>();
            seqNumberMap.put("seqNumber", LegacyReceiptHeader.SEQ_LEGACY_RECEIPT_HEADER);
            legacyHeaderSeqNumber = namedParameterJdbcTemplate.queryForObject(legacyHeaderSeqNumberQuery, seqNumberMap,
                    Long.class);
            List<String> businessDetailsCode = new ArrayList<>();
            businessDetailsCode.add(legacyReceiptHeader.getServiceCode());
            BusinessDetailsResponse detailsResponse = businessDetailsRepository.getBusinessDetails(businessDetailsCode,
                    legacyReceiptHeader.getTenantId(), legacyReceiptRequest.getRequestInfo());
            legacyReceiptHeader.setServiceName(detailsResponse.getBusinessDetails().get(0).getName());
            legacyReceiptValues.add(new MapSqlParameterSource("id", legacyHeaderSeqNumber)
                    .addValue("receiptno", legacyReceiptHeader.getReceiptNo())
                    .addValue("receiptdate", legacyReceiptHeader.getReceiptDate())
                    .addValue("department", legacyReceiptHeader.getDepartment())
                    .addValue("servicename", legacyReceiptHeader.getServiceName())
                    .addValue("consumerno", legacyReceiptHeader.getConsumerNo())
                    .addValue("consumername", legacyReceiptHeader.getConsumerName())
                    .addValue("totalamount", Double.valueOf(String.valueOf(legacyReceiptHeader.getTotalAmount())))
                    .addValue("advanceamount", Double.valueOf(String.valueOf(legacyReceiptHeader.getAdvanceAmount())))
                    .addValue("adjustmentamount", Double.valueOf(String.valueOf(legacyReceiptHeader.getAdjustmentAmount())))
                    .addValue("consumeraddress", legacyReceiptHeader.getConsumerAddress())
                    .addValue("payeename", legacyReceiptHeader.getPayeeName())
                    .addValue("instrumenttype", legacyReceiptHeader.getInstrumentType())
                    .addValue("instrumentdate", legacyReceiptHeader.getInstrumentDate())
                    .addValue("instrumentno", legacyReceiptHeader.getInstrumentNo())
                    .addValue("bankname", legacyReceiptHeader.getBankName())
                    .addValue("manualreceiptnumber", legacyReceiptHeader.getManualreceiptnumber())
                    .addValue("manualreceiptdate", legacyReceiptHeader.getManualreceiptDate())
                    .addValue("tenantid", legacyReceiptHeader.getTenantId())
                    .addValue("remarks", legacyReceiptHeader.getRemarks())
                    .getValues());
            for (LegacyReceiptDetails legacyReceiptDetail : legacyReceiptHeader.getLegacyReceiptDetails()) {
                String receiptDetailSequenceNumber = receiptDetailQueryBuilder.getSequenceNumberForReceiptDetails();
                Map<String, Object> detailsParam = new HashMap<>();
                detailsParam.put("seqNumber", LegacyReceiptDetails.SEQ_LEGACY_RECEIPT_DETAILS);
                Long sequenceNumberForDetails = namedParameterJdbcTemplate.queryForObject(receiptDetailSequenceNumber,
                        detailsParam, Long.class);
                legacyReceiptDetailsValues
                        .add(new MapSqlParameterSource("id", sequenceNumberForDetails)
                                .addValue("billno", legacyReceiptDetail.getBillNo())
                                .addValue("billid", legacyReceiptDetail.getBillId())
                                .addValue("billyear", legacyReceiptDetail.getBillYear())
                                .addValue("taxid", legacyReceiptDetail.getTaxId())
                                .addValue("billdate", legacyReceiptDetail.getBillDate())
                                .addValue("description", legacyReceiptDetail.getDescription())
                                .addValue("currdemand",
                                        Double.valueOf(String.valueOf(legacyReceiptDetail.getCurrDemand())))
                                .addValue("arrdemand", Double.valueOf(String.valueOf(legacyReceiptDetail.getArrDemand())))
                                .addValue("currcollection",
                                        Double.valueOf(String.valueOf(legacyReceiptDetail.getCurrCollection())))
                                .addValue("arrcollection",
                                        Double.valueOf(String.valueOf(legacyReceiptDetail.getArrCollection())))
                                .addValue("currbalance", Double.valueOf(String.valueOf(legacyReceiptDetail.getCurrDemand()
                                        .subtract(legacyReceiptDetail.getCurrCollection()))))
                                .addValue("arrbalance", Double.valueOf(String.valueOf(legacyReceiptDetail.getArrDemand()
                                        .subtract(legacyReceiptDetail.getArrCollection()))))
                                .addValue("receiptheaderid", legacyHeaderSeqNumber)
                                .addValue("tenantid", legacyReceiptDetail.getTenantid()).getValues());
            }
        }
        try {
            logger.info("persisting legacyReceiptHeaderData to its respective table:" + legacyReceiptValues.toString());
            logger.info("persisting legacyReceiptDetailsData to its respective table:" + legacyReceiptDetailsValues.toString());
            namedParameterJdbcTemplate.batchUpdate(legacyReceiptHeaderQuery,
                    legacyReceiptValues.toArray(new Map[listOflegacyReceiptHeaders.size()]));
            namedParameterJdbcTemplate.batchUpdate(legacyReceiptDetailsQuery,
                    legacyReceiptDetailsValues.toArray(new Map[listOfLegacyReceiptDetails.size()]));
        } catch (Exception e) {
            logger.error("data cannot be persisted to respective tables", e);
        }
        return legacyReceiptRequest;
    }


    public List<LegacyReceiptHeader> getLegacyReceiptsByCriteria(LegacyReceiptGetReq legacyReceiptGetReq) {

        List<Object> preparedStatementVal = new ArrayList<>();
        String legacyReceiptHeaderQuery = receiptDetailQueryBuilder.getLegacyReceiptSearchQuery(legacyReceiptGetReq,
                preparedStatementVal);
        String legacyReceiptDetailQuery = receiptDetailQueryBuilder.getLegacyReceiptDetailByLegacyReceiptHeader();
        List<LegacyReceiptHeader> listOfLegacyReceiptHeader = jdbcTemplate.query(legacyReceiptHeaderQuery,
                preparedStatementVal.toArray(),
                legacyReceiptHeaderRowMapper);
        List<LegacyReceiptHeader> listOfLegacyHeaders = new ArrayList<>();
        for (LegacyReceiptHeader legacyReceiptHeader : listOfLegacyReceiptHeader) {
            List<Object> receiptDetailsPreparedStatementValues = new ArrayList<>();
            receiptDetailsPreparedStatementValues.add(legacyReceiptGetReq.getTenantId());
            receiptDetailsPreparedStatementValues.add(legacyReceiptHeader.getId());
            List<LegacyReceiptDetails> listOfLegacyReceiptDetails = jdbcTemplate.query(legacyReceiptDetailQuery,
                    receiptDetailsPreparedStatementValues.toArray(), legacyReceiptDetailRowMapper);
            legacyReceiptHeader.setLegacyReceiptDetails(listOfLegacyReceiptDetails);
            listOfLegacyHeaders.add(legacyReceiptHeader);
        }
        return listOfLegacyHeaders;
    }

}
