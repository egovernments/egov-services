package org.egov.works.measurementbook.domain.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.works.measurementbook.common.persistence.repository.JdbcRepository;
import org.egov.works.measurementbook.persistence.helper.ContractorBillHelper;
import org.egov.works.measurementbook.web.contract.ContractorBill;
import org.egov.works.measurementbook.web.contract.ContractorBillSearchContract;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptance;
import org.egov.works.measurementbook.web.contract.MBForContractorBillSearchContract;
import org.egov.works.measurementbook.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ContractorBillJdbcRepository extends JdbcRepository {

    @Autowired
    private LetterOfAcceptanceRepository letterOfAcceptanceRepository;

    public static final String TABLE_NAME = "egw_contractorbill cb";
    public static final String CB_LOAESTIMATE_EXTENTION = ", egw_letterofacceptanceestimate loaestimate";
    public static final String CB_BILLREGISTER_EXTENTION = ", eg_billregister br";
    public static final String CB_MEASUREMENTBOOK_TABLE_NAME = "egw_contractorbill_mb cmb";

    public List<ContractorBill> searchContractorBills(final ContractorBillSearchContract contractorBillSearchContract,
            final RequestInfo requestInfo) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (contractorBillSearchContract.getSortProperty() != null
                && !contractorBillSearchContract.getSortProperty().isEmpty()) {
            validateSortByOrder(contractorBillSearchContract.getSortProperty());
            validateEntityFieldName(contractorBillSearchContract.getSortProperty(), ContractorBill.class);
        }

        if (contractorBillSearchContract.getLetterOfAcceptanceNumbers() != null
                && !contractorBillSearchContract.getLetterOfAcceptanceNumbers().isEmpty())
            tableName += CB_LOAESTIMATE_EXTENTION;

        if (contractorBillSearchContract.getBillNumbers() != null
                && !contractorBillSearchContract.getBillNumbers().isEmpty()
                || contractorBillSearchContract.getBillTypes() != null
                        && !contractorBillSearchContract.getBillTypes().isEmpty())
            tableName += CB_BILLREGISTER_EXTENTION;

        String orderBy = "order by cb.id";
        if (contractorBillSearchContract.getSortProperty() != null
                && !contractorBillSearchContract.getSortProperty().isEmpty()) {
            orderBy = "order by cb." + contractorBillSearchContract.getSortProperty();
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (contractorBillSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("cb.tenantId =:tenantId");
            paramValues.put("tenantId", contractorBillSearchContract.getTenantId());
        }
        if (contractorBillSearchContract.getIds() != null) {
            addAnd(params);
            params.append("cb.id in(:ids) ");
            paramValues.put("ids", contractorBillSearchContract.getIds());
        }
        if (contractorBillSearchContract.getLetterOfAcceptanceNumbers() != null
                && !contractorBillSearchContract.getLetterOfAcceptanceNumbers().isEmpty()) {
            searchByLOANumbers(contractorBillSearchContract.getLetterOfAcceptanceNumbers(), params, paramValues);
            /*addAnd(params);
            params.append(
                    "cb.letterOfAcceptanceEstimate = loaestimate.id and loaestimate.letterOfAcceptance in (:loaNumber)");
            paramValues.put("loaNumber", contractorBillSearchContract.getLetterOfAcceptanceNumbers());*/
        }

        if (StringUtils.isNotBlank(contractorBillSearchContract.getLetterOfAcceptanceNumberLike())) {
            searchByLOANumberLike(Arrays.asList(contractorBillSearchContract.getLetterOfAcceptanceNumberLike()) , params, paramValues);
           /* addAnd(params);
            params.append(
                    "cb.letterOfAcceptanceEstimate = loaestimate.id and lower(loaestimate.letterOfAcceptance) like (:loaNumberlike)");
            paramValues.put("loaNumberlike",
                    "%" + contractorBillSearchContract.getLetterOfAcceptanceNumberLike().toLowerCase() + "%");*/
        }
        // need to check on relation b.w cbill and bregister
        if (contractorBillSearchContract.getBillNumbers() != null
                && !contractorBillSearchContract.getBillNumbers().isEmpty()) {
            addAnd(params);
            params.append("br.billNumber in (:billNumbers)");
            paramValues.put("billNumbers", contractorBillSearchContract.getBillNumbers());
        }

        if (StringUtils.isNotBlank(contractorBillSearchContract.getBillNumberLike())) {
            addAnd(params);
            params.append("br.billNumber like (:billNumberLike)");
            paramValues.put("billNumberLike", "%" + contractorBillSearchContract.getBillNumberLike() + "%");
        }
        // need to check on relation b.w cbill and bregister
        if (contractorBillSearchContract.getBillTypes() != null
                && !contractorBillSearchContract.getBillTypes().isEmpty()) {
            addAnd(params);
            params.append("br.billSubType in (:billTypes)");
            paramValues.put("billTypes", contractorBillSearchContract.getBillTypes());
        }

        if (contractorBillSearchContract.getBillFromDate() != null) {
            addAnd(params);
            params.append("cb.createdtime >=:createdtime");
            paramValues.put("createdtime", contractorBillSearchContract.getBillFromDate());
        }
        if (contractorBillSearchContract.getBillToDate() != null) {
            addAnd(params);
            params.append("cb.createdtime <=:createdtime");
            paramValues.put("createdtime", contractorBillSearchContract.getBillToDate());
        }

        if (contractorBillSearchContract.getBillTypes() != null
                && !contractorBillSearchContract.getBillTypes().isEmpty()) {
            addAnd(params);
            params.append("cb.id in(:ids) ");
            paramValues.put("ids", contractorBillSearchContract.getIds());
        }

        if (contractorBillSearchContract.getStatuses() != null
                && !contractorBillSearchContract.getStatuses().isEmpty()) {
            addAnd(params);
            params.append("br.status in(:status)");
            paramValues.put("status", contractorBillSearchContract.getStatuses());
        }

        if (contractorBillSearchContract.getDepartmentCodes() != null
                && !contractorBillSearchContract.getDepartmentCodes().isEmpty()) {
            addAnd(params);
            params.append("br.department in(:departments) ");
            paramValues.put("departments", contractorBillSearchContract.getDepartmentCodes());
        }

        List<String> loaNumbers = null;
        if (contractorBillSearchContract.getContractorNames() != null
                && !contractorBillSearchContract.getContractorNames().isEmpty()) {
            List<LetterOfAcceptance> loas = letterOfAcceptanceRepository.searchLetterOfAcceptance(null,
                    contractorBillSearchContract.getContractorNames(), contractorBillSearchContract.getTenantId(),
                    requestInfo);
            loaNumbers = new ArrayList<String>();
            for (LetterOfAcceptance letterOfAcceptance : loas)
                loaNumbers.add(letterOfAcceptance.getLoaNumber());
            searchByLOANumbers(loaNumbers, params, paramValues);

        }

        if (StringUtils.isNotBlank(contractorBillSearchContract.getContractorNameLike())) {
            List<LetterOfAcceptance> loas = letterOfAcceptanceRepository.searchLetterOfAcceptance(null,
                    Arrays.asList(contractorBillSearchContract.getContractorNameLike()),
                    contractorBillSearchContract.getTenantId(), requestInfo);
            loaNumbers = new ArrayList<String>();
            for (LetterOfAcceptance letterOfAcceptance : loas)
                loaNumbers.add(letterOfAcceptance.getLoaNumber());
            searchByLOANumberLike(loaNumbers, params, paramValues);

        }

        if (params.length() > 0) {
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(ContractorBillHelper.class);

        List<ContractorBillHelper> cBillList = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues,
                row);
        List<ContractorBill> contractorBills = new ArrayList<>();
        for (ContractorBillHelper contractorBillHelper : cBillList) {
            ContractorBill contractorBill = contractorBillHelper.toDomain();
            //ContractorBillSearchContract contractorBillSearchCriteria = ContractorBillSearchContract.builder()
             //       .tenantId(contractorBill.getTenantId()).ids(Arrays.asList(contractorBill.getId())).build();
            contractorBills.add(contractorBill);
        }

        return contractorBills;
    }

    private void searchByLOANumbers(List<String> loaNumbers, StringBuilder params, Map<String, Object> paramValues) {
        addAnd(params);
        params.append(
                "cb.letterOfAcceptanceEstimate = loaestimate.id and loaestimate.letterOfAcceptance in (:loaNumber)");
        paramValues.put("loaNumber", loaNumbers);
    }

    private void searchByLOANumberLike(List<String> loaNumbers, StringBuilder params, Map<String, Object> paramValues) {
        addAnd(params);
        params.append(
                "cb.letterOfAcceptanceEstimate = loaestimate.id and upper(loaestimate.letterOfAcceptance) like (:loaNumber)");
        paramValues.put("loaNumber", "%" + loaNumbers.get(0).toUpperCase() + "%");
    }
    
    public List<ContractorBill> searchContractorBillsByMb(final MBForContractorBillSearchContract mbForContractorBillSearchContract,
            final RequestInfo requestInfo) {
        List<ContractorBill> contractorBills = new ArrayList<>();
        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";
        String tableName = CB_MEASUREMENTBOOK_TABLE_NAME;
        String orderBy = "order by cmb.id";
        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();
        if (mbForContractorBillSearchContract.getSortBy() != null
                && !mbForContractorBillSearchContract.getSortBy().isEmpty()) {
            orderBy = "order by cmb." + mbForContractorBillSearchContract.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (mbForContractorBillSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("cmb.tenantId =:tenantId");
            paramValues.put("tenantId", mbForContractorBillSearchContract.getTenantId());
        }
        if (mbForContractorBillSearchContract.getMeasurementBook() != null) {
            addAnd(params);
            params.append("cmb.measurementBook in(:measurementBook) ");
            paramValues.put("measurementBook", mbForContractorBillSearchContract.getMeasurementBook());
        }

        if (params.length() > 0) {
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(ContractorBillHelper.class);

        List<ContractorBillHelper> cBillList = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues,
                row);
        for (ContractorBillHelper contractorBillHelper : cBillList) {
            ContractorBill contractorBill = contractorBillHelper.toDomain();
            contractorBills.add(contractorBill);
        }
        return contractorBills;
    }

}
