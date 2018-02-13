package org.egov.works.workorder.domain.repository;

import org.apache.commons.lang3.StringUtils;
import org.egov.works.common.persistence.repository.JdbcRepository;
import org.egov.works.workorder.persistence.helper.LetterOfAcceptanceHelper;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class LetterOfAcceptanceJdbcRepository extends JdbcRepository {

    @Autowired
    private WorksMastersRepository worksMastersRepository;

    @Autowired
    private EstimateRepository estimateRepository;

    @Autowired
    private SecurityDepositJdbcRepository securityDepositeJdbcRepository;

    @Autowired
    private LetterOfAcceptanceEstimateRepository letterOfAcceptanceEstimateRepository;

    public static final String TABLE_NAME = "egw_letterofacceptance loa";
    public static final String LOA_ESTIMATESEARCH_EXTENTION = " , egw_letterofacceptanceestimate loaestimate";

    public List<LetterOfAcceptance> searchLOAs(final LetterOfAcceptanceSearchContract letterOfAcceptanceSearchCriteria,
            final RequestInfo requestInfo) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (letterOfAcceptanceSearchCriteria.getSortBy() != null
                && !letterOfAcceptanceSearchCriteria.getSortBy().isEmpty()) {
            validateSortByOrder(letterOfAcceptanceSearchCriteria.getSortBy());
            validateEntityFieldName(letterOfAcceptanceSearchCriteria.getSortBy(), LetterOfAcceptance.class);
        }

        if ((letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers() != null
                && !letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers().isEmpty())
                || (letterOfAcceptanceSearchCriteria.getDepartment() != null
                        && !letterOfAcceptanceSearchCriteria.getDepartment().isEmpty()) || StringUtils.isNotBlank(letterOfAcceptanceSearchCriteria.getDetailedEstimateNumberLike())
                || StringUtils.isNotBlank(letterOfAcceptanceSearchCriteria.getLoaEstimateId()))
            tableName += LOA_ESTIMATESEARCH_EXTENTION;

        StringBuilder orderBy = new StringBuilder("order by loa.createdtime");
        if (letterOfAcceptanceSearchCriteria.getSortBy() != null
                && !letterOfAcceptanceSearchCriteria.getSortBy().isEmpty()) {
            orderBy.delete(0,orderBy.length()).append("order by loa.").append(letterOfAcceptanceSearchCriteria.getSortBy());
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (letterOfAcceptanceSearchCriteria.getTenantId() != null) {
            addAnd(params);
            params.append("loa.tenantId =:tenantId");
            paramValues.put("tenantId", letterOfAcceptanceSearchCriteria.getTenantId());
        }
        if (letterOfAcceptanceSearchCriteria.getIds() != null) {
            addAnd(params);
            params.append("loa.id in(:ids) ");
            paramValues.put("ids", letterOfAcceptanceSearchCriteria.getIds());
        }
        if (StringUtils.isNotBlank(letterOfAcceptanceSearchCriteria.getLoaNumberLike())) {
            addAnd(params);
            params.append("upper(loa.loaNumber) like (:loaNumberLike)");
            paramValues.put("loaNumberLike", "%" + letterOfAcceptanceSearchCriteria.getLoaNumberLike().toUpperCase() + "%");
        }
        if (letterOfAcceptanceSearchCriteria.getLoaNumbers() != null
                && !letterOfAcceptanceSearchCriteria.getLoaNumbers().isEmpty()) {
            addAnd(params);
            params.append("loa.loaNumber in(:loaNumbers)");
            paramValues.put("loaNumbers", letterOfAcceptanceSearchCriteria.getLoaNumbers());
        }
        if (StringUtils.isNotBlank(letterOfAcceptanceSearchCriteria.getFileNumber())) {
            addAnd(params);
            params.append("loa.fileNumber in(:fileNumber)");
            paramValues.put("fileNumber", letterOfAcceptanceSearchCriteria.getFileNumber());
        }
        if (letterOfAcceptanceSearchCriteria.getFromDate() != null) {
            addAnd(params);
            params.append("loa.createdTime >=:fromDate");
            paramValues.put("fromDate", letterOfAcceptanceSearchCriteria.getFromDate());
        }
        if (letterOfAcceptanceSearchCriteria.getToDate() != null) {
            addAnd(params);
            params.append("loa.createdTime <=:toDate");
            paramValues.put("toDate", letterOfAcceptanceSearchCriteria.getToDate());
        }
        if (letterOfAcceptanceSearchCriteria.getSpillOverFlag() != null) {
            addAnd(params);
            params.append("loa.spillOverFlag =:spillOverFlag");
            paramValues.put("spillOverFlag", letterOfAcceptanceSearchCriteria.getSpillOverFlag());
        }

        if (letterOfAcceptanceSearchCriteria.getStatuses() != null) {
            addAnd(params);
            params.append("loa.status in(:status)");
            paramValues.put("status", letterOfAcceptanceSearchCriteria.getStatuses());
        }

        if (letterOfAcceptanceSearchCriteria.getContractorCodes() != null
                && !letterOfAcceptanceSearchCriteria.getContractorCodes().isEmpty()) {
            addAnd(params);
            params.append("loa.contractor in (:contractorcode)");
            paramValues.put("contractorcode", letterOfAcceptanceSearchCriteria.getContractorCodes());
        }

        if (StringUtils.isNotBlank(letterOfAcceptanceSearchCriteria.getContractorCodeLike())) {
            addAnd(params);
            params.append("lower(loa.contractor) like :contractorcodeLike");
            paramValues.put("contractorcodeLike",
                    "%" + letterOfAcceptanceSearchCriteria.getContractorCodeLike().toLowerCase() + "%");
        }

        List<String> contractorCodes = new ArrayList<>();
        if (letterOfAcceptanceSearchCriteria.getContractorNames() != null
                && !letterOfAcceptanceSearchCriteria.getContractorNames().isEmpty()) {
            List<Contractor> contractors = worksMastersRepository.searchContractors(
                    letterOfAcceptanceSearchCriteria.getTenantId(), letterOfAcceptanceSearchCriteria.getContractorNames(),
                    requestInfo);
            for (Contractor contractor : contractors)
                contractorCodes.add(contractor.getCode());
            if (!contractorCodes.isEmpty())
                searchByContractorCodes(contractorCodes, params, paramValues);
        }
        
        List<String> contractorCodesLike = new ArrayList<>();
        if (StringUtils.isNotBlank(letterOfAcceptanceSearchCriteria.getContractorNameLike())) {
            List<Contractor> contractors = worksMastersRepository.searchContractors(
                    letterOfAcceptanceSearchCriteria.getTenantId(), Arrays.asList(letterOfAcceptanceSearchCriteria.getContractorNameLike()),
                    requestInfo);
            for (Contractor contractor : contractors)
                contractorCodesLike.add(contractor.getCode());
            if (!contractorCodesLike.isEmpty()) {
                addAnd(params);
                params.append("upper(loa.contractor) like :contractorcode");
                paramValues.put("contractorcode", '%' + contractorCodesLike.get(0).toUpperCase() + '%');
            }
        }

        List<String> deIds = new ArrayList<>();
        if (letterOfAcceptanceSearchCriteria.getDepartment() != null
                && !letterOfAcceptanceSearchCriteria.getDepartment().isEmpty()) {
            List<DetailedEstimate> detailedEstimates = estimateRepository.searchDetailedEstimatesByDepartment(
                    letterOfAcceptanceSearchCriteria.getDepartment(), letterOfAcceptanceSearchCriteria.getTenantId(),
                    requestInfo);
            for (DetailedEstimate detailedEstimate : detailedEstimates)
                deIds.add(detailedEstimate.getId());

            addAnd(params);
            params.append(
                    "loaestimate.letterofacceptance = loa.id and loaestimate.detailedestimate in (:deIds) and loaestimate.tenantId =:tenantId");
            paramValues.put("deIds", deIds);

        }

        if(StringUtils.isNotBlank(letterOfAcceptanceSearchCriteria.getLoaEstimateId())) {
            addAnd(params);
            params.append(
                    "loaestimate.letterofacceptance = loa.id and loaestimate.id in (:loaestimateid) and loaestimate.tenantId =:tenantId");
            paramValues.put("loaestimateid", letterOfAcceptanceSearchCriteria.getLoaEstimateId());
        }

        if (StringUtils.isNotBlank(letterOfAcceptanceSearchCriteria.getDetailedEstimateNumberLike())) {
            addAnd(params);
            params.append(
                    "loaestimate.letterofacceptance = loa.id and loaestimate.detailedestimate in (select estimatenumber from egw_detailedestimate where lower(estimatenumber) like (:detailedestimatenumberlike))  and loaestimate.tenantId =:tenantId");
            paramValues.put("detailedestimatenumberlike",
                    '%' + letterOfAcceptanceSearchCriteria.getDetailedEstimateNumberLike().toLowerCase() + '%');
        }
        if (letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers() != null
                && !letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers().isEmpty()) {
            addAnd(params);
            params.append(
                    "loaestimate.letterofacceptance = loa.id and loaestimate.detailedestimate in (select estimatenumber from egw_detailedestimate where estimatenumber in (:detailedestimatenumber))  and loaestimate.tenantId =:tenantId and loaestimate.deleted = false");
            paramValues.put("detailedestimatenumber", letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers());
        }

        params.append(" and loa.deleted = false");
        params.append(" and loa.workOrderExists = ").append(letterOfAcceptanceSearchCriteria.getWorkOrderExists());
        params.append(" and loa.withAllOfflineStatusAndWONotCreated = ").append(letterOfAcceptanceSearchCriteria.getWithAllOfflineStatusAndWONotCreated());
        params.append(" and loa.milestoneExists = ").append(letterOfAcceptanceSearchCriteria.getMilestoneExists());
        params.append(" and loa.billExists = ").append(letterOfAcceptanceSearchCriteria.getBillExists());
        params.append(" and loa.contractorAdvanceExists = ").append(letterOfAcceptanceSearchCriteria.getContractorAdvanceExists());
        params.append(" and loa.mbExistsAndBillNotCreated = ").append(letterOfAcceptanceSearchCriteria.getMbExistsAndBillNotCreated());
        params.append(" and loa.withoutOfflineStatus = ").append(letterOfAcceptanceSearchCriteria.getWithoutOfflineStatus());

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(LetterOfAcceptanceHelper.class);

        List<LetterOfAcceptanceHelper> loaList = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
        List<LetterOfAcceptance> loas = new ArrayList<>();
        for (LetterOfAcceptanceHelper letterOfAcceptanceHelper : loaList) {
            LetterOfAcceptance letterOfAcceptance = letterOfAcceptanceHelper.toDomain();

            LetterOfAcceptanceEstimateSearchContract letterOfAcceptanceEstimateSearchCriteria = LetterOfAcceptanceEstimateSearchContract
                    .builder()
                    .tenantId(letterOfAcceptance.getTenantId()).letterOfAcceptanceIds(Arrays.asList(letterOfAcceptance.getId()))
                    .build();

            SecurityDepositSearchContract securityDepositeSearchCriteria = SecurityDepositSearchContract.builder()
                    .tenantId(letterOfAcceptance.getTenantId()).letterOfAcceptanceIds(Arrays.asList(letterOfAcceptance.getId()))
                    .build();

            letterOfAcceptance
                    .setSecurityDeposits(securityDepositeJdbcRepository.searchSecurityDeposite(securityDepositeSearchCriteria));
            letterOfAcceptance.setLetterOfAcceptanceEstimates(
                    letterOfAcceptanceEstimateRepository.searchLOAs(letterOfAcceptanceEstimateSearchCriteria, requestInfo));

            loas.add(letterOfAcceptance);

        }
        return loas;
    }

    private void searchByContractorCodes(List<String> contractorCodes, StringBuilder params, Map<String, Object> paramValues) {
        addAnd(params);
        params.append("loa.contractor in :contractorcode");
        paramValues.put("contractorcode", contractorCodes);
    }

}
