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
package org.egov.lams.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.lams.exceptions.NoObjectionRecordsFoundException;
import org.egov.lams.exceptions.NoRenewalRecordsFoundException;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.Cancellation;
import org.egov.lams.model.Document;
import org.egov.lams.model.Eviction;
import org.egov.lams.model.Judgement;
import org.egov.lams.model.Objection;
import org.egov.lams.model.Remission;
import org.egov.lams.model.Renewal;
import org.egov.lams.model.SubSeqRenewal;
import org.egov.lams.model.enums.Action;
import org.egov.lams.model.enums.Source;
import org.egov.lams.repository.builder.AgreementQueryBuilder;
import org.egov.lams.repository.helper.AgreementHelper;
import org.egov.lams.repository.helper.AllotteeHelper;
import org.egov.lams.repository.helper.AssetHelper;
import org.egov.lams.repository.rowmapper.AgreementRowMapper;
import org.egov.lams.repository.rowmapper.DocumentRowMapper;
import org.egov.lams.repository.rowmapper.SubSeqRenewalRowMapper;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.AllotteeResponse;
import org.egov.lams.web.contract.AssetResponse;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AgreementRepository {

    public static final String AGREEMENT_SEARCH_QUERY = "SELECT *,agreement.id as lamsagreementid FROM eglams_agreement agreement LEFT OUTER JOIN eglams_demand demand ON agreement.id = demand.agreementid LEFT OUTER JOIN eglams_rentincrementtype rent ON agreement.rent_increment_method = rent.id where agreement.action is not null ";

    public static final String AGREEMENT_UPDATE_QUERY = "UPDATE eglams_agreement SET Id=:agreementID,Agreement_Date=:agreementDate,Agreement_No=:agreementNo,Bank_Guarantee_Amount=:bankGuaranteeAmount,Bank_Guarantee_Date=:bankGuaranteeDate,Case_No=:caseNo,Commencement_Date=:commencementDate,Council_Date=:councilDate,Council_Number=:councilNumber,Expiry_Date=:expiryDate,Nature_Of_Allotment=:natureOfAllotment,Order_Date=:orderDate,Order_Details=:orderDetails,Order_No=:orderNumber,Payment_Cycle=:paymentCycle,Registration_Fee=:registrationFee,Remarks=:remarks,Rent=:rent,Rr_Reading_No=:rrReadingNo,Security_Deposit=:securityDeposit,Security_Deposit_Date=:securityDepositDate,solvency_certificate_date=:solvencyCertificateDate,solvency_certificate_no=:solvencyCertificateNo,status=:status,tin_number=:tinNumber,Tender_Date=:tenderDate,Tender_Number=:tenderNumber,Trade_license_Number=:tradelicenseNumber,created_by=:createdBy,last_modified_by=:lastmodifiedBy,last_modified_date=:lastmodifiedDate,allottee=:allottee,asset=:asset,Rent_Increment_Method=:rentIncrement,AcknowledgementNumber=:acknowledgementNumber,stateid=:stateId,Tenant_id=:tenantId,goodwillamount=:goodWillAmount,timeperiod=:timePeriod,collectedsecuritydeposit=:collectedSecurityDeposit,collectedgoodwillamount=:collectedGoodWillAmount,source=:source,reason=:reason,terminationDate=:terminationDate,courtReferenceNumber=:courtReferenceNumber,action=:action,courtcase_no=:courtCaseNo,courtcase_date=:courtCaseDate,courtfixed_rent=:courtFixedRent,effective_date=:effectiveDate,judgement_no=:judgementNo,judgement_date=:judgementDate,judgement_rent=:judgementRent,remission_fee=:remissionRent,remission_from_date=:remissionFromDate,remission_to_date=:remissionToDate,remission_order_no=:remissionOrder,adjustment_start_date=:adjustmentStartDate,is_under_workflow=:isUnderWorkflow,first_allotment=:firstAllotment,gstin=:gstin,municipal_order_no=:municaipalOrderNo,municipal_order_date=:municipalOrderDate,govt_order_no=:govtOrderNo,govt_order_date=:govtOrderDate,renewal_date=:renewalDate, old_agreement_no=:oldAgreementNumber, base_allotment=:basisAllotment,res_category=:resCategory,referenceno=:referenceNo,floorno=:floorNo,tenderOpeningDate=:tenderOpeningDate,auctionAmount=:auctionAmount,solvencyAmount=:solvencyAmount,remission_order_date=:remissionDate WHERE id=:agreementID and Tenant_id=:tenantId ";

    public static final String VIEW_DCB = "DCB";

    public static final String SOURCE_DATAENTRY = "DATA_ENTRY";

    public static final String SOURCE_SYSTEM = "SYSTEM";

    public static final String ACTION_MODIFY = "MODIFY";

    public static final String ACTION_VIEW = "VIEW";

    public static final String SUB_SEQ_RENEW_INSERT_QUERY = "INSERT INTO eglams_history (id,agreementid,resolutionno,resolutiondate,fromdate,todate,years,rent,tenantid,createdby,createddate,lastmodifiedby,lastmodifieddate) values "
            + "(nextval('seq_eglams_history'),?,?,?,?,?,?,?,?,?,?,?,?);";

    @Autowired
    private AssetHelper assetHelper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AllotteeRepository allotteeRepository;

    @Autowired
    private AllotteeHelper allotteeHelper;

    @Autowired
    private AgreementHelper agreementHelper;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public boolean isAgreementExist(final String code) {

        Long agreementId = null;
        final String sql = AgreementQueryBuilder.AGREEMENT_QUERY;
        final Map<String, Object> params = new HashMap<>();
        params.put("acknowledgementNumber", code);
        params.put("agreementNumber", code);

        try {
            agreementId = namedParameterJdbcTemplate.queryForList(sql, params, Long.class).get(0);
        } catch (final DataAccessException e) {
            log.info("exception in getagreementbyid :: " + e);
            throw new RuntimeException(e.getMessage());
        }

        return agreementId != null && agreementId != 0;
    }

    public List<Agreement> getHistoryAgreementsByAsset(final Agreement agreement) {

        List<Agreement> agreements = null;
        final Map<String, Object> params = new HashMap<>();
        final String sql = AgreementQueryBuilder.getHistoryAgreementSearchQuery(agreement, params);
        try {
            agreements = namedParameterJdbcTemplate.query(sql, params, new AgreementRowMapper());
        } catch (final DataAccessException e) {
            log.info("exception in getagreement :: " + e);
            throw new RuntimeException(e.getMessage());
        }
        return agreements;
    }

    public List<Agreement> getAgreementForCriteria(final AgreementCriteria agreementsModel) {

        List<Agreement> agreements = null;
        final Map<String, Object> params = new HashMap<>();
        final String sql = AgreementQueryBuilder.getAgreementSearchQuery(agreementsModel, params);
        try {
            agreements = namedParameterJdbcTemplate.query(sql, params, new AgreementRowMapper());
        } catch (final DataAccessException e) {
            log.info("exception in getagreement :: " + e);
            throw new RuntimeException(e.getMessage());
        }
        return agreements;
    }

    public List<Agreement> findByAllotee(final AgreementCriteria agreementCriteria, final RequestInfo requestInfo) {
        final Map<String, Object> params = new HashMap<>();
        List<Agreement> agreements = null;

        final List<Allottee> allottees = getAllottees(agreementCriteria, requestInfo);
        agreementCriteria.setAllottee(allotteeHelper.getAllotteeIdList(allottees));
        final String queryStr = AgreementQueryBuilder.getAgreementSearchQuery(agreementCriteria, params);
        try {
            agreements = namedParameterJdbcTemplate.query(queryStr, params, new AgreementRowMapper());
        } catch (final DataAccessException e) {
            log.info("exception in agreementrepo jdbc temp :" + e);
            throw new RuntimeException(e.getMessage());
        }
        if (agreements.isEmpty())
            return agreements; // empty agreement list is returned
        // throw new RuntimeException("The criteria provided did not match any
        // agreements");
        agreementCriteria.setAsset(assetHelper.getAssetIdListByAgreements(agreements));

        final List<Asset> assets = getAssets(agreementCriteria, requestInfo);
        agreements = agreementHelper.filterAndEnrichAgreements(agreements, allottees, assets);

        return agreements;
    }

    public List<Agreement> findByAsset(final AgreementCriteria agreementCriteria, final RequestInfo requestInfo) {
        log.info("AgreementController SearchAgreementService AgreementRepository : inside findByAsset");
        final Map<String, Object> params = new HashMap<>();
        List<Agreement> agreements = null;
        log.info("before calling get asset method");
        final List<Asset> assets = getAssets(agreementCriteria, requestInfo);
        log.info("after calling get asset method : lengeth of result is" + assets.size());
        if (assets.size() > 1000) // FIXME
            throw new RuntimeException("Asset criteria is too big");
        agreementCriteria.setAsset(assetHelper.getAssetIdList(assets));
        final String queryStr = AgreementQueryBuilder.getAgreementSearchQuery(agreementCriteria, params);
        try {
            agreements = namedParameterJdbcTemplate.query(queryStr, params, new AgreementRowMapper());
        } catch (final DataAccessException e) {
            log.info(e.getMessage(), e.getCause());
            throw new RuntimeException(e.getMessage());
        }
        if (agreements.isEmpty())
            return agreements;
        agreementCriteria.setAllottee(allotteeHelper.getAllotteeIdListByAgreements(agreements));
        final List<Allottee> allottees = getAllottees(agreementCriteria, requestInfo);
        agreements = agreementHelper.filterAndEnrichAgreements(agreements, allottees, assets);

        return agreements;
    }

    public List<Agreement> findByAgreement(final AgreementCriteria agreementCriteria, final RequestInfo requestInfo) {
        log.info("AgreementController SearchAgreementService AgreementRepository : inside findByAgreement");
        final Map<String, Object> params = new HashMap<>();
        List<Agreement> agreements = null;

        final String queryStr = AgreementQueryBuilder.getAgreementSearchQuery(agreementCriteria, params);
        try {
            agreements = namedParameterJdbcTemplate.query(queryStr, params, new AgreementRowMapper());
        } catch (final DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
        if (agreements.isEmpty())
            return agreements;
        agreementCriteria.setAsset(assetHelper.getAssetIdListByAgreements(agreements));
        agreementCriteria.setAllottee(allotteeHelper.getAllotteeIdListByAgreements(agreements));
        final List<Asset> assets = getAssets(agreementCriteria, requestInfo);
        final List<Allottee> allottees = getAllottees(agreementCriteria, requestInfo);
        agreements = agreementHelper.filterAndEnrichAgreements(agreements, allottees, assets);
        if (ACTION_MODIFY.equalsIgnoreCase(agreementCriteria.getAction())
                || ACTION_VIEW.equalsIgnoreCase(agreementCriteria.getAction()))
            agreements = agreementHelper.enrichAgreementsWithSubSeqRenewals(agreements, getSubSeqRenewals(agreements),
                    getAttachedDocuments(agreements));

        return agreements;
    }

    public List<Agreement> findByAgreementAndAllotee(final AgreementCriteria agreementCriteria, final RequestInfo requestInfo) {
        log.info(
                "AgreementController SearchAgreementService AgreementRepository : inside findByAgreementAndAllotee");
        final Map<String, Object> params = new HashMap<>();
        List<Agreement> agreements = null;

        final String queryStr = AgreementQueryBuilder.getAgreementSearchQuery(agreementCriteria, params);
        try {
            agreements = namedParameterJdbcTemplate.query(queryStr, params, new AgreementRowMapper());
        } catch (final DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
        if (agreements.isEmpty())
            return agreements;
        agreementCriteria.setAllottee(allotteeHelper.getAllotteeIdListByAgreements(agreements));
        final List<Allottee> allottees = getAllottees(agreementCriteria, requestInfo);
        agreementCriteria.setAsset(assetHelper.getAssetIdListByAgreements(agreements));
        final List<Asset> assets = getAssets(agreementCriteria, requestInfo);
        agreements = agreementHelper.filterAndEnrichAgreements(agreements, allottees, assets);

        return agreements;
    }

    public List<Agreement> findByAgreementAndAsset(final AgreementCriteria fetchAgreementsModel, final RequestInfo requestInfo) {
        log.info("AgreementController SearchAgreementService AgreementRepository : inside findByAgreementAndAsset");
        final Map<String, Object> params = new HashMap<>();
        List<Agreement> agreements = null;

        final String queryStr = AgreementQueryBuilder.getAgreementSearchQuery(fetchAgreementsModel, params);
        try {
            agreements = namedParameterJdbcTemplate.query(queryStr, params, new AgreementRowMapper());
        } catch (final DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
        if (agreements.isEmpty())
            return agreements;
        fetchAgreementsModel.setAsset(assetHelper.getAssetIdListByAgreements(agreements));
        final List<Asset> assets = getAssets(fetchAgreementsModel, requestInfo);
        fetchAgreementsModel.setAllottee(allotteeHelper.getAllotteeIdListByAgreements(agreements));
        final List<Allottee> allottees = getAllottees(fetchAgreementsModel, requestInfo);
        agreements = agreementHelper.filterAndEnrichAgreements(agreements, allottees, assets);

        return agreements;
    }

    public List<Agreement> findByAgreementNumber(final AgreementCriteria agreementCriteria, final RequestInfo requestInfo) {

        String query = null;
        final StringBuilder appendQuery = new StringBuilder();
        query = AGREEMENT_SEARCH_QUERY;
        List<Agreement> agreements = null;
        final Map<String, Object> params = new HashMap<>();
        if (agreementCriteria.getAgreementNumber() != null) {
            appendQuery.append(" and agreement.agreement_No=:agreementNumber ");
            params.put("agreementNumber", agreementCriteria.getAgreementNumber());
        }
        if (agreementCriteria.getAcknowledgementNumber() != null) {
            appendQuery.append(" and agreement.acknowledgementnumber=:acknowledgementnumber ");
            params.put("acknowledgementnumber", agreementCriteria.getAcknowledgementNumber());
        }
        if (agreementCriteria.getStatus() != null) {
            appendQuery.append(" and status in (:status)");
            params.put("status", agreementCriteria.getStatus().toString());
        }
        if (agreementCriteria.getTenantId() != null) {
            appendQuery.append(" and agreement.tenant_id=:tenantId ");
            params.put("tenantId", agreementCriteria.getTenantId());

        }
        appendQuery.append(" order by agreement.id desc");
        query = query.concat(appendQuery.toString());

        try {
            agreements = namedParameterJdbcTemplate.query(query, params, new AgreementRowMapper());
        } catch (final DataAccessException e) {
            log.info("exception occured while getting agreement by agreementNumber" + e);
            throw new RuntimeException(e.getMessage());
        }
        if (agreements.isEmpty())
            return agreements;
        agreementCriteria.setAsset(assetHelper.getAssetIdListByAgreements(agreements));
        agreementCriteria.setAllottee(allotteeHelper.getAllotteeIdListByAgreements(agreements));
        final List<Asset> assets = getAssets(agreementCriteria, requestInfo);
        final List<Allottee> allottees = getAllottees(agreementCriteria, requestInfo);
        agreements = agreementHelper.filterAndEnrichAgreements(agreements, allottees, assets);
        return agreements;
    }

    public List<Agreement> findByAgreementId(final Long agreementId) {
        List<Agreement> agreements = null;
        final HashMap<String, Object> params = new HashMap<>();
        params.put("agreementId", agreementId);
        final String query = AgreementQueryBuilder.BASE_SEARCH_QUERY + " where agreement.id=:agreementId";

        try {
            agreements = namedParameterJdbcTemplate.query(query, params, new AgreementRowMapper());
        } catch (final DataAccessException e) {
            log.info("exception occured while getting agreement by agreementId" + e);
            throw new RuntimeException(e.getMessage());
        }
        return agreements;

    }

    /*
     * method to return a list of Allottee objects by making an API call to Allottee API
     */
    public List<Allottee> getAllottees(final AgreementCriteria agreementCriteria, final RequestInfo requestInfo) {
        // FIXME TODO urgent allottee helper has to be changed for post
        // String queryString =
        // allotteeHelper.getAllotteeParams(agreementCriteria);
        log.info("AgreementController SearchAgreementService AgreementRepository : inside Allottee API caller");
        final AllotteeResponse allotteeResponse = allotteeRepository.getAllottees(agreementCriteria, new RequestInfo());
        if (allotteeResponse.getAllottee() == null || allotteeResponse.getAllottee().size() <= 0)
            throw new RuntimeException("No allottee found for given criteria");
        log.info("the result allottee response from allottee api call : " + allotteeResponse.getAllottee());
        return allotteeResponse.getAllottee();
    }

    /*
     * method to return a list of Asset objects by calling AssetService API
     */
    public List<Asset> getAssets(final AgreementCriteria agreementCriteria, final RequestInfo requestInfo) {
        log.info("inside get asset method");
        final String queryString = assetHelper.getAssetParams(agreementCriteria);
        final AssetResponse assetResponse = assetRepository.getAssets(queryString, new RequestInfoWrapper());
        if (assetResponse.getAssets() == null || assetResponse.getAssets().size() <= 0)
            throw new RuntimeException("No assets found for given criteria");
        // FIXME empty response exception
        log.info("the result asset response from asset api call : " + assetResponse.getAssets());
        return assetResponse.getAssets();
    }

    @Transactional
    public void saveAgreement(final AgreementRequest agreementRequest) {

        final Map<String, Object> processMap = getProcessMap(agreementRequest);
        final Agreement agreement = agreementRequest.getAgreement();
        final RequestInfo requestInfo = agreementRequest.getRequestInfo();
        final String userId = requestInfo.getUserInfo().getId().toString();
        log.info("AgreementDao agreement::" + agreement);

        final String agreementinsert = AgreementQueryBuilder.INSERT_AGREEMENT_QUERY;

        final Map<String, Object> agreementParameters = getInputParams(agreement, processMap);
        agreementParameters.put("createdDate", new Date());
        try {
            namedParameterJdbcTemplate.update(agreementinsert, agreementParameters);
        } catch (final DataAccessException ex) {
            log.info("exception saving agreement details" + ex);
            throw new RuntimeException(ex.getMessage());
        }

        final List<String> demands = agreement.getDemands();
        if (demands != null) {
            final String sql = "INSERT INTO eglams_demand values ( nextval('seq_eglams_demand'),?,?,?)";
            final List<Object[]> demandBatchArgs = new ArrayList<>();
            final int demandsCount = demands.size();

            for (int i = 0; i < demandsCount; i++) {
                final Object[] demandRecord = { agreement.getTenantId(), agreement.getId(), demands.get(i) };
                demandBatchArgs.add(demandRecord);
            }

            try {
                jdbcTemplate.batchUpdate(sql, demandBatchArgs);
            } catch (final DataAccessException ex) {
                log.info("exception saving demand details" + ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

        final List<Document> documents = agreement.getDocuments();
        if (documents != null) {
            final String sql = "INSERT INTO eglams_document (id,documenttype,agreement,filestore,tenantid) values "
                    + "(nextval('seq_eglams_document'),(select id from eglams_documenttype where "
                    + "name='Agreement Docs' and application='CREATE' and tenantid= ?),?,?,?);";
            log.info("the insert query for agreement docs : " + sql);
            final List<Object[]> documentBatchArgs = new ArrayList<>();

            for (final Document document : documents) {
                final Object[] documentRecord = { agreement.getTenantId(), agreement.getId(), document.getFileStore(),
                        agreement.getTenantId() };
                documentBatchArgs.add(documentRecord);
            }

            try {
                jdbcTemplate.batchUpdate(sql, documentBatchArgs);
            } catch (final DataAccessException ex) {
                log.info("exception saving agreement document details" + ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

        final List<SubSeqRenewal> renewalDetails = agreement.getSubSeqRenewals();
        if (Source.DATA_ENTRY.equals(agreement.getSource()) && Action.CREATE.equals(agreement.getAction())
                && renewalDetails != null) {
            final String sql = SUB_SEQ_RENEW_INSERT_QUERY;
            log.info("query for saving agreement history : " + sql);
            final List<Object[]> renewHistorytBatchArgs = new ArrayList<>();

            for (final SubSeqRenewal renewalHistory : renewalDetails) {

                final Object[] historyDetailList = { agreement.getId(), renewalHistory.getResolutionNumber(),
                        renewalHistory.getResolutionDate(), renewalHistory.getHistoryFromDate(),
                        renewalHistory.getHistoryToDate(), renewalHistory.getYears(), renewalHistory.getHistoryRent(),
                        agreement.getTenantId(), userId, new Date(), userId, new Date() };
                renewHistorytBatchArgs.add(historyDetailList);
            }

            try {
                jdbcTemplate.batchUpdate(sql, renewHistorytBatchArgs);
            } catch (final DataAccessException ex) {
                log.info("exception while saving history details" + ex);
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    public void updateAgreement(final AgreementRequest agreementRequest) {

        final Map<String, Object> processMap = getProcessMap(agreementRequest);
        final Agreement agreement = agreementRequest.getAgreement();
        log.info("AgreementDao agreement::" + agreement);

        final String agreementUpdate = AgreementQueryBuilder.UPDATE_AGREEMENT_QUERY;

        final Map<String, Object> agreementParameters = getInputParams(agreement, processMap);

        try {
            namedParameterJdbcTemplate.update(agreementUpdate, agreementParameters);
        } catch (final DataAccessException ex) {
            log.error("the exception from update demand in update agreement " + ex);
            throw new RuntimeException(ex.getMessage());
        }

        final String demandQuery = "select demandid from eglams_demand where agreementid=" + agreement.getId();
        final List<String> demandIdList = jdbcTemplate.queryForList(demandQuery, String.class);

        if (demandIdList.isEmpty() && agreement.getDemands() != null) {

            final List<String> demands = agreement.getDemands();

            final String sql = "INSERT INTO eglams_demand values ( nextval('seq_eglams_demand'),?,?,?)";
            final List<Object[]> demandBatchArgs = new ArrayList<>();
            final int demandsCount = demands.size();

            for (int i = 0; i < demandsCount; i++) {
                final Object[] demandRecord = { agreement.getTenantId(), agreement.getId(), demands.get(i) };
                demandBatchArgs.add(demandRecord);
            }

            try {
                jdbcTemplate.batchUpdate(sql, demandBatchArgs);
            } catch (final DataAccessException ex) {
                log.error("the exception from add demand in update agreement " + ex);
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    public void modifyAgreement(final AgreementRequest agreementRequest) {

        final Agreement agreement = agreementRequest.getAgreement();
        final RequestInfo requestInfo = agreementRequest.getRequestInfo();
        final Map<String, Object> processMap = getProcessMap(agreementRequest);
        final String userId = requestInfo.getUserInfo().getId().toString();
        log.info("Modify agreement::" + agreement);
        final String agreementUpdate = AGREEMENT_UPDATE_QUERY;
        final Map<String, Object> agreementParameters = getInputParams(agreement, processMap);

        final String findDemandQuery = "select demandid from eglams_demand where agreementid=" + agreement.getId();
        final String demandId = jdbcTemplate.queryForObject(findDemandQuery, String.class);
        if (!demandId.equals(agreement.getDemands().get(0))) {
            final String sql = "update eglams_demand set demandid=? where agreementid=?";
            final List<Object[]> demandBatchArgs = new ArrayList<>();
            final List<String> demands = agreement.getDemands();
            final int demandsCount = demands.size();

            for (int i = 0; i < demandsCount; i++) {
                final Object[] demandRecord = { demands.get(i), agreement.getId() };
                demandBatchArgs.add(demandRecord);
            }

            try {
                jdbcTemplate.batchUpdate(sql, demandBatchArgs);
            } catch (final DataAccessException ex) {
                log.error("the exception in add demand in modify agreement " + ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

        final List<Object[]> insertBatchArgs = new ArrayList<>();
        final Map<String, Object> paramsMap = new HashMap<>();
        final List<SubSeqRenewal> renewalDetails = agreement.getSubSeqRenewals();
        if (renewalDetails != null && !renewalDetails.isEmpty()) {
            final String insertQuery = SUB_SEQ_RENEW_INSERT_QUERY;
            final String deleteQuery = "delete from  eglams_history  where agreementid=:agreementId  and tenantid=:tenantId";
            paramsMap.put("agreementId", agreement.getId());
            paramsMap.put("tenantId", agreement.getTenantId());

            for (final SubSeqRenewal renewalHistory : renewalDetails) {

                final Object[] historyDetailList = { agreement.getId(), renewalHistory.getResolutionNumber(),
                        renewalHistory.getResolutionDate(), renewalHistory.getHistoryFromDate(),
                        renewalHistory.getHistoryToDate(), renewalHistory.getYears(), renewalHistory.getHistoryRent(),
                        agreement.getTenantId(), userId, new Date(), userId, new Date() };
                insertBatchArgs.add(historyDetailList);
            }
            try {
                namedParameterJdbcTemplate.update(deleteQuery, paramsMap);
                if (!insertBatchArgs.isEmpty())
                    jdbcTemplate.batchUpdate(insertQuery, insertBatchArgs);

            } catch (final DataAccessException ex) {
                log.error("the exception in updating subseqrenewal in modify agreement" + ex);
                throw new RuntimeException(ex.getMessage());
            }

        }
        try {
            namedParameterJdbcTemplate.update(agreementUpdate, agreementParameters);
        } catch (final DataAccessException ex) {
            log.error("exception in modify agreement " + ex);
            throw new RuntimeException(ex.getMessage());
        }
        deleteDocument(agreement.getId(), agreement.getTenantId());
        saveDocument(agreement);

    }

    private Map<String, Object> getInputParams(final Agreement agreement, final Map<String, Object> processMap) {
        final Map<String, Object> agreementParameters = new HashMap<>();
        agreementParameters.put("agreementID", agreement.getId());
        agreementParameters.put("agreementDate", agreement.getAgreementDate());
        agreementParameters.put("agreementNo", agreement.getAgreementNumber());
        agreementParameters.put("bankGuaranteeAmount", agreement.getBankGuaranteeAmount());
        agreementParameters.put("bankGuaranteeDate", agreement.getBankGuaranteeDate());
        agreementParameters.put("caseNo", agreement.getCaseNo());
        agreementParameters.put("commencementDate", agreement.getCommencementDate());
        agreementParameters.put("councilDate", agreement.getCouncilDate());
        agreementParameters.put("councilNumber", agreement.getCouncilNumber());
        agreementParameters.put("expiryDate", agreement.getExpiryDate());
        agreementParameters.put("natureOfAllotment",
                agreement.getNatureOfAllotment() == null ? "" : agreement.getNatureOfAllotment().toString());
        agreementParameters.put("orderDate", processMap.get("orderDate"));
        agreementParameters.put("orderDetails", agreement.getOrderDetails());
        agreementParameters.put("orderNumber", processMap.get("orderNumber"));
        agreementParameters.put("paymentCycle", agreement.getPaymentCycle().toString());
        agreementParameters.put("registrationFee", agreement.getRegistrationFee());
        agreementParameters.put("remarks", agreement.getRemarks());
        agreementParameters.put("rent", agreement.getRent());
        agreementParameters.put("rrReadingNo", agreement.getRrReadingNo());
        agreementParameters.put("securityDeposit", agreement.getSecurityDeposit());
        agreementParameters.put("securityDepositDate", agreement.getSecurityDepositDate());
        agreementParameters.put("solvencyCertificateDate", agreement.getSolvencyCertificateDate());
        agreementParameters.put("solvencyCertificateNo", agreement.getSolvencyCertificateNo());
        agreementParameters.put("status", agreement.getStatus().toString());
        agreementParameters.put("tinNumber", agreement.getTinNumber());
        agreementParameters.put("tenderDate", agreement.getTenderDate());
        agreementParameters.put("tenderNumber", agreement.getTenderNumber());
        agreementParameters.put("tradelicenseNumber", agreement.getTradelicenseNumber());
        agreementParameters.put("createdBy", agreement.getCreatedBy());
        agreementParameters.put("lastmodifiedBy", agreement.getLastmodifiedBy());
        agreementParameters.put("lastmodifiedDate", new Date());
        agreementParameters.put("allottee", agreement.getAllottee().getId());
        agreementParameters.put("asset", agreement.getAsset().getId());
        agreementParameters.put("rentIncrement",
                agreement.getRentIncrementMethod() == null ? null : agreement.getRentIncrementMethod().getId());
        agreementParameters.put("acknowledgementNumber", agreement.getAcknowledgementNumber());
        agreementParameters.put("stateId", agreement.getStateId());
        agreementParameters.put("tenantId", agreement.getTenantId());
        agreementParameters.put("goodWillAmount", agreement.getGoodWillAmount());
        agreementParameters.put("timePeriod", agreement.getTimePeriod());
        agreementParameters.put("collectedSecurityDeposit", agreement.getCollectedSecurityDeposit());
        agreementParameters.put("collectedGoodWillAmount", agreement.getCollectedGoodWillAmount());
        agreementParameters.put("source", agreement.getSource().toString());
        agreementParameters.put("reason", processMap.get("reason"));
        agreementParameters.put("terminationDate", processMap.get("terminationDate"));
        agreementParameters.put("courtReferenceNumber", processMap.get("courtReferenceNumber"));
        agreementParameters.put("action", agreement.getAction().toString());
        agreementParameters.put("courtCaseNo", processMap.get("courtCaseNo"));
        agreementParameters.put("courtCaseDate", processMap.get("courtCaseDate"));
        agreementParameters.put("courtFixedRent", processMap.get("courtFixedRent"));
        agreementParameters.put("effectiveDate", processMap.get("effectiveDate"));
        agreementParameters.put("judgementNo", processMap.get("judgementNo"));
        agreementParameters.put("judgementDate", processMap.get("judgementDate"));
        agreementParameters.put("judgementRent", processMap.get("judgementRent"));
        agreementParameters.put("remissionRent", processMap.get("remissionRent"));
        agreementParameters.put("remissionFromDate", processMap.get("remissionFromDate"));
        agreementParameters.put("remissionToDate", processMap.get("remissionToDate"));
        agreementParameters.put("remissionOrder", processMap.get("remissionOrder"));
        agreementParameters.put("remissionDate", processMap.get("remissionDate"));
        agreementParameters.put("adjustmentStartDate", agreement.getAdjustmentStartDate());
        agreementParameters.put("isUnderWorkflow", agreement.getIsUnderWorkflow());
        agreementParameters.put("firstAllotment", agreement.getFirstAllotment());
        agreementParameters.put("gstin", agreement.getGstin());
        agreementParameters.put("municaipalOrderNo", agreement.getMunicipalOrderNumber());
        agreementParameters.put("municipalOrderDate", agreement.getMunicipalOrderDate());
        agreementParameters.put("govtOrderNo", agreement.getGovernmentOrderNumber());
        agreementParameters.put("govtOrderDate", agreement.getGovernmentOrderDate());
        agreementParameters.put("renewalDate", agreement.getRenewalDate());
        agreementParameters.put("basisAllotment",
                agreement.getBasisOfAllotment() == null ? null : agreement.getBasisOfAllotment().toString());
        agreementParameters.put("resCategory", agreement.getReservationCategory());
        agreementParameters.put("oldAgreementNumber", agreement.getOldAgreementNumber());
        agreementParameters.put("referenceNo", agreement.getReferenceNumber());
        agreementParameters.put("floorNo", agreement.getFloorNumber());
        agreementParameters.put("parent", agreement.getParent());
        agreementParameters.put("tenderOpeningDate", agreement.getTenderOpeningDate());
        agreementParameters.put("auctionAmount", agreement.getAuctionAmount());
        agreementParameters.put("solvencyAmount", agreement.getSolvencyAmount());
        agreementParameters.put("isHistory", agreement.getIsHistory());
        agreementParameters.put("serviceTax", agreement.getServiceTax());
        agreementParameters.put("cgst", agreement.getCgst());
        agreementParameters.put("sgst", agreement.getSgst());
        agreementParameters.put("noticeNumber", agreement.getNoticeNumber());

        return agreementParameters;
    }

    private Map<String, Object> getProcessMap(final AgreementRequest agreementRequest) {

        final Agreement agreement = agreementRequest.getAgreement();
        String orderNumber = null;
        String reason = null;
        String courtReferenceNumber = null;
        String courtCaseNo = null;
        Date courtCaseDate = null;
        Double courtFixedRent = null;
        Date effectiveDate = null;
        Date orderDate = null;
        Date terminationDate = null;
        String judgementNo = null;
        Date judgementDate = null;
        BigDecimal judgementRent = null;
        Date remissionFromDate = null;
        Date remissionToDate = null;
        String remissionOrder = null;
        Date remissionDate = null;
        BigDecimal remissionRent = null;

        final Action action = agreement.getAction();

        if (Action.CREATE.equals(action)) {
            orderNumber = agreement.getOrderNumber();
            orderDate = agreement.getOrderDate();
        } else if (Action.EVICTION.equals(action)) {
            final Eviction eviction = agreement.getEviction();
            orderNumber = eviction.getEvictionProceedingNumber();
            orderDate = eviction.getEvictionProceedingDate();
            reason = eviction.getReasonForEviction();
            courtReferenceNumber = eviction.getCourtReferenceNumber();
        } else if (Action.CANCELLATION.equals(action)) {
            final Cancellation cancellation = agreement.getCancellation();
            orderNumber = cancellation.getOrderNumber();
            orderDate = cancellation.getOrderDate();
            reason = cancellation.getReasonForCancellation().toString();
            terminationDate = cancellation.getTerminationDate();

        } else if (Action.RENEWAL.equals(action)) {
            final Renewal renewal = agreement.getRenewal();
            orderNumber = renewal.getRenewalOrderNumber();
            orderDate = renewal.getRenewalOrderDate();
            reason = renewal.getReasonForRenewal();

        } else if (Action.OBJECTION.equals(action)) {
            final Objection objection = agreement.getObjection();
            courtCaseNo = objection.getCourtCaseNo();
            courtCaseDate = objection.getCourtCaseDate();
            courtFixedRent = objection.getCourtFixedRent();
            effectiveDate = objection.getEffectiveDate();

        } else if (Action.JUDGEMENT.equals(action)) {
            final Judgement judgement = agreement.getJudgement();
            judgementNo = judgement.getJudgementNo();
            judgementDate = judgement.getJudgementDate();
            judgementRent = BigDecimal.valueOf(judgement.getJudgementRent());
            effectiveDate = judgement.getEffectiveDate();
        } else if (Action.REMISSION.equals(action)) {
            final Remission remission = agreement.getRemission();
            reason = remission.getRemissionReason();
            remissionFromDate = remission.getRemissionFromDate();
            remissionToDate = remission.getRemissionToDate();
            remissionOrder = remission.getRemissionOrder();
            remissionDate = remission.getRemissionDate();
            remissionRent = BigDecimal.valueOf(remission.getRemissionRent());
        }

        final Map<String, Object> processMap = new HashMap<>();
        processMap.put("orderNumber", orderNumber);
        processMap.put("orderDate", orderDate);
        processMap.put("reason", reason);
        processMap.put("courtReferenceNumber", courtReferenceNumber);
        processMap.put("terminationDate", terminationDate);
        processMap.put("courtCaseNo", courtCaseNo);
        processMap.put("courtCaseDate", courtCaseDate);
        processMap.put("courtFixedRent", courtFixedRent);
        processMap.put("effectiveDate", effectiveDate);
        processMap.put("judgementNo", judgementNo);
        processMap.put("judgementDate", judgementDate);
        processMap.put("judgementRent", judgementRent);
        processMap.put("remissionFromDate", remissionFromDate);
        processMap.put("remissionToDate", remissionToDate);
        processMap.put("remissionOrder", remissionOrder);
        processMap.put("remissionDate", remissionDate);
        processMap.put("remissionRent", remissionRent);

        return processMap;
    }

    public Long getAgreementID() {
        final String agreementIdQuery = "select nextval('seq_eglams_agreement')";
        try {
            return jdbcTemplate.queryForObject(agreementIdQuery, Long.class);
        } catch (final DataAccessException ex) {
            log.info("exception in getting agreement sequence" + ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void updateAgreementAdvance(final String acknowledgementNumber) {
        final String sql = "UPDATE eglams_agreement set is_advancepaid = true where acknowledgementnumber = '"
                + acknowledgementNumber + "'";
        log.info("advance paid update query :", sql);
        try {

            jdbcTemplate.update(sql);
        } catch (final DataAccessException ex) {
            log.info("exception while updating is_advancepaid flag" + ex);
        }
    }

    public String getRenewalStatus(final String agreementnumber, final String tenantId) {
        final String sql = "select status from  eglams_agreement where agreement_no ='" + agreementnumber
                + "' and tenant_id = '" + tenantId + "' and action='RENEWAL'";
        log.info("renewal status query :", sql);
        String status = null;
        try {

            status = jdbcTemplate.queryForObject(sql, String.class);
        } catch (final DataAccessException ex) {
            log.info("exception while fetching renewal status of agreementNo :" + agreementnumber);
            throw new NoRenewalRecordsFoundException();
        }
        return status;
    }

    public String getObjectionStatus(final String agreementnumber, final String tenantId) {
        final String sql = "select status from  eglams_agreement where agreement_no ='" + agreementnumber
                + "' and tenant_id = '" + tenantId + "' and action='OBJECTION'";
        log.info("objection status query :", sql);
        String status = null;
        try {

            status = jdbcTemplate.queryForObject(sql, String.class);
        } catch (final DataAccessException ex) {
            log.info("exception while fetching objection status of agreementNo :" + agreementnumber);
            throw new NoObjectionRecordsFoundException();
        }
        return status;
    }

    private Map<Long, List<SubSeqRenewal>> getSubSeqRenewals(final List<Agreement> agreements) {
        final List<Long> agreementIds = new ArrayList<>();
        String query = "select * from eglams_history where agreementid in ( ";
        String agreementIdQuery = null;
        List<SubSeqRenewal> subSeqRenewals = null;
        Map<Long, List<SubSeqRenewal>> map = new HashMap<>();
        for (final Agreement agreement : agreements)
            agreementIds.add(agreement.getId());
        agreementIdQuery = AgreementQueryBuilder.getAgreementIdQuery(agreementIds);
        query = query + agreementIdQuery;
        try {
            subSeqRenewals = namedParameterJdbcTemplate.query(query, new SubSeqRenewalRowMapper());
        } catch (final DataAccessException e) {
            log.info("exception in getting subseqrenewal to enrich agreement :: " + e);
            throw new RuntimeException(e.getMessage());
        }
        if (!subSeqRenewals.isEmpty())
            map = subSeqRenewals.stream().collect(Collectors.groupingBy(SubSeqRenewal::getAgreementId));
        return map;
    }

    public void updateExistingAgreementAsHistory(final Agreement agreement, final String userId) {

        final String query = "update eglams_agreement set status='HISTORY',last_modified_by=:lastmodifiedBy,last_modified_date=now() where agreement_no=:agreementNumber and status ='ACTIVE' ";
        final Map<String, Object> params = new HashMap<>();
        params.put("agreementNumber", agreement.getAgreementNumber());
        params.put("lastmodifiedBy", userId);
        log.info("updating existing agreement as history,agreementNo :: " + agreement.getAgreementNumber());
        try {

            namedParameterJdbcTemplate.update(query, params);
        } catch (final Exception e) {
            log.info("exception while updating existing agreement as history :: " + e);
            throw new RuntimeException(e.getMessage());
        }

    }

    public Map<Long, List<Document>> getAttachedDocuments(final List<Agreement> agreements) {
        final List<Long> agreementIds = new ArrayList<>();
        final String query = "select document.*,filestore.filename as filename from eglams_document document,"
                + "eg_filestoremap filestore where filestore.filestoreid =document.filestore and agreement in (:agreementIds) ";
        final Map<String, Object> params = new HashMap<>();
        List<Document> documents = null;
        Map<Long, List<Document>> documentsMap = new HashMap<>();
        for (final Agreement agreement : agreements)
            agreementIds.add(agreement.getId());
        params.put("agreementIds", agreementIds);
        try {
            documents = namedParameterJdbcTemplate.query(query, params, new DocumentRowMapper());
        } catch (final DataAccessException e) {
            log.info("exception in getting attached documents :: " + e);
            throw new RuntimeException(e.getMessage());
        }
        if (!documents.isEmpty())
            documentsMap = documents.stream().collect(Collectors.groupingBy(Document::getAgreement));
        return documentsMap;

    }

    public void saveDocument(final Agreement agreement) {
        final List<Document> documents = agreement.getDocuments();

        if (documents != null) {
            final String sqlQuery = "INSERT INTO eglams_document (id,documenttype,agreement,filestore,tenantid) values "
                    + "(nextval('seq_eglams_document'),(select id from eglams_documenttype where "
                    + "name='Agreement Docs'  and tenantid= ?),?,?,?);";
            log.info("the update query for agreement docs : " + sqlQuery);
            final List<Object[]> documentBatchArgs = new ArrayList<>();

            for (final Document document : documents) {
                final Object[] documentRecord = {
                        agreement.getTenantId(), agreement.getId(),
                        document.getFileStore(), agreement.getTenantId()
                };
                documentBatchArgs.add(documentRecord);
            }

            try {
                jdbcTemplate.batchUpdate(sqlQuery, documentBatchArgs);
            } catch (final DataAccessException ex) {
                log.info("exception saving agreement document details" + ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

    }

    public void deleteDocument(final Long agreementId, final String tenantId) {
        final Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("agreementId", agreementId);
        paramsMap.put("tenantId", tenantId);
        final String deleteQuery = "DELETE FROM  eglams_document WHERE agreement = :agreementId AND tenantid=:tenantId";
        try {
            namedParameterJdbcTemplate.update(deleteQuery, paramsMap);

        } catch (final DataAccessException ex) {
            log.error("the exception in updating subseqrenewal in modify agreement" + ex);
            throw new RuntimeException(ex.getMessage());
        }
    }
}
