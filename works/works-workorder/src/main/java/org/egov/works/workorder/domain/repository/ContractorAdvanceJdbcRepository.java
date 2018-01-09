package org.egov.works.workorder.domain.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.works.common.persistence.repository.JdbcRepository;
import org.egov.works.workorder.web.contract.Contractor;
import org.egov.works.workorder.web.contract.ContractorAdvanceRequisition;
import org.egov.works.workorder.web.contract.ContractorAdvanceRequisitionHelper;
import org.egov.works.workorder.web.contract.ContractorAdvanceSearchContract;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceEstimateSearchContract;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ContractorAdvanceJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_contractoradvance advance";

    @Autowired
    private WorksMastersRepository worksMastersRepository;

    @Autowired
    private LetterOfAcceptanceEstimateRepository letterOfAcceptanceEstimateRepository;

    public List<ContractorAdvanceRequisition> searchContractorAdvanceRequisitions(final ContractorAdvanceSearchContract contractorAdvanceSearchContract,
            final RequestInfo requestInfo) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        StringBuilder tableName = new StringBuilder(TABLE_NAME);

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (contractorAdvanceSearchContract.getSortBy() != null && !contractorAdvanceSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(contractorAdvanceSearchContract.getSortBy());
            validateEntityFieldName(contractorAdvanceSearchContract.getSortBy(), ContractorAdvanceRequisitionHelper.class);
        }

        StringBuilder orderBy = new StringBuilder("order by advance.createdtime");
        if (StringUtils.isNotBlank(contractorAdvanceSearchContract.getSortBy())) {
            orderBy.delete(0,orderBy.length()).append("order by advance.").append(contractorAdvanceSearchContract.getSortBy());
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (contractorAdvanceSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("advance.tenantId =:tenantid");
            paramValues.put("tenantid", contractorAdvanceSearchContract.getTenantId());
        }
        if (contractorAdvanceSearchContract.getIds() != null) {
            addAnd(params);
            params.append("advance.id in(:ids) ");
            paramValues.put("ids", contractorAdvanceSearchContract.getIds());
        }

        if (contractorAdvanceSearchContract.getStatuses() != null) {
            addAnd(params);
            params.append("advance.status in(:statuses) ");
            paramValues.put("statuses", contractorAdvanceSearchContract.getStatuses());
        }

        if (contractorAdvanceSearchContract.getLoaNumbers() != null) {
            addAnd(params);
            params.append(
                    "advance.letterofacceptanceestimate in (select id from egw_letterofacceptanceestimate where letterofacceptance in (select id from egw_letterofacceptance  where loanumber in(:loaNumbers)  and tenantid =:tenantId and deleted = false) and tenantid =:tenantId and deleted = false) ");
            paramValues.put("loaNumbers", contractorAdvanceSearchContract.getLoaNumbers());
        }

        if (StringUtils.isNotBlank(contractorAdvanceSearchContract.getLoaNumberLike())) {
            addAnd(params);
            params.append(
                    "advance.letterofacceptanceestimate in (select id from egw_letterofacceptanceestimate where letterofacceptance in (select id from egw_letterofacceptance  where lower(loanumber) =:loaNumberLike and tenantid =:tenantId and deleted = false) and tenantid =:tenantId and deleted = false) ");
            paramValues.put("loaNumberLike", contractorAdvanceSearchContract.getLoaNumberLike().toLowerCase());
        }

        if (contractorAdvanceSearchContract.getContractorCodes() != null) {
            addAnd(params);
            params.append(
                    "advance.letterofacceptanceestimate in (select id from egw_letterofacceptanceestimate loae where loae.letterofacceptance in (select loa.id from egw_letterofacceptance loa where loa.contractor in(:contractorCodes)  and loa.tenantid =:tenantId and loa.deleted = false) and loae.tenantid =:tenantId and loae.deleted = false) ");
            paramValues.put("contractorCodes", contractorAdvanceSearchContract.getContractorCodes());
        }

        if (StringUtils.isNotBlank(contractorAdvanceSearchContract.getContractorCodeLike())) {
            addAnd(params);
            params.append(
                    "advance.letterofacceptanceestimate in (select id from egw_letterofacceptanceestimate loae where loae.letterofacceptance in (select loa.id from egw_letterofacceptance loa where lower(loa.contractor)=:contractorCodeLike and loa.tenantid =:tenantId and loa.deleted = false) and loae.tenantid =:tenantId and loae.deleted = false) ");
            paramValues.put("contractorCodeLike", contractorAdvanceSearchContract.getContractorCodeLike().toLowerCase());
        }

        List<String> contractorCodes = new ArrayList<>();
        if (contractorAdvanceSearchContract.getContractorNames() != null
                && !contractorAdvanceSearchContract.getContractorNames().isEmpty()) {
            List<Contractor> contractors = worksMastersRepository.searchContractors(contractorAdvanceSearchContract.getTenantId(),
                    contractorAdvanceSearchContract.getContractorNames(), requestInfo);
            for (Contractor contractor : contractors)
                contractorCodes.add(contractor.getCode());
            if (!contractorCodes.isEmpty()) {
                if (contractorCodes != null) {
                    addAnd(params);
                    params.append(
                            "advance.letterofacceptanceestimate in (select id from egw_letterofacceptanceestimate loae where loae.letterofacceptance in (select loa.id from egw_letterofacceptance loa where loa.contractor in(:contractorCodes)  and loa.tenantid =:tenantId and loa.deleted = false) and loae.tenantid =:tenantId and loae.deleted = false) ");
                    paramValues.put("contractorcodes", contractorCodes);
                }
            }
        }

        List<String> contractorCodes1 = new ArrayList<>();
        if (StringUtils.isNotBlank(contractorAdvanceSearchContract.getContractorNameLike())) {
            List<Contractor> contractors = worksMastersRepository.searchContractors(contractorAdvanceSearchContract.getTenantId(),
                    Arrays.asList(contractorAdvanceSearchContract.getContractorNameLike()), requestInfo);
            for (Contractor contractor : contractors)
                contractorCodes1.add(contractor.getCode());
            if (!contractorCodes1.isEmpty()) {
                if (contractorCodes1 != null) {
                    addAnd(params);
                    params.append(
                            "advance.letterofacceptanceestimate in (select id from egw_letterofacceptanceestimate loae where loae.letterofacceptance in (select loa.id from egw_letterofacceptance loa where lower(loa.contractor)=:contractorCodeLike and loa.tenantid =:tenantId and loa.deleted = false) and loae.tenantid =:tenantId and loae.deleted = false) ");
                    paramValues.put("contractorcodes", contractorCodes);
                }
            }
        }

        params.append(" and advance.deleted = false");

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(ContractorAdvanceRequisitionHelper.class);

        List<ContractorAdvanceRequisitionHelper> advanceRequisitionHelpers = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        List<ContractorAdvanceRequisition> advanceRequisitions = new ArrayList<>();
        for (ContractorAdvanceRequisitionHelper advanceRequisitionHelper : advanceRequisitionHelpers) {

            ContractorAdvanceRequisition advanceRequisition = advanceRequisitionHelper.toDomain();

            LetterOfAcceptanceEstimateSearchContract letterOfAcceptanceEstimateSearchContract = new LetterOfAcceptanceEstimateSearchContract();
            letterOfAcceptanceEstimateSearchContract
                    .setIds(Arrays.asList(advanceRequisition.getLetterOfAcceptanceEstimate().getId()));
            letterOfAcceptanceEstimateSearchContract.setTenantId(advanceRequisition.getTenantId());

            advanceRequisition.setLetterOfAcceptanceEstimate(letterOfAcceptanceEstimateRepository
                    .searchLOAs(letterOfAcceptanceEstimateSearchContract, requestInfo).get(0));

            advanceRequisitions.add(advanceRequisition);
        }

        return advanceRequisitions;
    }

}
