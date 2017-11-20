package org.egov.works.workorder.domain.repository;

import org.apache.commons.lang3.StringUtils;
import org.egov.works.common.persistence.repository.JdbcRepository;
import org.egov.works.workorder.persistence.helper.LetterOfAcceptanceHelper;
import org.egov.works.workorder.web.contract.Contractor;
import org.egov.works.workorder.web.contract.LetterOfAcceptance;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceSearchCriteria;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LetterOfAcceptanceJdbcRepository extends JdbcRepository {

    @Autowired
    private WorksMastersRepository worksMastersRepository;


    public static final String TABLE_NAME = "egw_letterofacceptance loa";
    public static final String LOA_ESTIMATESEARCH_EXTENTION = "egw_letterofacceptanceestimate loaestimate";


    public List<LetterOfAcceptance> searchLOAs(final LetterOfAcceptanceSearchCriteria letterOfAcceptanceSearchCriteria, final RequestInfo requestInfo) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (letterOfAcceptanceSearchCriteria.getSortBy() != null
                && !letterOfAcceptanceSearchCriteria.getSortBy().isEmpty()) {
            validateSortByOrder(letterOfAcceptanceSearchCriteria.getSortBy());
            validateEntityFieldName(letterOfAcceptanceSearchCriteria.getSortBy(), LetterOfAcceptance.class);
        }

        if (letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers() != null && !letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers().isEmpty())
            tableName += LOA_ESTIMATESEARCH_EXTENTION;

        String orderBy = "order by id";
        if (letterOfAcceptanceSearchCriteria.getSortBy() != null
                && !letterOfAcceptanceSearchCriteria.getSortBy().isEmpty()) {
            orderBy = "order by " + letterOfAcceptanceSearchCriteria.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", TABLE_NAME);

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
            params.append("loa.loaNumber like (:loaNumber)");
            paramValues.put("loa.loaNumber", "%" + letterOfAcceptanceSearchCriteria.getLoaNumbers() + "%");
        } else if (letterOfAcceptanceSearchCriteria.getLoaNumbers() != null && letterOfAcceptanceSearchCriteria.getLoaNumbers().size() > 1) {
            addAnd(params);
            params.append("loa.loaNumber in(:loaNumber)");
            paramValues.put("loaNumber", letterOfAcceptanceSearchCriteria.getLoaNumbers());
        }
        if (letterOfAcceptanceSearchCriteria.getDepartment() != null) {
            addAnd(params);
            params.append("loa.department =:department");
            paramValues.put("department", letterOfAcceptanceSearchCriteria.getDepartment());
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
            params.append("status in(:status)");
            paramValues.put("status", letterOfAcceptanceSearchCriteria.getStatuses());
        }

        if (letterOfAcceptanceSearchCriteria.getContractorCodes() != null && !letterOfAcceptanceSearchCriteria.getContractorCodes().isEmpty()) {
            searchByContractorCodes(letterOfAcceptanceSearchCriteria.getContractorCodes(), params, paramValues);
        }

        List<String> contractorCodes = new ArrayList<>();
        if (letterOfAcceptanceSearchCriteria.getContractorNames() != null && !letterOfAcceptanceSearchCriteria.getContractorNames().isEmpty()) {
            List<Contractor> contractors = worksMastersRepository.searchScheduleOfRates(letterOfAcceptanceSearchCriteria.getTenantId(), letterOfAcceptanceSearchCriteria.getContractorNames(), requestInfo);
            for (Contractor contractor : contractors)
                contractorCodes.add(contractor.getCode());
            if(!contractorCodes.isEmpty())
              searchByContractorCodes(contractorCodes, params, paramValues);
        }

        if (letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers() != null && letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers().size() == 1) {
            addAnd(params);
            params.append("loaestimate.letterofacceptance = loa.loanumber and upper(loaestimate.detailedestimate) like :detailedestimatenumber");
            paramValues.put("detailedestimatenumber", '%' + letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers().get(0).toUpperCase() + '%');
        } else if (letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers() != null && letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers().size() > 1) {
            addAnd(params);
            params.append("loaestimate.letterofacceptance = loa.loanumber and loaestimate.detailedestimate in :detailedestimatenumber");
            paramValues.put("detailedestimatenumber", letterOfAcceptanceSearchCriteria.getDetailedEstimateNumbers());
        }

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
            loas.add(letterOfAcceptance);
        }
        return loas;
    }



    private void searchByContractorCodes(List<String> contractorCodes, StringBuilder params, Map<String, Object> paramValues) {

        if (contractorCodes.size() == 1) {
            addAnd(params);
            params.append("upper(loa.contractor) like :contractorcode");
            paramValues.put("contractorcode", '%' + contractorCodes.get(0).toUpperCase() + '%');
        } else if (contractorCodes.size() > 1) {
            addAnd(params);
            params.append("loa.contractor in :contractorcode");
            paramValues.put("contractorcode", contractorCodes);
        }
    }
}
