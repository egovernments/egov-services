package org.egov.egf.bill.domain.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.constants.Constants;
import org.egov.egf.bill.domain.model.AuditDetails;
import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.model.ChecklistSearch;
import org.egov.egf.bill.domain.model.Pagination;
import org.egov.egf.bill.domain.repository.BillRegisterRepository;
import org.egov.egf.bill.domain.repository.BoundaryRepository;
import org.egov.egf.bill.domain.repository.ChecklistRepository;
import org.egov.egf.bill.web.contract.AccountDetailKey;
import org.egov.egf.bill.web.contract.AccountDetailType;
import org.egov.egf.bill.web.contract.Boundary;
import org.egov.egf.bill.web.contract.ChartOfAccount;
import org.egov.egf.bill.web.contract.ChartOfAccountDetail;
import org.egov.egf.bill.web.contract.FinancialConfiguration;
import org.egov.egf.bill.web.contract.FinancialConfigurationValue;
import org.egov.egf.bill.web.contract.Function;
import org.egov.egf.bill.web.repository.AccountDetailKeyRepository;
import org.egov.egf.bill.web.repository.AccountDetailTypeRepository;
import org.egov.egf.bill.web.repository.ChartOfAccountRepository;
import org.egov.egf.bill.web.repository.FinancialConfigurationRepository;
import org.egov.egf.bill.web.repository.IdgenRepository;
import org.egov.egf.bill.web.requests.BillRegisterRequest;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BillRegisterService {

    private static final Logger LOG = LoggerFactory.getLogger(BillRegisterService.class);

    @Value("${egf.bill.default.number.format.name}")
    private String idGenFormatNameForBillPath;

    @Value("${egf.bill.number.auto.gen}")
    private Boolean billNumberAutoGen;

    @Autowired
    private BillRegisterRepository billRegisterRepository;

    @Autowired
    private SchemeService schemeService;

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

    @Autowired
    private IdgenRepository idgenRepository;

    @Autowired
    private FinancialConfigurationRepository financialConfigurationRepository;

    @Transactional
    public BillRegisterRequest create(BillRegisterRequest billRegisterRequest) {

        fetchRelated(billRegisterRequest);
        populateAuditDetails(billRegisterRequest);

        if (billNumberAutoGen != null && billNumberAutoGen)
            populateBillNumbers(billRegisterRequest);

        populateDependentEntityIds(billRegisterRequest.getBillRegisters());

        validate(billRegisterRequest, Constants.ACTION_CREATE);

        return billRegisterRepository.save(billRegisterRequest);
    }

    @Transactional
    public BillRegisterRequest update(BillRegisterRequest billRegisterRequest) {

        fetchRelated(billRegisterRequest);
        populateAuditDetails(billRegisterRequest);
        populateDependentEntityIds(billRegisterRequest.getBillRegisters());
        validate(billRegisterRequest, Constants.ACTION_UPDATE);

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

    private void validate(BillRegisterRequest billRegisterRequest, String action) {

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        findDuplicateAccountCodes(billRegisterRequest);

        String tenantId = null;

        if (billRegisterRequest.getBillRegisters() != null && !billRegisterRequest.getBillRegisters().isEmpty()) {
            tenantId = billRegisterRequest.getBillRegisters().get(0).getTenantId();
        }

        for (BillRegister br : billRegisterRequest.getBillRegisters()) {
            if (action != null && action.equalsIgnoreCase(Constants.ACTION_CREATE)) {
                if (billNumberAutoGen != null && !billNumberAutoGen
                        && (br.getBillNumber() == null || br.getBillNumber().isEmpty()))
                    throw new CustomException("billNumber",
                            "The field BillNumber is Mandatory . It cannot be not be null or empty.Please provide correct value ");
            } else if (action != null && action.equalsIgnoreCase(Constants.ACTION_UPDATE)) {
                if (br.getBillNumber() == null || br.getBillNumber().isEmpty())
                    throw new CustomException("billNumber",
                            "The field BillNumber is Mandatory . It cannot be not be null or empty.Please provide correct value ");
            }

            if (br.getPassedAmount().compareTo(br.getBillAmount()) > 0)
                throw new CustomException("passedAmount", "passedAmount must be less than billAmount");

        }
        BigDecimal creditSum = BigDecimal.ZERO;
        BigDecimal debitSum = BigDecimal.ZERO;
        BigDecimal billDetailAmt = BigDecimal.ZERO;
        BigDecimal payeeDetailSum = BigDecimal.ZERO;
        Boolean check = false;

        for (BillRegister br : billRegisterRequest.getBillRegisters()) {

            if (br.getBillDate() != null && new Date().before(new Date(br.getBillDate())))
                throw new CustomException("BillDate ",
                        "Given Bill Date is invalid: " + dateFormat.format(new Date(br.getBillDate())));

            creditSum = BigDecimal.ZERO;
            debitSum = BigDecimal.ZERO;

            for (BillDetail bd : br.getBillDetails()) {

                payeeDetailSum = BigDecimal.ZERO;
                billDetailAmt = BigDecimal.ZERO;

                // validate coa is active for posting
                if (bd.getChartOfAccount() != null && bd.getChartOfAccount().getIsActiveForPosting() != null
                        && !bd.getChartOfAccount().getIsActiveForPosting()) {
                    throw new CustomException("AccountCode",
                            "The field Account code is not active for posting.Please provide correct account code: "
                                    + bd.getChartOfAccount().getGlcode());
                }

                // validate coa is of classification 4
                if (bd.getChartOfAccount() != null && bd.getChartOfAccount().getClassification() != null
                        && bd.getChartOfAccount().getClassification() != 4) {
                    throw new CustomException("AccountCode",
                            "The field Account code is not valid detail code.Please provide correct account code: "
                                    + bd.getChartOfAccount().getGlcode());
                }

                // validate both debit and credit can not have non zero value
                if (bd.getDebitAmount() != null && bd.getCreditAmount() != null
                        && bd.getDebitAmount().compareTo(BigDecimal.ZERO) > 0
                        && bd.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {

                    throw new CustomException("DebitAmount",
                            "Positive amount is not allowed in both debit and credit amount of "
                                    + bd.getChartOfAccount().getGlcode() + ",Any time only one can be greater than 0");

                }

                // validate both debit and credit can not have zero value
                if (bd.getDebitAmount() != null && bd.getCreditAmount() != null
                        && bd.getDebitAmount().compareTo(BigDecimal.ZERO) == 0
                        && bd.getCreditAmount().compareTo(BigDecimal.ZERO) == 0) {

                    throw new CustomException("DebitAmount", "Zero amount is not allowed in both debit and credit amount of "
                            + bd.getChartOfAccount().getGlcode() + ",Any time only one can be greater than 0 ");

                }

                // validate subledger data if account code is control code
                if (bd.getChartOfAccount() != null && bd.getChartOfAccount().getIsSubLedger() != null
                        && bd.getChartOfAccount().getIsSubLedger()
                        && (bd.getBillPayeeDetails() == null || bd.getBillPayeeDetails().isEmpty())) {

                    throw new CustomException("BillDetail",
                            "Subledger(Payee details) details are missing for the control account code "
                                    + bd.getChartOfAccount().getGlcode());
                }

                // validate subledger data if account code is non control code
                if (bd.getChartOfAccount() != null && bd.getChartOfAccount().getIsSubLedger() != null
                        && !bd.getChartOfAccount().getIsSubLedger()
                        && bd.getBillPayeeDetails() != null && !bd.getBillPayeeDetails().isEmpty()) {

                    throw new CustomException("BillDetail",
                            "Invalid Subledger(Payee details) details for the non control account code "
                                    + bd.getChartOfAccount().getGlcode());
                }

                for (BillPayeeDetail bpd : bd.getBillPayeeDetails()) {
                    payeeDetailSum = payeeDetailSum.add(bpd.getAmount());

                    // validate passed subledger data is correct or not
                    if (bd.getChartOfAccount() != null && bd.getChartOfAccount().getChartOfAccountDetails() != null
                            && !bd.getChartOfAccount().getChartOfAccountDetails().isEmpty() && bd.getBillPayeeDetails() != null
                            && !bd.getBillPayeeDetails().isEmpty()) {

                        check = false;

                        for (ChartOfAccountDetail coad : bd.getChartOfAccount().getChartOfAccountDetails()) {
                            if (coad.getAccountDetailType() != null && coad.getAccountDetailType().getId() != null
                                    && bpd.getAccountDetailType() != null && bpd.getAccountDetailType().getId() != null
                                    && coad.getAccountDetailType().getId().equalsIgnoreCase(bpd.getAccountDetailType().getId()))
                                check = true;
                        }
                        if (!check)
                            throw new CustomException("AccountDetailType",
                                    "Invalid Account detail type " + bpd.getAccountDetailType().getId()
                                            + ", for the non control account code "
                                            + bd.getChartOfAccount().getGlcode());
                    }
                }

                billDetailAmt = bd.getDebitAmount().compareTo(BigDecimal.ZERO) > 0 ? bd.getDebitAmount()
                        : bd.getCreditAmount();

                // validate subledger sum and ledger amount
                if (billDetailAmt.compareTo(payeeDetailSum) != 0) {

                    throw new CustomException("BillDetail",
                            "Subledger(Payee details) total amount is not matching the control account code: "
                                    + bd.getChartOfAccount().getGlcode());
                }

                debitSum = debitSum.add(bd.getDebitAmount());
                creditSum = creditSum.add(bd.getCreditAmount());
            }

            // validate sum(debit)-sum(credit) should be 0
            if (debitSum.compareTo(creditSum) != 0) {
                throw new CustomException("Bill",
                        "Ledger total amount is not matching. Sum of debit should be equal to sum of credit");
            }
        }

        List<String> mandatoryFields = getHeaderMandateFields(tenantId, billRegisterRequest.getRequestInfo());
        validateMandatoryFields(mandatoryFields, billRegisterRequest.getBillRegisters());

    }

    private void findDuplicateAccountCodes(BillRegisterRequest billRegisterRequest) {

        Map<String, List<String>> coaFunCodeMap = new HashMap<>();
        List<String> functionCodes = new ArrayList<>();

        for (BillRegister br : billRegisterRequest.getBillRegisters()) {

            coaFunCodeMap = new HashMap<>();

            for (BillDetail bd : br.getBillDetails()) {

                if (bd.getChartOfAccount() != null && bd.getChartOfAccount().getGlcode() != null && bd.getFunction() != null
                        && bd.getFunction().getCode() != null) {

                    if (coaFunCodeMap.get(bd.getChartOfAccount().getGlcode()) != null) {
                        functionCodes = coaFunCodeMap.get(bd.getChartOfAccount().getGlcode());

                        if (functionCodes != null && functionCodes.contains(bd.getFunction().getCode())) {
                            throw new CustomException("BillDetails",
                                    "Duplicate account code and function in given bill: " + bd.getChartOfAccount().getGlcode());
                        } else if (functionCodes == null || functionCodes.isEmpty()) {
                            throw new CustomException("BillDetails",
                                    "Duplicate account code and function in given bill: " + bd.getChartOfAccount().getGlcode());
                        } else {
                            functionCodes.add(bd.getFunction().getCode());
                            coaFunCodeMap.put((bd.getChartOfAccount().getGlcode()), functionCodes);
                        }
                    }

                    coaFunCodeMap.put((bd.getChartOfAccount().getGlcode()),
                            new ArrayList<>(Arrays.asList(bd.getFunction().getCode())));

                } else if (bd.getChartOfAccount() != null && bd.getChartOfAccount().getGlcode() != null
                        && (bd.getFunction() == null || bd.getFunction().getCode() == null
                                || bd.getFunction().getCode().isEmpty())) {

                    if (coaFunCodeMap.get(bd.getChartOfAccount().getGlcode()) != null)
                        throw new CustomException("BillDetails",
                                "Duplicate account code in given bill: " + bd.getChartOfAccount().getGlcode());

                    coaFunCodeMap.put((bd.getChartOfAccount().getGlcode()), new ArrayList<>());
                }
            }
        }

    }

    public void fetchRelated(final BillRegisterRequest billRegisterRequest) {
        if (null != billRegisterRequest.getBillRegisters())
            for (final BillRegister billRegister : billRegisterRequest.getBillRegisters()) {

                // Validate Bill Status

                if (billRegister.getStatus() != null && (billRegister.getStatus().getCode() == null
                        || billRegister.getStatus().getCode().isEmpty()))
                    throw new CustomException("BillStatus",
                            "The field BillStatus Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

                if (billRegister.getStatus() != null && billRegister.getStatus().getCode() != null)
                    billRegister.setStatus(billStatusService.getBillStatus(billRegister.getTenantId(),
                            billRegister.getStatus().getCode(), billRegisterRequest.getRequestInfo()));

                // Validate Fund

                if (billRegister.getFund() != null && (billRegister.getFund().getCode() == null
                        || billRegister.getFund().getCode().isEmpty()))
                    throw new CustomException("Fund",
                            "The field Fund Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

                if (billRegister.getFund() != null && billRegister.getFund().getCode() != null)
                    billRegister.setFund(fundService.getFund(billRegister.getTenantId(),
                            billRegister.getFund().getCode(), billRegisterRequest.getRequestInfo()));

                // Validate Function

                if (billRegister.getFunction() != null && (billRegister.getFunction().getCode() == null
                        || billRegister.getFunction().getCode().isEmpty()))
                    throw new CustomException("Function",
                            "The field Function Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

                if (billRegister.getFunction() != null && billRegister.getFunction().getCode() != null)
                    billRegister.setFunction(functionService.getFunction(billRegister.getTenantId(),
                            billRegister.getFunction().getCode(), billRegisterRequest.getRequestInfo()));

                // Validate Functionary

                if (billRegister.getFunctionary() != null && (billRegister.getFunctionary().getCode() == null
                        || billRegister.getFunctionary().getCode().isEmpty()))
                    throw new CustomException("Functionary",
                            "The field Functionary Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

                if (billRegister.getFunctionary() != null && billRegister.getFunctionary().getCode() != null)
                    billRegister.setFunctionary(functionaryService.getFunctionary(billRegister.getTenantId(),
                            billRegister.getFunctionary().getCode(), billRegisterRequest.getRequestInfo()));

                // Validate FundSource

                if (billRegister.getFundsource() != null && (billRegister.getFundsource().getCode() == null
                        || billRegister.getFundsource().getCode().isEmpty()))
                    throw new CustomException("FundSource",
                            "The field FundSource Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

                if (billRegister.getFundsource() != null && billRegister.getFundsource().getCode() != null)
                    billRegister.setFundsource(fundSourceService.getFundSource(billRegister.getTenantId(),
                            billRegister.getFundsource().getCode(), billRegisterRequest.getRequestInfo()));

                // Validate Scheme

                if (billRegister.getScheme() != null && (billRegister.getScheme().getCode() == null
                        || billRegister.getScheme().getCode().isEmpty()))
                    throw new CustomException("Scheme",
                            "The field Scheme Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

                if (billRegister.getScheme() != null && billRegister.getScheme().getCode() != null)
                    billRegister.setScheme(schemeService.getScheme(billRegister.getTenantId(),
                            billRegister.getScheme().getCode(), billRegisterRequest.getRequestInfo()));

                // Validate SubScheme

                if (billRegister.getSubScheme() != null && (billRegister.getSubScheme().getCode() == null
                        || billRegister.getSubScheme().getCode().isEmpty()))
                    throw new CustomException("SubScheme",
                            "The field SubScheme Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

                if (billRegister.getSubScheme() != null && billRegister.getSubScheme().getCode() != null)
                    billRegister.setSubScheme(subSchemeService.getSubScheme(billRegister.getTenantId(),
                            billRegister.getSubScheme().getCode(), billRegisterRequest.getRequestInfo()));

                // Validate Department

                if (billRegister.getDepartment() != null && (billRegister.getDepartment().getCode() == null
                        || billRegister.getDepartment().getCode().isEmpty()))
                    throw new CustomException("Department",
                            "The field Department Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

                if (billRegister.getDepartment() != null && billRegister.getDepartment().getCode() != null)
                    billRegister.setDepartment(departmentService.getDepartment(billRegister.getTenantId(),
                            billRegister.getDepartment().getCode(), billRegisterRequest.getRequestInfo()));

                if (billRegister.getLocation() != null && billRegister.getLocation().getCode() != null) {
                    final Boundary location = boundaryRepository.fetchBoundaryByCode(billRegister.getLocation().getCode(),
                            billRegister.getTenantId());
                    if (location == null ||
                            location.getId() == null || location.getId().isEmpty())
                        throw new CustomException("location",
                                "Given location is Invalid: " + location.getId());
                    billRegister.setLocation(location);
                }

                fetchRelatedForBillDetail(billRegister, billRegisterRequest.getRequestInfo());
                fetchRelatedForBillPayeeDetail(billRegister, billRegisterRequest.getRequestInfo());
                fetchRelatedForChecklist(billRegister);
            }
    }

    private void fetchRelatedForBillDetail(final BillRegister billRegister, final RequestInfo requestInfo) {

        final Map<String, ChartOfAccount> coaMap = new HashMap<>();
        final Map<String, Function> functionMap = new HashMap<>();
        final String tenantId = billRegister.getTenantId();
        if (billRegister.getBillDetails() != null)
            for (final BillDetail billDetail : billRegister.getBillDetails()) {

                if (billDetail.getChartOfAccount() != null && billDetail.getChartOfAccount().getGlcode() != null) {
                    ChartOfAccount coa = null;
                    if (coaMap.get(billDetail.getChartOfAccount().getGlcode()) == null) {
                        final ChartOfAccount coaContract = new ChartOfAccount();
                        coaContract.setGlcode(billDetail.getChartOfAccount().getGlcode());
                        coaContract.setTenantId(tenantId);
                        coa = chartOfAccountRepository.findByGlcode(coaContract,
                                requestInfo);
                        if (coa == null || coa.getId() == null || coa.getId().isEmpty())
                            throw new CustomException("glCode", "Given glCode is Invalid: " + coa.getId());
                        coaMap.put(billDetail.getChartOfAccount().getGlcode(), coa);
                    }
                    billDetail.setChartOfAccount(coaMap.get(billDetail.getChartOfAccount().getGlcode()));
                }

                if (billDetail.getFunction() != null && billDetail.getFunction().getId() != null) {
                    if (functionMap.get(billDetail.getFunction().getId()) == null) {
                        billDetail.getFunction().setTenantId(tenantId);
                        final Function function = functionService.getFunction(billRegister.getTenantId(),
                                billDetail.getFunction().getCode(), requestInfo);
                        if (function == null || function.getId() == null || function.getId().isEmpty())
                            throw new CustomException("function", "Given function is Invalid: " + function.getId());
                        functionMap.put(billDetail.getFunction().getId(), function);
                    }

                    billDetail.setFunction(functionMap.get(billDetail.getFunction().getId()));
                }
            }

    }

    private void fetchRelatedForBillPayeeDetail(final BillRegister billRegister, final RequestInfo requestInfo) {

        final Map<String, AccountDetailType> adtMap = new HashMap<>();
        final Map<String, AccountDetailKey> adkMap = new HashMap<>();
        final String tenantId = billRegister.getTenantId();
        if (billRegister.getBillDetails() != null) {
            for (final BillDetail billDetail : billRegister.getBillDetails()) {
                if (billDetail.getBillPayeeDetails() != null) {
                    for (final BillPayeeDetail detail : billDetail.getBillPayeeDetails()) {
                        if (detail.getAccountDetailType() != null) {
                            if (adtMap.get(detail.getAccountDetailType().getId()) == null) {
                                detail.getAccountDetailType().setTenantId(tenantId);
                                final AccountDetailType accountDetailType = accountDetailTypeRepository
                                        .findById(detail.getAccountDetailType(), requestInfo);
                                if (accountDetailType == null || accountDetailType.getId() == null
                                        || accountDetailType.getId().isEmpty())
                                    throw new CustomException("accountDetailType",
                                            "Given accountDetailType is Invalid: " + detail.getAccountDetailType().getId());
                                adtMap.put(detail.getAccountDetailType().getId(), accountDetailType);
                            }
                            detail.setAccountDetailType(adtMap.get(detail.getAccountDetailType().getId()));
                        }
                        if (detail.getAccountDetailKey() != null) {
                            if (adkMap.get(detail.getAccountDetailKey().getId()) == null) {
                                detail.getAccountDetailKey().setTenantId(tenantId);
                                final AccountDetailKey accountDetailKey = accountDetailKeyRepository
                                        .findById(detail.getAccountDetailKey(), requestInfo);
                                if (accountDetailKey == null
                                        || accountDetailKey.getId() == null || accountDetailKey.getId().isEmpty())
                                    throw new CustomException("accountDetailType",
                                            "Given accountDetailType is Invalid: " + detail.getAccountDetailKey().getId());
                                adkMap.put(detail.getAccountDetailKey().getId(), accountDetailKey);
                            }
                            detail.setAccountDetailKey(adkMap.get(detail.getAccountDetailKey().getId()));
                        }
                    }
                }
            }
        }
    }

    private void fetchRelatedForChecklist(final BillRegister billRegister) {

        final Map<String, Checklist> checklistMap = new HashMap<>();
        final String tenantId = billRegister.getTenantId();
        if (billRegister.getCheckLists() != null)
            for (final BillChecklist billChecklist : billRegister.getCheckLists())
                if (billChecklist.getChecklist() != null) {
                    if (checklistMap.get(billChecklist.getChecklist().getCode()) == null) {
                        billChecklist.getChecklist().setTenantId(tenantId);
                        ChecklistSearch search = new ChecklistSearch();
                        search.setTenantId(tenantId);
                        search.setCode(billChecklist.getChecklist().getCode());
                        Pagination<Checklist> checkListResponse = checklistRepository.search(search);

                        if (checkListResponse == null || checkListResponse.getPagedData() == null
                                || checkListResponse.getPagedData().isEmpty())
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

    private void populateBillNumbers(BillRegisterRequest billRegisterRequest) {

        for (final BillRegister billregister : billRegisterRequest.getBillRegisters()) {
            billregister
                    .setBillNumber(generateDefaultBillNumber(billregister.getTenantId(), billRegisterRequest.getRequestInfo()));
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

    private List<String> getHeaderMandateFields(String tenantId, RequestInfo requestInfo) {

        List<String> mandatoryFields = new ArrayList<String>();

        FinancialConfiguration financialConfiguration = new FinancialConfiguration();
        financialConfiguration.setModule(Constants.CONFIG_MODULE_NAME);
        financialConfiguration.setName(Constants.DEFAULT_TXN_MIS_ATTRRIBUTES_CONFIG_NAME);
        financialConfiguration.setTenantId(tenantId);

        FinancialConfiguration response = financialConfigurationRepository
                .findByModuleAndName(financialConfiguration, requestInfo);

        if (response != null)
            for (FinancialConfigurationValue configValue : response.getValues()) {
                String value = configValue.getValue();
                final String header = value.substring(0, value.indexOf("|"));
                final String mandate = value.substring(value.indexOf("|") + 1);
                if (mandate.equalsIgnoreCase("M"))
                    mandatoryFields.add(header);
            }

        mandatoryFields.add("billdate");

        return mandatoryFields;
    }

    private void validateMandatoryFields(List<String> mandatoryFields, List<BillRegister> bills) {

        if (bills != null)
            for (BillRegister bill : bills) {

                checkMandatoryField(mandatoryFields, "fund",
                        bill.getFund() != null ? bill.getFund().getId() : null);
                checkMandatoryField(mandatoryFields, "function",
                        bill.getFunction() != null ? bill.getFunction().getId() : null);
                checkMandatoryField(mandatoryFields, "department",
                        bill.getDepartment() != null ? bill.getDepartment().getId() : null);
                checkMandatoryField(mandatoryFields, "scheme",
                        bill.getScheme() != null ? bill.getScheme().getId() : null);
                checkMandatoryField(mandatoryFields, "subscheme",
                        bill.getSubScheme() != null ? bill.getSubScheme().getId() : null);
                checkMandatoryField(mandatoryFields, "functionary",
                        bill.getFunctionary() != null ? bill.getFunctionary().getId() : null);
                checkMandatoryField(mandatoryFields, "fundsource",
                        bill.getFundsource() != null ? bill.getFundsource().getId() : null);
                checkMandatoryField(mandatoryFields, "location",
                        bill.getLocation() != null ? bill.getLocation().getId() : null);
                checkMandatoryField(mandatoryFields, "billdate",
                        bill.getBillDate() != null ? bill.getBillDate() : null);
            }

    }

    private void checkMandatoryField(final List<String> mandatoryFields, final String fieldName, final Object value) {

        LOG.warn("checkMandatoryField---------fieldName--" + fieldName);
        LOG.warn("checkMandatoryField---------StringUtils.isEmpty(value.toString())--"
                + (null != value ? StringUtils.isEmpty(value.toString()) : ""));
        if (mandatoryFields.contains(fieldName) && (value == null || StringUtils.isEmpty(value.toString())))
            throw new CustomException(fieldName,
                    "The field " + fieldName + " is Mandatory . It cannot be not be null or empty.Please provide correct value ");

    }

    private String generateDefaultBillNumber(final String tenantId, final RequestInfo requestInfo) {

        return idgenRepository.getIdGeneration(tenantId, requestInfo, idGenFormatNameForBillPath);
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
