package org.egov.egf.bill.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillChecklistSearch;
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.model.BillDetailSearch;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.domain.model.BillStatus;
import org.egov.egf.bill.domain.model.Pagination;
import org.egov.egf.bill.domain.repository.BoundaryRepository;
import org.egov.egf.bill.domain.repository.ChecklistRepository;
import org.egov.egf.bill.domain.service.BillStatusService;
import org.egov.egf.bill.domain.service.DepartmentService;
import org.egov.egf.bill.domain.service.FunctionService;
import org.egov.egf.bill.domain.service.FunctionaryService;
import org.egov.egf.bill.domain.service.FundService;
import org.egov.egf.bill.domain.service.FundSourceService;
import org.egov.egf.bill.domain.service.SchemeService;
import org.egov.egf.bill.domain.service.SubSchemeService;
import org.egov.egf.bill.persistence.entity.BillRegisterEntity;
import org.egov.egf.bill.web.contract.Boundary;
import org.egov.egf.bill.web.contract.Department;
import org.egov.egf.bill.web.contract.Function;
import org.egov.egf.bill.web.contract.Functionary;
import org.egov.egf.bill.web.contract.Fund;
import org.egov.egf.bill.web.contract.Fundsource;
import org.egov.egf.bill.web.contract.Scheme;
import org.egov.egf.bill.web.contract.SubScheme;
import org.egov.egf.bill.web.repository.AccountDetailKeyRepository;
import org.egov.egf.bill.web.repository.AccountDetailTypeRepository;
import org.egov.egf.bill.web.repository.ChartOfAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class BillRegisterJdbcRepository extends JdbcRepository {

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private BillDetailJdbcRepository billDetailJdbcRepository;

    @Autowired
    private BillPayeeDetailJdbcRepository billPayeeDetailJdbcRepository;

    @Autowired
    private BillChecklistJdbcRepository billChecklistJdbcRepository;

    @Autowired
    private BoundaryRepository boundaryRepository;

    @Autowired
    private FunctionaryService functionaryService;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private ChartOfAccountRepository chartOfAccountRepository;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private AccountDetailTypeRepository accountDetailTypeRepository;

    @Autowired
    private AccountDetailKeyRepository accountDetailKeyRepository;

    @Autowired
    private FundSourceService fundSourceService;

    @Autowired
    private BillStatusService billStatusService;

    @Autowired
    private FundService fundService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SubSchemeService subSchemeService;

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

        if (billRegisterSearch.getBillFromDate() != null) {
            addAnd(params);
            params.append("billDate >= (:billFromDate)");
            paramValues.put("billFromDate", billRegisterSearch.getBillFromDate());
        }

        if (billRegisterSearch.getBillToDate() != null) {
            addAnd(params);
            params.append("billDate <= (:billToDate)");
            paramValues.put("billToDate", billRegisterSearch.getBillToDate());
        }

        if (billRegisterSearch.getBillNumber() != null) {
            addAnd(params);
            params.append("billNumber =:billNumber");
            paramValues.put("billNumber",
                    billRegisterSearch.getBillNumber());
        }

        if (billRegisterSearch.getBillNumbers() != null) {
            addAnd(params);
            params.append("billNumber in (:billNumbers)");
            paramValues.put("billNumbers",
                    new ArrayList<>(Arrays.asList(billRegisterSearch.getBillNumbers().split(","))));
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

        if (billRegisterSearch.getFundCode() != null) {
            addAnd(params);
            params.append("fund =:fund");
            paramValues.put("fund", billRegisterSearch.getFundCode());
        }

        if (billRegisterSearch.getFunctionCode() != null) {
            addAnd(params);
            params.append("function =:function");
            paramValues.put("function", billRegisterSearch.getFunctionCode());
        }

        if (billRegisterSearch.getFundSourceCode() != null) {
            addAnd(params);
            params.append("fundsource =:fundsource");
            paramValues.put("fundsource", billRegisterSearch.getFundSourceCode());
        }

        if (billRegisterSearch.getStatusCode() != null) {
            addAnd(params);
            params.append("status =:status");
            paramValues.put("status", billRegisterSearch.getStatusCode());
        }

        if (billRegisterSearch.getStatusCodes() != null) {
            addAnd(params);
            params.append("status in (:statusCodes)");
            paramValues.put("statusCodes", billRegisterSearch.getStatusCodes());
        }

        if (billRegisterSearch.getSchemeCode() != null) {
            addAnd(params);
            params.append("scheme =:scheme");
            paramValues.put("scheme", billRegisterSearch.getSchemeCode());
        }

        if (billRegisterSearch.getSubSchemeCode() != null) {
            addAnd(params);
            params.append("subscheme =:subScheme");
            paramValues.put("subScheme", billRegisterSearch.getSubSchemeCode());
        }

        if (billRegisterSearch.getFunctionaryCode() != null) {
            addAnd(params);
            params.append("functionary =:functionary");
            paramValues.put("functionary", billRegisterSearch.getFunctionaryCode());
        }

        if (billRegisterSearch.getLocationCode() != null) {
            addAnd(params);
            params.append("location =:location");
            paramValues.put("location", billRegisterSearch.getLocationCode());
        }

        if (billRegisterSearch.getDepartmentCode() != null) {
            addAnd(params);
            params.append("department =:department");
            paramValues.put("department", billRegisterSearch.getDepartmentCode());
        }

        if (billRegisterSearch.getDepartmentCodes() != null) {
            addAnd(params);
            params.append("department in (:departmentCodes)");
            paramValues.put("departmentCodes", billRegisterSearch.getDepartmentCodes());
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
        final List<BillRegister> billRegisterList = new ArrayList<>();
        StringBuffer billNumbers = new StringBuffer();

        for (final BillRegisterEntity billRegisterEntity : billRegisterEntities) {

            if (billNumbers.length() >= 1)
                billNumbers.append(",");

            billNumbers.append(billRegisterEntity.getBillNumber());

            billRegisterList.add(billRegisterEntity.toDomain());

        }

        if (billRegisterList != null && !billRegisterList.isEmpty()) {

            populateBillDetail(billRegisterList, billNumbers.toString());
            
            populateBillChecklist(billRegisterList, billNumbers.toString());

            populateBillStatuses(billRegisterList);

            populateDepartments(billRegisterList);

            populateFunctionarys(billRegisterList);

            populateFunctions(billRegisterList);

            populateFunds(billRegisterList);

            populateFundsources(billRegisterList);

            populateSchemes(billRegisterList);

            populateSubSchemes(billRegisterList);

            populateBoundarys(billRegisterList);

        }

        page.setPagedData(billRegisterList);
        return page;
    }

    private void populateBillDetail(List<BillRegister> billRegisterList, String billRegisterCodes) {
        Map<String, List<BillDetail>> billDetailMap = new HashMap<>();
        String tenantId = null;
        BillDetailSearch bds;
        bds = new BillDetailSearch();

        if (billRegisterList != null && !billRegisterList.isEmpty())
            tenantId = billRegisterList.get(0).getTenantId();

        bds.setBillNumbers(billRegisterCodes);
        bds.setTenantId(tenantId);

        List<BillDetail> billDetail = billDetailJdbcRepository.search(bds);

        for (BillDetail bd : billDetail) {

            if (billDetailMap.get(bd.getBill()) == null) {

                billDetailMap.put(bd.getBill(), Collections.singletonList(bd));

            } else {

                List<BillDetail> bdList = new ArrayList<>(billDetailMap.get(bd.getBill()));

                bdList.add(bd);

                billDetailMap.put(bd.getBill(), bdList);

            }
        }

        for (BillRegister billRegister : billRegisterList) {

            billRegister.setBillDetails(billDetailMap.get(billRegister.getBillNumber()));

        }

    }

    private void populateBillChecklist(List<BillRegister> billRegisterList, String billRegisterCodes) {
        Map<String, List<BillChecklist>> billChecklistMap = new HashMap<>();
        String tenantId = null;
        BillChecklistSearch bds;
        bds = new BillChecklistSearch();

        if (billRegisterList != null && !billRegisterList.isEmpty())
            tenantId = billRegisterList.get(0).getTenantId();

        bds.setBillNumbers(billRegisterCodes);
        bds.setTenantId(tenantId);

        List<BillChecklist> billChecklist = billChecklistJdbcRepository.search(bds);

        for (BillChecklist bd : billChecklist) {

            if (billChecklistMap.get(bd.getBill().getBillNumber()) == null) {

                billChecklistMap.put(bd.getBill().getBillNumber(), Collections.singletonList(bd));

            } else {

                List<BillChecklist> bdList = new ArrayList<>(billChecklistMap.get(bd.getBill().getBillNumber()));

                bdList.add(bd);

                billChecklistMap.put(bd.getBill().getBillNumber(), bdList);

            }
        }

        for (BillRegister billRegister : billRegisterList) {

           // billRegister.setBillChecklist(billChecklistMap.get(billRegister.getBillNumber()));

        }

    }

    private void populateFunds(List<BillRegister> billRegisterList) {
        Map<String, Fund> fundMap = new HashMap<>();
        String tenantId = null;

        if (billRegisterList != null && !billRegisterList.isEmpty())
            tenantId = billRegisterList.get(0).getTenantId();

        List<Fund> funds = fundService.getAll(tenantId, new RequestInfo());

        for (Fund ft : funds) {
            fundMap.put(ft.getCode(), ft);
        }

        for (BillRegister br : billRegisterList) {

            if (br.getFund() != null && br.getFund().getCode() != null
                    && !br.getFund().getCode().isEmpty()) {

                br.setFund(fundMap.get(br.getFund().getCode()));
            }

        }
    }

    private void populateFunctions(List<BillRegister> billRegisterList) {
        Map<String, Function> functionMap = new HashMap<>();
        String tenantId = null;

        if (billRegisterList != null && !billRegisterList.isEmpty())
            tenantId = billRegisterList.get(0).getTenantId();

        List<Function> functions = functionService.getAll(tenantId, new RequestInfo());

        for (Function ft : functions) {
            functionMap.put(ft.getCode(), ft);
        }

        for (BillRegister br : billRegisterList) {

            if (br.getFunction() != null && br.getFunction().getCode() != null
                    && !br.getFunction().getCode().isEmpty()) {

                br.setFunction(functionMap.get(br.getFunction().getCode()));
            }

        }
    }

    private void populateFunctionarys(List<BillRegister> billRegisterList) {
        Map<String, Functionary> functionaryMap = new HashMap<>();
        String tenantId = null;

        if (billRegisterList != null && !billRegisterList.isEmpty())
            tenantId = billRegisterList.get(0).getTenantId();

        List<Functionary> functionarys = functionaryService.getAll(tenantId, new RequestInfo());

        for (Functionary ft : functionarys) {
            functionaryMap.put(ft.getCode(), ft);
        }

        for (BillRegister br : billRegisterList) {

            if (br.getFunctionary() != null && br.getFunctionary().getCode() != null
                    && !br.getFunctionary().getCode().isEmpty()) {

                br.setFunctionary(functionaryMap.get(br.getFunctionary().getCode()));
            }

        }
    }

    private void populateFundsources(List<BillRegister> billRegisterList) {
        Map<String, Fundsource> fundsourceMap = new HashMap<>();
        String tenantId = null;

        if (billRegisterList != null && !billRegisterList.isEmpty())
            tenantId = billRegisterList.get(0).getTenantId();

        List<Fundsource> fundsources = fundSourceService.getAll(tenantId, new RequestInfo());

        for (Fundsource ft : fundsources) {
            fundsourceMap.put(ft.getCode(), ft);
        }

        for (BillRegister br : billRegisterList) {

            if (br.getFundsource() != null && br.getFundsource().getCode() != null
                    && !br.getFundsource().getCode().isEmpty()) {

                br.setFundsource(fundsourceMap.get(br.getFundsource().getCode()));
            }

        }
    }

    private void populateSchemes(List<BillRegister> billRegisterList) {
        Map<String, Scheme> schemeMap = new HashMap<>();
        String tenantId = null;

        if (billRegisterList != null && !billRegisterList.isEmpty())
            tenantId = billRegisterList.get(0).getTenantId();

        List<Scheme> schemes = schemeService.getAll(tenantId, new RequestInfo());

        for (Scheme ft : schemes) {
            schemeMap.put(ft.getCode(), ft);
        }

        for (BillRegister br : billRegisterList) {

            if (br.getScheme() != null && br.getScheme().getCode() != null
                    && !br.getScheme().getCode().isEmpty()) {

                br.setScheme(schemeMap.get(br.getScheme().getCode()));
            }

        }
    }

    private void populateSubSchemes(List<BillRegister> billRegisterList) {
        Map<String, SubScheme> subSchemeMap = new HashMap<>();
        String tenantId = null;

        if (billRegisterList != null && !billRegisterList.isEmpty())
            tenantId = billRegisterList.get(0).getTenantId();

        List<SubScheme> subSchemes = subSchemeService.getAll(tenantId, new RequestInfo());

        for (SubScheme ft : subSchemes) {
            subSchemeMap.put(ft.getCode(), ft);
        }

        for (BillRegister br : billRegisterList) {

            if (br.getSubScheme() != null && br.getSubScheme().getCode() != null
                    && !br.getSubScheme().getCode().isEmpty()) {

                br.setSubScheme(subSchemeMap.get(br.getSubScheme().getCode()));
            }

        }
    }

    private void populateBillStatuses(List<BillRegister> billRegisterList) {
        Map<String, BillStatus> billStatusMap = new HashMap<>();
        String tenantId = null;

        if (billRegisterList != null && !billRegisterList.isEmpty())
            tenantId = billRegisterList.get(0).getTenantId();

        List<BillStatus> billStatuss = billStatusService.getAll(tenantId, new RequestInfo());

        for (BillStatus ft : billStatuss) {
            billStatusMap.put(ft.getCode(), ft);
        }

        for (BillRegister br : billRegisterList) {

            if (br.getStatus() != null && br.getStatus().getCode() != null
                    && !br.getStatus().getCode().isEmpty()) {

                br.setStatus(billStatusMap.get(br.getStatus().getCode()));
            }

        }
    }

    private void populateDepartments(List<BillRegister> billRegisterList) {
        Map<String, Department> departmentMap = new HashMap<>();
        String tenantId = null;

        if (billRegisterList != null && !billRegisterList.isEmpty())
            tenantId = billRegisterList.get(0).getTenantId();

        List<Department> departments = departmentService.getAll(tenantId, new RequestInfo());

        for (Department ft : departments) {
            departmentMap.put(ft.getCode(), ft);
        }

        for (BillRegister br : billRegisterList) {

            if (br.getDepartment() != null && br.getDepartment().getCode() != null
                    && !br.getDepartment().getCode().isEmpty()) {

                br.setDepartment(departmentMap.get(br.getDepartment().getCode()));
            }

        }
    }

    private void populateBoundarys(List<BillRegister> billRegisterList) {

        StringBuffer boundaryCodes = new StringBuffer();
        Set<String> boundaryCodesSet = new HashSet<>();

        for (BillRegister br : billRegisterList) {

            if (br.getLocation() != null && br.getLocation().getCode() != null
                    && !br.getLocation().getCode().isEmpty()) {

                boundaryCodesSet.add(br.getLocation().getCode());

            }

        }

        List<String> locationCodes = new ArrayList(boundaryCodesSet);

        for (String code : locationCodes) {

            if (boundaryCodes.length() >= 1)
                boundaryCodes.append(",");

            boundaryCodes.append(code);

        }

        String tenantId = null;
        Map<String, Boundary> boundaryMap = new HashMap<>();

        if (billRegisterList != null && !billRegisterList.isEmpty())
            tenantId = billRegisterList.get(0).getTenantId();

        if (boundaryCodes != null && boundaryCodes.length() > 0) {

            List<Boundary> boundarys = boundaryRepository.fetchBoundaryByCodes(boundaryCodes.toString(), tenantId);

            for (Boundary bd : boundarys) {

                boundaryMap.put(bd.getCode(), bd);

            }

            for (BillRegister br : billRegisterList) {

                if (br.getLocation() != null && br.getLocation().getCode() != null
                        && !br.getLocation().getCode().isEmpty()) {

                    br.setLocation(boundaryMap.get(br.getLocation().getCode()));
                }

            }

        }

    }

}