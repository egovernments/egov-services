package org.egov.works.workorder.domain.repository;

import org.egov.works.common.persistence.repository.JdbcRepository;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ritesh on 27/11/17.
 */
@Repository
public class WorkOrderJdbcRepository extends JdbcRepository {


    public static final String TABLE_NAME = "egw_workorder wo";
    public static final String LOA_ESTIMATESEARCH_EXTENTION = "egw_letterofacceptanceestimate loaestimate";
    @Autowired
    private WorksMastersRepository worksMastersRepository;

    public List<WorkOrder> searchLOAs(final WorkOrderSearchContract workOrderSearchContract, final RequestInfo requestInfo) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (workOrderSearchContract.getSortBy() != null
                && !workOrderSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(workOrderSearchContract.getSortBy());
            validateEntityFieldName(workOrderSearchContract.getSortBy(), LetterOfAcceptance.class);
        }

        if (workOrderSearchContract.getDetailedEstimateNumbers() != null && !workOrderSearchContract.getDetailedEstimateNumbers().isEmpty()
                || workOrderSearchContract.getDepartment() != null && !workOrderSearchContract.getDepartment().isEmpty())
            tableName += LOA_ESTIMATESEARCH_EXTENTION;

        String orderBy = "order by wo.workorderdate";
        if (workOrderSearchContract.getSortBy() != null
                && !workOrderSearchContract.getSortBy().isEmpty()) {
            orderBy = "order by " + workOrderSearchContract.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (workOrderSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("wo.tenantId =:tenantId");
            paramValues.put("tenantId", workOrderSearchContract.getTenantId());
        }
        if (workOrderSearchContract.getIds() != null) {
            addAnd(params);
            params.append("wo.id in(:ids) ");
            paramValues.put("ids", workOrderSearchContract.getIds());
        }

        if (workOrderSearchContract.getStatuses() != null) {
            addAnd(params);
            params.append("wo.status in(:status)");
            paramValues.put("status", workOrderSearchContract.getStatuses());
        }

        if (workOrderSearchContract.getFromDate() != null) {
            addAnd(params);
            params.append("wo.workorderdate >= (:createddate)");
            paramValues.put("createddate", workOrderSearchContract.getFromDate());
        }

        if (workOrderSearchContract.getToDate() != null) {
            addAnd(params);
            params.append("wo.workorderdate <= (:todate)");
            paramValues.put("todate", workOrderSearchContract.getToDate());
        }

        if (workOrderSearchContract.getLoaNumbers() != null && workOrderSearchContract.getLoaNumbers().size() == 1) {
            addAnd(params);
            params.append("wo.letterofacceptance in (select id from egw_letterofacceptance loa where lower(loa.loanumber) = :loaNumber)");
            paramValues.put("loaNumber", "%" + workOrderSearchContract.getLoaNumbers().get(0).toLowerCase() + "%");
        } else if (workOrderSearchContract.getLoaNumbers() != null) {
            addAnd(params);
            params.append("wo.letterofacceptance in (select id from egw_letterofacceptance loa where lower(loa.loanumber) in (:loaNumbers))");
            paramValues.put("loaNumbers", workOrderSearchContract.getLoaNumbers());
        }

        if (workOrderSearchContract.getContractorCodes() != null && !workOrderSearchContract.getContractorCodes().isEmpty() && workOrderSearchContract.getContractorCodes().size() == 1) {
            addAnd(params);
            params.append("wo.letterofacceptance in (select id from egw_letterofacceptance loa where loa.contractor = (:contractorcode))");
            paramValues.put("contractorcode", workOrderSearchContract.getContractorCodes());
        } else if (workOrderSearchContract.getContractorCodes() != null) {
            addAnd(params);
            params.append("wo.letterofacceptance in (select id from egw_letterofacceptance loa where lower(loa.contractor) in (:contractorcodes))");
            paramValues.put("contractorcodes", workOrderSearchContract.getContractorCodes());
        }


        List<String> contractorCodes = new ArrayList<>();
        if (workOrderSearchContract.getContractorNames() != null && !workOrderSearchContract.getContractorNames().isEmpty()) {
            List<Contractor> contractors = worksMastersRepository.searchContractors(workOrderSearchContract.getTenantId(), workOrderSearchContract.getContractorNames(), requestInfo);
            for (Contractor contractor : contractors)
                contractorCodes.add(contractor.getCode());
            if (!contractorCodes.isEmpty()) {
                if (!contractorCodes.isEmpty() && contractorCodes.size() == 1) {
                    addAnd(params);
                    params.append("wo.letterofacceptance in (select id from egw_letterofacceptance loa where lower(loa.contractor) = (:contractorcode))");
                    paramValues.put("contractorcode", contractorCodes.get(0).toLowerCase());
                } else if (contractorCodes != null) {
                    addAnd(params);
                    params.append("wo.letterofacceptance in (select id from egw_letterofacceptance loa where loa.contractor in (:contractorcodes))");
                    paramValues.put("contractorcodes", contractorCodes);
                }
            }
        }


        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(WorkOrder.class);

        List<WorkOrder> loaList = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
        return loaList;
    }

}
