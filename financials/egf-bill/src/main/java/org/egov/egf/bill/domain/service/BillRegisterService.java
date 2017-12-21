package org.egov.egf.bill.domain.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.AuditDetails;
import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.model.ChecklistSearch;
import org.egov.egf.bill.domain.repository.BillRegisterRepository;
import org.egov.egf.bill.domain.repository.BoundaryRepository;
import org.egov.egf.bill.domain.repository.ChecklistRepository;
import org.egov.egf.bill.domain.repository.DepartmentRepository;
import org.egov.egf.bill.web.contract.Boundary;
import org.egov.egf.bill.web.contract.Department;
import org.egov.egf.bill.web.requests.BillRegisterRequest;
import org.egov.egf.master.web.contract.AccountDetailKeyContract;
import org.egov.egf.master.web.contract.AccountDetailTypeContract;
import org.egov.egf.master.web.contract.ChartOfAccountContract;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.FundsourceContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;
import org.egov.egf.master.web.repository.AccountDetailKeyContractRepository;
import org.egov.egf.master.web.repository.AccountDetailTypeContractRepository;
import org.egov.egf.master.web.repository.ChartOfAccountContractRepository;
import org.egov.egf.master.web.repository.FinancialStatusContractRepository;
import org.egov.egf.master.web.repository.FunctionContractRepository;
import org.egov.egf.master.web.repository.FunctionaryContractRepository;
import org.egov.egf.master.web.repository.FundContractRepository;
import org.egov.egf.master.web.repository.FundsourceContractRepository;
import org.egov.egf.master.web.repository.SchemeContractRepository;
import org.egov.egf.master.web.repository.SubSchemeContractRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BillRegisterService {
    @Autowired
    private BillRegisterRepository billRegisterRepository;

    @Autowired
    private SchemeContractRepository schemeContractRepository;

    @Autowired
    private BoundaryRepository boundaryRepository;

    @Autowired
    private FunctionaryContractRepository functionaryContractRepository;

    @Autowired
    private FunctionContractRepository functionContractRepository;

    @Autowired
    private ChartOfAccountContractRepository chartOfAccountContractRepository;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private AccountDetailTypeContractRepository accountDetailTypeContractRepository;

    @Autowired
    private AccountDetailKeyContractRepository accountDetailKeyContractRepository;

    @Autowired
    private FundsourceContractRepository fundsourceContractRepository;

    @Autowired
    private FinancialStatusContractRepository financialStatusContractRepository;

    @Autowired
    private FundContractRepository fundContractRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SubSchemeContractRepository subSchemeContractRepository;

    @Transactional
    public BillRegisterRequest create(BillRegisterRequest billRegisterRequest) {

        fetchRelated(billRegisterRequest.getBillRegisters(), billRegisterRequest.getRequestInfo());
        validate(billRegisterRequest);
        populateAuditDetails(billRegisterRequest);
        populateBillRegisterIds(billRegisterRequest.getBillRegisters());
        populateDependentEntityIds(billRegisterRequest.getBillRegisters());

        return billRegisterRepository.save(billRegisterRequest);
    }

    @Transactional
    public BillRegisterRequest update(BillRegisterRequest billRegisterRequest) {

        fetchRelated(billRegisterRequest.getBillRegisters(), billRegisterRequest.getRequestInfo());
        populateAuditDetails(billRegisterRequest);
        populateDependentEntityIds(billRegisterRequest.getBillRegisters());
        validate(billRegisterRequest);

        return billRegisterRepository.update(billRegisterRequest);
    }

    private void populateAuditDetails(BillRegisterRequest billRegisterRequest) {
        Long userId = null;

        if (billRegisterRequest.getRequestInfo() != null
                && billRegisterRequest.getRequestInfo().getUserInfo() != null
                && null != billRegisterRequest.getRequestInfo().getUserInfo().getId())
            userId = billRegisterRequest.getRequestInfo().getUserInfo().getId();

        for (final BillRegister billregister : billRegisterRequest.getBillRegisters()) {
            setAuditDetails(billregister, userId);
            if (null != billregister.getBillDetails())
                for (final BillDetail billDetail : billregister.getBillDetails()) {
                    setAuditDetails(billDetail, userId);
                    if (null != billDetail.getBillPayeeDetails())
                        for (final BillPayeeDetail billPayeeDetail : billDetail.getBillPayeeDetails()) {
                            setAuditDetails(billPayeeDetail, userId);
                        }
                    else
                        billDetail.setBillPayeeDetails(Collections.emptyList());
                }
            else
                billregister.setBillDetails(Collections.emptyList());

            if (null != billregister.getCheckLists())
                for (final BillChecklist checklist : billregister.getCheckLists()) {
                    setAuditDetails(checklist, userId);
                }
            else
                billregister.setCheckLists(Collections.emptyList());
        }
    }

    private void validate(BillRegisterRequest billRegisterRequest) {

        for (BillRegister br : billRegisterRequest.getBillRegisters()) {
            if (br.getPassedAmount().compareTo(br.getBillAmount()) > 0)
                throw new CustomException("passedAmount",
                        "passedAmount must be less than billAmount");
        }

    }

    public List<BillRegister> fetchRelated(final List<BillRegister> billregisters, final RequestInfo requestInfo) {
        if (null != billregisters)
            for (final BillRegister billRegister : billregisters) {
                // fetch related items
                if (billRegister.getStatus() != null) {
                    billRegister.getStatus().setTenantId(billRegister.getTenantId());
                    final FinancialStatusContract status = financialStatusContractRepository
                            .findById(billRegister.getStatus(), requestInfo);
                    if (status == null || status.getCode() == null || status.getCode().isEmpty())
                        throw new CustomException("status",
                                "Given status is Invalid: " + status.getCode());
                    billRegister.setStatus(status);
                }
                if (billRegister.getFund() != null) {
                    billRegister.getFund().setTenantId(billRegister.getTenantId());
                    final FundContract fund = fundContractRepository
                            .findById(billRegister.getFund(), requestInfo);
                    if (fund == null || fund.getCode() == null || fund.getCode().isEmpty())
                        throw new CustomException("fund",
                                "Given fund is Invalid: " + fund.getCode());
                    billRegister.setFund(fund);
                }
                if (billRegister.getFunction() != null) {
                    billRegister.getFunction().setTenantId(billRegister.getTenantId());
                    final FunctionContract function = functionContractRepository
                            .findById(billRegister.getFunction(), requestInfo);
                    if (function == null || function.getCode() == null || function.getCode().isEmpty())
                        throw new CustomException("function",
                                "Given function is Invalid: " + function.getCode());
                    billRegister.setFunction(function);
                }
                if (billRegister.getFundsource() != null) {
                    billRegister.getFundsource().setTenantId(billRegister.getTenantId());
                    final FundsourceContract fundsource = fundsourceContractRepository
                            .findById(billRegister.getFundsource(), requestInfo);
                    if (fundsource == null || fundsource.getCode() == null || fundsource.getCode().isEmpty())
                        throw new CustomException("fundsource",
                                "Given fundsource is Invalid: " + fundsource.getCode());
                    billRegister.setFundsource(fundsource);
                }
                if (billRegister.getScheme() != null) {
                    billRegister.getScheme().setTenantId(billRegister.getTenantId());
                    final SchemeContract scheme = schemeContractRepository
                            .findById(billRegister.getScheme(), requestInfo);
                    if (scheme == null || scheme.getCode() == null || scheme.getCode().isEmpty())
                        throw new CustomException("scheme",
                                "Given scheme is Invalid: " + scheme.getCode());
                    billRegister.setScheme(scheme);
                }
                if (billRegister.getSubScheme() != null) {
                    billRegister.getSubScheme().setTenantId(billRegister.getTenantId());
                    final SubSchemeContract subScheme = subSchemeContractRepository
                            .findById(billRegister.getSubScheme(), requestInfo);
                    if (subScheme == null || subScheme.getCode() == null || subScheme.getCode().isEmpty())
                        throw new CustomException("subScheme",
                                "Given subScheme is Invalid: " + subScheme.getCode());
                    billRegister.setSubScheme(subScheme);
                }
                if (billRegister.getFunctionary() != null) {
                    billRegister.getFunctionary().setTenantId(billRegister.getTenantId());
                    final FunctionaryContract functionary = functionaryContractRepository
                            .findById(billRegister.getFunctionary(), requestInfo);
                    if (functionary == null || functionary.getCode() == null || functionary.getCode().isEmpty())
                        throw new CustomException("functionary",
                                "Given functionary is Invalid: " + functionary.getCode());
                    billRegister.setFunctionary(functionary);
                }
                if (billRegister.getDivision() != null) {
                    billRegister.getDivision().setTenantId(billRegister.getTenantId());
                    final Boundary division = boundaryRepository
                            .findById(billRegister.getDivision());
                    if (division == null || division.getId() == null || division.getId().isEmpty())
                        throw new CustomException("division",
                                "Given division is Invalid: " + division.getId());
                    billRegister.setDivision(division);
                }
                if (billRegister.getDepartment() != null) {
                    billRegister.getDepartment().setTenantId(billRegister.getTenantId());
                    final Department department = departmentRepository
                            .findById(billRegister.getDepartment());
                    if (department == null || department.getId() == null || department.getId().isEmpty())
                        throw new CustomException("department",
                                "Given department is Invalid: " + department.getId());
                    billRegister.setDepartment(department);
                }
                fetchRelatedForBillDetail(billRegister, requestInfo);
                fetchRelatedForBillPayeeDetail(billRegister, requestInfo);
                fetchRelatedForChecklist(billRegister);
            }
        return billregisters;
    }

    private void fetchRelatedForBillDetail(final BillRegister billRegister, final RequestInfo requestInfo) {

        final Map<String, ChartOfAccountContract> coaMap = new HashMap<>();
        final Map<String, FunctionContract> functionMap = new HashMap<>();
        final String tenantId = billRegister.getTenantId();

        for (final BillDetail billDetail : billRegister.getBillDetails()) {
            if (billDetail.getGlcode() != null) {
                ChartOfAccountContract coa = null;
                if (coaMap.get(billDetail.getGlcode()) == null) {
                    final ChartOfAccountContract coaContract = new ChartOfAccountContract();
                    coaContract.setGlcode(billDetail.getGlcode());
                    coaContract.setTenantId(tenantId);
                    coa = chartOfAccountContractRepository.findByGlcode(coaContract, requestInfo);
                    if (coa == null || coa.getId() == null || coa.getId().isEmpty())
                        throw new CustomException("glCode",
                                "Given glCode is Invalid: " + coa.getId());
                    coaMap.put(billDetail.getGlcode(), coa);

                }
                billDetail.setChartOfAccount(coaMap.get(billDetail.getGlcode()));
            }

            if (billDetail.getFunction() != null) {
                if (functionMap.get(billDetail.getFunction().getId()) == null) {
                    billDetail.getFunction().setTenantId(tenantId);
                    final FunctionContract function = functionContractRepository.findById(billDetail.getFunction(), requestInfo);
                    if (function == null || function.getId() == null || function.getId().isEmpty())
                        throw new CustomException("function",
                                "Given function is Invalid: " + function.getId());
                    functionMap.put(billDetail.getFunction().getId(), function);
                }

                billDetail.setFunction(functionMap.get(billDetail.getFunction().getId()));
            }
        }

    }

    private void fetchRelatedForBillPayeeDetail(final BillRegister billRegister, final RequestInfo requestInfo) {

        final Map<String, AccountDetailTypeContract> adtMap = new HashMap<>();
        final Map<String, AccountDetailKeyContract> adkMap = new HashMap<>();
        final String tenantId = billRegister.getTenantId();

        for (final BillDetail billDetail : billRegister.getBillDetails())
            if (billDetail.getBillPayeeDetails() != null)

                for (final BillPayeeDetail detail : billDetail.getBillPayeeDetails()) {

                    if (detail.getAccountDetailType() != null) {

                        if (adtMap.get(detail.getAccountDetailType().getId()) == null) {
                            detail.getAccountDetailType().setTenantId(tenantId);
                            final AccountDetailTypeContract accountDetailType = accountDetailTypeContractRepository
                                    .findById(detail.getAccountDetailType(), requestInfo);

                            if (accountDetailType == null || accountDetailType.getId() == null
                                    || accountDetailType.getId().isEmpty())
                                throw new CustomException("accountDetailType",
                                        "Given accountDetailType is Invalid: " + detail.getAccountDetailType());

                            adtMap.put(detail.getAccountDetailType().getId(), accountDetailType);
                        }
                        detail.setAccountDetailType(adtMap.get(detail.getAccountDetailType().getId()));
                    }

                    if (detail.getAccountDetailKey() != null) {

                        if (adkMap.get(detail.getAccountDetailKey().getId()) == null) {
                            detail.getAccountDetailKey().setTenantId(tenantId);
                            final AccountDetailKeyContract accountDetailKey = accountDetailKeyContractRepository
                                    .findById(detail.getAccountDetailKey(), requestInfo);

                            if (accountDetailKey == null || accountDetailKey.getId() == null
                                    || accountDetailKey.getId().isEmpty())
                                throw new CustomException("accountDetailType",
                                        "Given accountDetailType is Invalid: " + detail.getAccountDetailKey());

                            adkMap.put(detail.getAccountDetailKey().getId(), accountDetailKey);
                        }
                        detail.setAccountDetailKey(adkMap.get(detail.getAccountDetailKey().getId()));
                    }
                }
    }

    private void fetchRelatedForChecklist(final BillRegister billRegister) {

        final Map<String, Checklist> checklistMap = new HashMap<>();
        final String tenantId = billRegister.getTenantId();
        for (final BillChecklist billChecklist : billRegister.getCheckLists())
            if (billChecklist.getChecklist() != null) {
                if (checklistMap.get(billChecklist.getChecklist().getCode()) == null) {
                    billChecklist.getChecklist().setTenantId(tenantId);
                    ChecklistSearch search = new ChecklistSearch();
                    search.setTenantId(tenantId);
                    search.setCode(billChecklist.getChecklist().getCode());
                    Pagination<Checklist> checkListResponse = checklistRepository.search(search);

                    if (checkListResponse == null || checkListResponse.getPagedData() == null
                            || !checkListResponse.getPagedData().isEmpty())
                        throw new CustomException("checklist",
                                "Given checklist is Invalid: " + billChecklist.getChecklist().getCode());
                    checklistMap.put(billChecklist.getChecklist().getCode(), checkListResponse.getPagedData().get(0));
                }

                billChecklist.setChecklist(checklistMap.get(billChecklist.getChecklist().getCode()));
            }
    }

    public Pagination<BillRegister> search(final BillRegisterSearch billRegisterSearch) {
        return billRegisterRepository.search(billRegisterSearch);
    }

    private void populateBillRegisterIds(final List<BillRegister> billregisters) {

        for (final BillRegister billregister : billregisters) {
            billregister.setBillNumber(UUID.randomUUID().toString().replace("-", ""));
            if (null != billregister.getBillDetails())
                for (final BillDetail billDetail : billregister.getBillDetails()) {
                    billDetail.setId(UUID.randomUUID().toString().replace("-", ""));
                    billDetail.setTenantId(billregister.getTenantId());
                    if (null != billDetail.getBillPayeeDetails())
                        for (final BillPayeeDetail billPayeeDetail : billDetail.getBillPayeeDetails()) {
                            billPayeeDetail.setId(UUID.randomUUID().toString().replace("-", ""));
                            billPayeeDetail.setTenantId(billregister.getTenantId());
                        }
                }

            if (null != billregister.getCheckLists())
                for (final BillChecklist checklist : billregister.getCheckLists()) {
                    checklist.setId(UUID.randomUUID().toString().replace("-", ""));
                    checklist.setTenantId(billregister.getTenantId());
                }
        }

    }

    private void populateDependentEntityIds(final List<BillRegister> billregisters) {

        for (final BillRegister billregister : billregisters) {
            if (null != billregister.getBillDetails())
                for (final BillDetail billDetail : billregister.getBillDetails()) {
                    billDetail.setId(UUID.randomUUID().toString().replace("-", ""));
                    billDetail.setTenantId(billregister.getTenantId());
                    if (null != billDetail.getBillPayeeDetails())
                        for (final BillPayeeDetail billPayeeDetail : billDetail.getBillPayeeDetails()) {
                            billPayeeDetail.setId(UUID.randomUUID().toString().replace("-", ""));
                            billPayeeDetail.setTenantId(billregister.getTenantId());
                        }
                }

            if (null != billregister.getCheckLists())
                for (final BillChecklist checklist : billregister.getCheckLists()) {
                    checklist.setId(UUID.randomUUID().toString().replace("-", ""));
                    checklist.setTenantId(billregister.getTenantId());
                }
        }

    }

    private void setAuditDetails(final BillRegister contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getBillNumber() || contract.getBillNumber().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }

    private void setAuditDetails(final BillDetail contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getId() || contract.getId().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }

    private void setAuditDetails(final BillPayeeDetail contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getId() || contract.getId().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }

    private void setAuditDetails(final BillChecklist contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getId() || contract.getId().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }

}
