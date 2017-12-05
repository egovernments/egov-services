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


    public List<LetterOfAcceptance> searchLOAs(final LetterOfAcceptanceSearchContract letterOfAcceptanceSearchCriteria, final RequestInfo requestInfo) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (letterOfAcceptanceSearchCriteria.getSortBy() != null
                && !letterOfAcceptanceSearchCriteria.getSortBy().isEmpty()) {
            validateSortByOrder(letterOfAcceptanceSearchCriteria.getSortBy());
            validateEntityFieldName(letterOfAcceptanceSearchCriteria.getSortBy(), LetterOfAcceptance.class);
        }

        if (letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers() != null && !letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers().isEmpty()
                || letterOfAcceptanceSearchCriteria.getDepartment() != null && !letterOfAcceptanceSearchCriteria.getDepartment().isEmpty())
            tableName += LOA_ESTIMATESEARCH_EXTENTION;

        String orderBy = "order by loa.id";
        if (letterOfAcceptanceSearchCriteria.getSortBy() != null
                && !letterOfAcceptanceSearchCriteria.getSortBy().isEmpty()) {
            orderBy = "order by loa." + letterOfAcceptanceSearchCriteria.getSortBy();
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
        if (letterOfAcceptanceSearchCriteria.getLoaNumbers() != null && letterOfAcceptanceSearchCriteria.getLoaNumbers().size() == 1) {
            addAnd(params);
            params.append("loa.loaNumber like (:loaNumbers)");
            paramValues.put("loaNumbers", "%" + letterOfAcceptanceSearchCriteria.getLoaNumbers().get(0).toUpperCase() + "%");
        } else if (letterOfAcceptanceSearchCriteria.getLoaNumbers() != null && letterOfAcceptanceSearchCriteria.getLoaNumbers().size() > 1) {
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

        if (letterOfAcceptanceSearchCriteria.getContractorCodes() != null && !letterOfAcceptanceSearchCriteria.getContractorCodes().isEmpty()) {
            searchByContractorCodes(letterOfAcceptanceSearchCriteria.getContractorCodes(), params, paramValues);
        }

        List<String> contractorCodes = new ArrayList<>();
        if (letterOfAcceptanceSearchCriteria.getContractorNames() != null && !letterOfAcceptanceSearchCriteria.getContractorNames().isEmpty()) {
            List<Contractor> contractors = worksMastersRepository.searchContractors(letterOfAcceptanceSearchCriteria.getTenantId(), letterOfAcceptanceSearchCriteria.getContractorNames(), requestInfo);
            for (Contractor contractor : contractors)
                contractorCodes.add(contractor.getCode());
            if(!contractorCodes.isEmpty())
              searchByContractorCodes(contractorCodes, params, paramValues);
        }

        List<String> estimateNumbers = new ArrayList<>();
        if(letterOfAcceptanceSearchCriteria.getDepartment() != null && !letterOfAcceptanceSearchCriteria.getDepartment().isEmpty()) {
            List<DetailedEstimate> detailedEstimates = estimateRepository.searchDetailedEstimatesByDepartment(letterOfAcceptanceSearchCriteria.getDepartment(),letterOfAcceptanceSearchCriteria.getTenantId(),requestInfo);
            for(DetailedEstimate detailedEstimate : detailedEstimates)
                estimateNumbers.add(detailedEstimate.getEstimateNumber());

            addAnd(params);
            params.append("loaestimate.letterofacceptance = loa.loanumber and loaestimate.detailedestimate in :detailedestimatenumber and loaestimate.tenantId =:tenantId");
            paramValues.put("detailedestimatenumber", letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers());
            paramValues.put("tenantId", letterOfAcceptanceSearchCriteria.getTenantId());

        }

        if (letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers() != null && letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers().size() == 1) {
            addAnd(params);
            params.append("loaestimate.letterofacceptance = loa.loanumber and upper(loaestimate.detailedestimate) like :detailedestimatenumber  and loaestimate.tenantId =:tenantId");
            paramValues.put("tenantId", letterOfAcceptanceSearchCriteria.getTenantId());
            paramValues.put("detailedestimatenumber", '%' + letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers().get(0).toUpperCase() + '%');
        } else if (letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers() != null && letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers().size() > 1) {
            addAnd(params);
            params.append("loaestimate.letterofacceptance = loa.loanumber and loaestimate.detailedestimate in :detailedestimatenumber  and loaestimate.tenantId =:tenantId");
            paramValues.put("tenantId", letterOfAcceptanceSearchCriteria.getTenantId());
            paramValues.put("detailedestimatenumber", letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers());
        }

        params.append(" and loa.deleted = false");
        
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

            LetterOfAcceptanceEstimateSearchContract letterOfAcceptanceEstimateSearchCriteria = LetterOfAcceptanceEstimateSearchContract.builder()
                    .tenantId(letterOfAcceptance.getTenantId()).letterOfAcceptanceIds(Arrays.asList(letterOfAcceptance.getId())).build();

            SecurityDepositSearchContract securityDepositeSearchCriteria = SecurityDepositSearchContract.builder()
                    .tenantId(letterOfAcceptance.getTenantId()).letterOfAcceptanceIds(Arrays.asList(letterOfAcceptance.getId())).build();

            letterOfAcceptance.setSecurityDeposits(securityDepositeJdbcRepository.searchSecurityDeposite(securityDepositeSearchCriteria));
            letterOfAcceptance.setLetterOfAcceptanceEstimates(letterOfAcceptanceEstimateRepository.searchLOAs(letterOfAcceptanceEstimateSearchCriteria,requestInfo));

            loas.add(letterOfAcceptance);


        }
        return loas;
    }



    private void searchByContractorCodes(List<String> contractorCodes, StringBuilder params, Map<String, Object> paramValues) {

        if (contractorCodes.size() == 1) {
            addAnd(params);
            params.append("upper(loa.contractor) like :contractorcode");
            paramValues.put("contractorcode", '%' + contractorCodes.get(0).toUpperCase() + '%');
        } else {
            addAnd(params);
            params.append("loa.contractor in :contractorcode");
            paramValues.put("contractorcode", contractorCodes);
        }
    }
}
