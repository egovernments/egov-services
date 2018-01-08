package org.egov.works.masters.domain.repository.builder;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.works.masters.web.contract.ContractorSearchCriteria;
import org.springframework.stereotype.Service;

@Service
public class ContractorQueryBuilder {
    public static final String BASE_SEARCH_QUERY = "SELECT * FROM egw_contractor ec where ec.id is not null";

    public String getSearchQuery(ContractorSearchCriteria contractorSearchCriteria, Map params) {
        StringBuilder selectQuery = new StringBuilder(BASE_SEARCH_QUERY);
        appendParams(contractorSearchCriteria, params, selectQuery);
//        appendLimitAndOffset(contractorSearchCriteria, params, selectQuery);
        return selectQuery.toString();
    }

    private void appendParams(ContractorSearchCriteria contractorSearchCriteria, Map params,
            StringBuilder selectQuery) {

        if (contractorSearchCriteria.getTenantId() != null) {
            selectQuery.append(" and ec.tenantid = :tenantId");
            params.put("tenantId", contractorSearchCriteria.getTenantId());
        }

        if (contractorSearchCriteria.getIds() != null && !contractorSearchCriteria.getIds().isEmpty()) {
            selectQuery.append(" and ec.id in (:contractorIds)");
            params.put("contractorIds", contractorSearchCriteria.getIds());
        }

        if (contractorSearchCriteria.getCode() != null && !contractorSearchCriteria.getCode().isEmpty()) {
            selectQuery.append(" and ec.code = :code");
            params.put("code", contractorSearchCriteria.getCode());
        }

        if (contractorSearchCriteria.getName() != null && !contractorSearchCriteria.getName().isEmpty()) {
            selectQuery.append(" and ec.name = :name");
            params.put("name", contractorSearchCriteria.getName());
        }

        if (contractorSearchCriteria.getContractClass() != null
                && !contractorSearchCriteria.getContractClass().isEmpty()) {
            selectQuery.append(" and ec.contractorclass in (:contractorClass)");
            params.put("contractorClass", contractorSearchCriteria.getContractClass());
        }

        if (contractorSearchCriteria.getStatuses() != null && !contractorSearchCriteria.getStatuses().isEmpty()) {
            selectQuery.append(" and ec.status in (:contractorStatus)");
            params.put("contractorStatus", contractorSearchCriteria.getStatuses());
        }

        if (StringUtils.isNotBlank(contractorSearchCriteria.getStatusLike())) {
            selectQuery.append(" and lower(ec.status) like (:contractorStatusLike)");
            params.put("contractorStatusLike", "%" + contractorSearchCriteria.getStatusLike().toLowerCase() + "%");
        }

        if (contractorSearchCriteria.getPmc() != null) {
            selectQuery.append(" and ec.pmc = :pmc");
            params.put("pmc", contractorSearchCriteria.getPmc());
        }
        
        if (contractorSearchCriteria.getCorrespondenceAddress() != null && !contractorSearchCriteria.getCorrespondenceAddress().isEmpty()) {
            selectQuery.append(" and ec.correspondenceaddress = :address");
            params.put("address", contractorSearchCriteria.getCorrespondenceAddress());
        }
        
        if (contractorSearchCriteria.getMobileNumber() != null && !contractorSearchCriteria.getMobileNumber().isEmpty()) {
            selectQuery.append(" and ec.mobilenumber = :mobileNumber");
            params.put("mobileNumber", contractorSearchCriteria.getMobileNumber());
        }
        
        if (contractorSearchCriteria.getEmailId() != null && !contractorSearchCriteria.getEmailId().isEmpty()) {
            selectQuery.append(" and ec.email = :emailId");
            params.put("emailId", contractorSearchCriteria.getEmailId());
        }
    }

    private StringBuilder appendLimitAndOffset(ContractorSearchCriteria contractorSearchCriteria,
            @SuppressWarnings("rawtypes") Map params, StringBuilder selectQuery) {

        selectQuery.append(" order by ec.id");
        selectQuery.append(" limit :pageSize");
        if (contractorSearchCriteria.getPageSize() != null)
            params.put("pageSize", contractorSearchCriteria.getPageSize());
        else
            params.put("pageSize", 500);

        selectQuery.append(" offset :pageNumber");

        if (contractorSearchCriteria.getPageNumber() != null)
            params.put("pageNumber", contractorSearchCriteria.getPageNumber());
        else
            params.put("pageNumber", 1);

        return selectQuery;
    }

}
