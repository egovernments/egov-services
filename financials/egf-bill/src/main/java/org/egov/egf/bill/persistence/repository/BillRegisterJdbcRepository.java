package org.egov.egf.bill.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.domain.model.Pagination;
import org.egov.egf.bill.persistence.entity.BillRegisterEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class BillRegisterJdbcRepository extends JdbcRepository {

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(BillRegisterEntity.TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public Pagination<BillRegister> search(final BillRegisterSearch billRegisterSearch) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (billRegisterSearch.getSortBy() != null
                && !billRegisterSearch.getSortBy().isEmpty()) {
            validateSortByOrder(billRegisterSearch.getSortBy());
            validateEntityFieldName(billRegisterSearch.getSortBy(),
                    BillRegisterEntity.class);
        }

        String orderBy = "order by billType";

        if (billRegisterSearch.getSortBy() != null
                && !billRegisterSearch.getSortBy().isEmpty())
            orderBy = "order by " + billRegisterSearch.getSortBy();

        searchQuery = searchQuery.replace(":tablename",
                BillRegisterEntity.TABLE_NAME);
        searchQuery = searchQuery.replace(":selectfields", " * ");

        // implement jdbc specfic search

        if (billRegisterSearch.getBillType() != null) {
            addAnd(params);
            params.append("billType =:billType");
            paramValues.put("billType", billRegisterSearch.getBillType());
        }

        if (billRegisterSearch.getBillSubType() != null) {
            addAnd(params);
            params.append("billSubType =:billSubType");
            paramValues.put("billSubType",
                    billRegisterSearch.getBillSubType());
        }

        if (billRegisterSearch.getGlcode() != null) {
            addAnd(params);
            params.append("glcode =:glcode");
            paramValues.put("glcode", billRegisterSearch.getGlcode());
        }

        if (billRegisterSearch.getDebitAmount() != null) {
            addAnd(params);
            params.append("debitAmount =:debitAmount");
            paramValues.put("debitAmount", billRegisterSearch.getDebitAmount());
        }

        if (billRegisterSearch.getCreditAmount() != null) {
            addAnd(params);
            params.append("creditAmount =:creditAmount");
            paramValues.put("creditAmount", billRegisterSearch.getCreditAmount());
        }

        if (billRegisterSearch.getTypes() != null) {
            addAnd(params);
            params.append("type in (:types)");
            paramValues.put("types", new ArrayList<>(Arrays.asList(billRegisterSearch.getTypes().split(","))));
        }

        if (billRegisterSearch.getNames() != null) {
            addAnd(params);
            params.append("name in (:names)");
            paramValues.put("names", new ArrayList<>(Arrays.asList(billRegisterSearch.getNames().split(","))));
        }

        if (billRegisterSearch.getBillNumbers() != null) {
            addAnd(params);
            params.append("billNumber in (:billNumbers)");
            paramValues.put("billNumbers",
                    new ArrayList<>(Arrays.asList(billRegisterSearch.getBillNumbers().split(","))));
        }

        if (billRegisterSearch.getBillFromDate() != null) {
            addAnd(params);
            params.append("billDate >= (:billFromDate)");
            paramValues.put("billFromDate", billRegisterSearch.getBillFromDate());
        }

        if (billRegisterSearch.getBillToDate() != null) {
            addAnd(params);
            params.append("billDate >= (:billToDate)");
            paramValues.put("billToDate", billRegisterSearch.getBillToDate());
        }

        if (billRegisterSearch.getStatuses() != null) {
            addAnd(params);
            params.append("statusId >= (:statusId)");
            paramValues.put("statusId", billRegisterSearch.getStatuses());
        }

        if (billRegisterSearch.getStatuses() != null) {
            addAnd(params);
            params.append("statusid in (:statusids)");
            paramValues.put("statusids", new ArrayList<>(Arrays.asList(billRegisterSearch.getStatuses().split(","))));
        }

        if (billRegisterSearch.getBillNumber() != null) {
            addAnd(params);
            params.append("billNumber =:billNumber");
            paramValues.put("billNumber",
                    billRegisterSearch.getBillNumber());
        }

        if (billRegisterSearch.getBillDate() != null) {
            addAnd(params);
            params.append("billdate =:billDate");
            paramValues.put("billDate", billRegisterSearch.getBillDate());
        }

        if (billRegisterSearch.getBillAmount() != null) {
            addAnd(params);
            params.append("billAmount =:billAmount");
            paramValues.put("billAmount",
                    billRegisterSearch.getBillAmount());
        }

        if (billRegisterSearch.getPassedAmount() != null) {
            addAnd(params);
            params.append("passedAmount =:passedAmount");
            paramValues.put("passedAmount",
                    billRegisterSearch.getPassedAmount());
        }

        if (billRegisterSearch.getModuleName() != null) {
            addAnd(params);
            params.append("moduleName =:moduleName");
            paramValues.put("moduleName",
                    billRegisterSearch.getModuleName());
        }

        if (billRegisterSearch.getStatus() != null) {
            addAnd(params);
            params.append("status =:status");
            paramValues.put("status", billRegisterSearch.getStatus());
        }

        if (billRegisterSearch.getFund() != null) {
            addAnd(params);
            params.append("fund =:fund");
            paramValues.put("fund", billRegisterSearch.getFund());
        }

        if (billRegisterSearch.getFunction() != null) {
            addAnd(params);
            params.append("function =:function");
            paramValues.put("function",
                    billRegisterSearch.getFunction());
        }

        if (billRegisterSearch.getFundsource() != null) {
            addAnd(params);
            params.append("fundsource =:fundsource");
            paramValues.put("fundsource",
                    billRegisterSearch.getFundsource());
        }

        if (billRegisterSearch.getScheme() != null) {
            addAnd(params);
            params.append("scheme =:scheme");
            paramValues.put("scheme", billRegisterSearch.getScheme());
        }

        if (billRegisterSearch.getSubScheme() != null) {
            addAnd(params);
            params.append("subscheme =:subScheme");
            paramValues.put("subScheme",
                    billRegisterSearch.getSubScheme());
        }

        if (billRegisterSearch.getFunctionary() != null) {
            addAnd(params);
            params.append("functionary =:functionary");
            paramValues.put("functionary",
                    billRegisterSearch.getFunctionary());
        }

        if (billRegisterSearch.getLocation() != null) {
            addAnd(params);
            params.append("location =:location");
            paramValues.put("location",
                    billRegisterSearch.getLocation());
        }

        if (billRegisterSearch.getDepartment() != null) {
            addAnd(params);
            params.append("department =:department");
            paramValues.put("department",
                    billRegisterSearch.getDepartment());
        }

        if (billRegisterSearch.getSourcePath() != null) {
            addAnd(params);
            params.append("sourcepath =:sourcePath");
            paramValues.put("sourcePath",
                    billRegisterSearch.getSourcePath());
        }

        if (billRegisterSearch.getBudgetCheckRequired() != null) {
            addAnd(params);
            params.append("budgetcheckrequired =:budgetCheckRequired");
            paramValues.put("budgetCheckRequired",
                    billRegisterSearch.getBudgetCheckRequired());
        }

        if (billRegisterSearch.getBudgetAppropriationNo() != null) {
            addAnd(params);
            params.append("budgetappropriationno =:budgetAppropriationNo");
            paramValues.put("budgetAppropriationNo",
                    billRegisterSearch.getBudgetAppropriationNo());
        }

        if (billRegisterSearch.getPartyBillNumber() != null) {
            addAnd(params);
            params.append("partybillnumber =:partyBillNumber");
            paramValues.put("partyBillNumber",
                    billRegisterSearch.getPartyBillNumber());
        }

        if (billRegisterSearch.getPartyBillDate() != null) {
            addAnd(params);
            params.append("partybilldate =:partyBillDate");
            paramValues.put("partyBillDate",
                    billRegisterSearch.getPartyBillDate());
        }

        if (billRegisterSearch.getDescription() != null) {
            addAnd(params);
            params.append("description =:description");
            paramValues.put("description",
                    billRegisterSearch.getDescription());
        }

        if (billRegisterSearch.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", billRegisterSearch.getTenantId());
        }

        Pagination<BillRegister> page = new Pagination<>();

        if (billRegisterSearch.getOffset() != null)
            page.setOffset(billRegisterSearch.getOffset());

        if (billRegisterSearch.getPageSize() != null)
            page.setPageSize(billRegisterSearch.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition",
                    " where " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<BillRegister>) getPagination(searchQuery, page, paramValues);

        searchQuery = searchQuery + " :pagination";
        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset()
                        * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(BillRegisterEntity.class);

        final List<BillRegisterEntity> billRegisterEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
                paramValues,
                row);

        page.setTotalResults(billRegisterEntities.size());
        final List<BillRegister> billregisters = new ArrayList<>();

        for (final BillRegisterEntity billRegisterEntity : billRegisterEntities)
            billregisters.add(billRegisterEntity.toDomain());

        page.setPagedData(billregisters);
        return page;
    }

}