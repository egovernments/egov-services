package org.egov.egf.voucher.domain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.ErrorCode;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.web.contract.AccountDetailKeyContract;
import org.egov.egf.master.web.contract.AccountDetailTypeContract;
import org.egov.egf.master.web.contract.ChartOfAccountContract;
import org.egov.egf.master.web.contract.FinancialConfigurationContract;
import org.egov.egf.master.web.contract.FinancialConfigurationValueContract;
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
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.egov.egf.master.web.repository.FinancialStatusContractRepository;
import org.egov.egf.master.web.repository.FunctionContractRepository;
import org.egov.egf.master.web.repository.FunctionaryContractRepository;
import org.egov.egf.master.web.repository.FundContractRepository;
import org.egov.egf.master.web.repository.FundsourceContractRepository;
import org.egov.egf.master.web.repository.SchemeContractRepository;
import org.egov.egf.master.web.repository.SubSchemeContractRepository;
import org.egov.egf.voucher.domain.model.Ledger;
import org.egov.egf.voucher.domain.model.LedgerDetail;
import org.egov.egf.voucher.domain.model.Voucher;
import org.egov.egf.voucher.domain.model.VoucherSearch;
import org.egov.egf.voucher.domain.repository.VoucherRepository;
import org.egov.egf.voucher.web.contract.Boundary;
import org.egov.egf.voucher.web.contract.DepartmentResponse;
import org.egov.egf.voucher.web.repository.BoundaryRepository;
import org.egov.egf.voucher.web.repository.DepartmentRepository;
import org.egov.egf.voucher.web.util.VoucherConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class VoucherService {

	private static final Logger LOG = LoggerFactory.getLogger(VoucherService.class);
	
    protected List<String> headerFields = new ArrayList<String>();
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private SmartValidator validator;
    @Autowired
    private FinancialStatusContractRepository financialStatusContractRepository;
    @Autowired
    private FinancialConfigurationContractRepository financialConfigurationContractRepository;
    @Autowired
    private ChartOfAccountContractRepository chartOfAccountContractRepository;
    @Autowired
    private FundContractRepository fundContractRepository;
    @Autowired
    private FunctionContractRepository funnctionContractRepository;
    @Autowired
    private FundsourceContractRepository fundsourceContractRepository;
    @Autowired
    private SchemeContractRepository schemeContractRepository;
    @Autowired
    private SubSchemeContractRepository subSchemeContractRepository;
    @Autowired
    private FunctionaryContractRepository functionaryContractRepository;
    @Autowired
    private AccountDetailKeyContractRepository accountDetailKeyContractRepository;
    @Autowired
    private AccountDetailTypeContractRepository accountDetailTypeContractRepository;
    @Autowired
    private BoundaryRepository boundaryRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private VouchernumberGenerator vouchernumberGenerator;
    private List<String> mandatoryFields = new ArrayList<String>();

    @Transactional
    public List<Voucher> create(List<Voucher> vouchers, BindingResult errors, RequestInfo requestInfo) {

        try {
            vouchers = fetchRelated(vouchers, requestInfo);
            validate(vouchers, Constants.ACTION_CREATE, errors, requestInfo);

            if (errors.hasErrors()) {
                throw new CustomBindException(errors);
            }

            populateVoucherNumbers(vouchers);

        } catch (CustomBindException e) {
            throw new CustomBindException(errors);
        }

        return voucherRepository.save(vouchers, requestInfo);
    }

    @Transactional
    public List<Voucher> reverse(List<Voucher> vouchers, BindingResult errors, RequestInfo requestInfo) {

        List<Voucher> reverseVouchers = new ArrayList<>();
        try {
            for (Voucher voucher : vouchers) {
                if (null == voucher.getOriginalVoucherNumber() || voucher.getOriginalVoucherNumber().isEmpty())
                    throw new InvalidDataException("originalVoucherNumber", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
                            voucher.getOriginalVoucherNumber());

                if (null != voucher.getPartial() && voucher.getPartial()) {
                    reverseVouchers.add(voucher);
                } else {
                    Voucher reverseVoucher = prepareReverseVoucher(voucher, errors);
                    reverseVouchers.add(reverseVoucher);
                }
            }
            reverseVouchers = fetchRelated(reverseVouchers, requestInfo);
            validate(reverseVouchers, Constants.ACTION_CREATE, errors, requestInfo);

            if (errors.hasErrors()) {
                throw new CustomBindException(errors);
            }

            populateVoucherNumbers(reverseVouchers);

        } catch (CustomBindException e) {
            throw new CustomBindException(errors);
        }

        return voucherRepository.save(reverseVouchers, requestInfo);
    }

    private Voucher prepareReverseVoucher(Voucher voucher, BindingResult errors) {

        VoucherSearch voucherSearch = new VoucherSearch();
        voucherSearch.setTenantId(voucher.getTenantId());
        voucherSearch.setVoucherNumber(voucher.getOriginalVoucherNumber());
        Pagination<Voucher> searchResult = search(voucherSearch, errors);

        if (null == searchResult || null == searchResult.getPagedData() || searchResult.getPagedData().isEmpty())
            throw new InvalidDataException("originalVoucherNumber", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
                    voucher.getOriginalVoucherNumber());

        Voucher originalVoucher = searchResult.getPagedData().get(0);
        voucher = originalVoucher;
        voucher.setOriginalVoucherNumber(voucher.getVoucherNumber());
        voucher.setVoucherNumber(null);
        populateLedgerForReverseVoucher(voucher);

        return voucher;
    }

    private void populateLedgerForReverseVoucher(Voucher voucher) {

        BigDecimal originalDebitAmount, originalCreditAmount;

        for (Ledger ol : voucher.getLedgers()) {
            originalDebitAmount = ol.getDebitAmount();
            originalCreditAmount = ol.getCreditAmount();
            ol.setDebitAmount(originalCreditAmount);
            ol.setCreditAmount(originalDebitAmount);
        }
    }

    private void populateVoucherNumbers(List<Voucher> vouchers) {
        for (Voucher voucher : vouchers) {
        	voucher.setVoucherNumber(vouchernumberGenerator.getNextNumber(voucher));
        }
    }

    @Transactional
    public List<Voucher> update(List<Voucher> vouchers, BindingResult errors, RequestInfo requestInfo) {
        try {
            vouchers = fetchRelated(vouchers, requestInfo);
            validate(vouchers, Constants.ACTION_UPDATE, errors, requestInfo);

            if (errors.hasErrors()) {
                throw new CustomBindException(errors);
            }
        } catch (CustomBindException e) {
            throw new CustomBindException(errors);
        }
        return voucherRepository.update(vouchers, requestInfo);

    }

    private BindingResult validate(List<Voucher> vouchers, String method, BindingResult errors,
            RequestInfo requestInfo) {
        try {
            switch (method) {
            case Constants.ACTION_VIEW:
                // validator.validate(voucherContractRequest.getVoucher(),
                // errors);
                break;
            case Constants.ACTION_CREATE:
                if (null == vouchers || vouchers.isEmpty())
                    throw new InvalidDataException("vouchers", ErrorCode.NOT_NULL.getCode(), null);
                for (Voucher voucher : vouchers) {
                	LOG.warn("voucherDate---------------------"+voucher.getVoucherDate());
                    validator.validate(voucher, errors);
                    if (!voucherRepository.uniqueCheck("voucherNumber", voucher)) {
                        errors.addError(new FieldError("voucher", "voucherNumber", voucher.getVoucherNumber(), false,
                                new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
                    }
                }
                String tenantId = null;
                tenantId = (null != vouchers && !vouchers.isEmpty()) ? vouchers.get(0).getTenantId() : null;
                LOG.warn(" tenantId---------------------"+ tenantId);
                getHeaderMandateFields(tenantId, requestInfo);
                validateMandatoryFields(vouchers);
                checkBudget(vouchers);
                break;
            case Constants.ACTION_UPDATE:
                if (null == vouchers || vouchers.isEmpty())
                    throw new InvalidDataException("vouchers", ErrorCode.NOT_NULL.getCode(), null);
                for (Voucher voucher : vouchers) {
                    validator.validate(voucher, errors);
                    if (!voucherRepository.uniqueCheck("voucherNumber", voucher)) {
                        errors.addError(new FieldError("voucher", "voucherNumber", voucher.getVoucherNumber(), false,
                                new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
                    }
                }
                break;
            case Constants.ACTION_SEARCH:
                if (vouchers == null) {
                    throw new InvalidDataException("vouchers", ErrorCode.NOT_NULL.getCode(), null);
                }
                for (Voucher voucher : vouchers) {
                    if (voucher.getTenantId() == null) {
                        throw new InvalidDataException("tenantId", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
                                voucher.getTenantId());
                    }
                }
                break;
            default:
            }
        } catch (IllegalArgumentException e) {
            errors.addError(new ObjectError("Missing data", e.getMessage()));
        }
        return errors;
    }

    private void checkBudget(List<Voucher> vouchers) {

        // Implement ME.

    }

    public List<Voucher> fetchRelated(List<Voucher> vouchers, RequestInfo requestInfo) {

        if (null != vouchers)
            for (Voucher voucher : vouchers) {
                // fetch related items
                if (voucher.getFund() != null) {
                    voucher.getFund().setTenantId(voucher.getTenantId());
                    FundContract fund = fundContractRepository.findById(voucher.getFund(), requestInfo);
                    if (fund == null) {
                        throw new InvalidDataException("fund", ErrorCode.INVALID_REF_VALUE.getCode(),
                                voucher.getFund().getId());
                    }
                    voucher.setFund(voucher.getFund());
                }

                if (voucher.getStatus() != null) {
                    voucher.getStatus().setTenantId(voucher.getTenantId());
                    FinancialStatusContract status = financialStatusContractRepository.findById(voucher.getStatus(),
                            requestInfo);
                    if (status == null) {
                        throw new InvalidDataException("status", ErrorCode.INVALID_REF_VALUE.getCode(),
                                voucher.getStatus().getId());
                    }
                    voucher.setStatus(voucher.getStatus());
                }

                if (voucher.getFunction() != null) {
                    voucher.getFunction().setTenantId(voucher.getTenantId());
                    FunctionContract function = funnctionContractRepository.findById(voucher.getFunction(),
                            requestInfo);
                    if (function == null) {
                        throw new InvalidDataException("function", ErrorCode.INVALID_REF_VALUE.getCode(),
                                voucher.getFunction().getId());
                    }
                    voucher.setFunction(voucher.getFunction());
                }

                if (voucher.getFundsource() != null) {
                    voucher.getFundsource().setTenantId(voucher.getTenantId());
                    FundsourceContract fundsource = fundsourceContractRepository.findById(voucher.getFundsource(),
                            requestInfo);
                    if (fundsource == null) {
                        throw new InvalidDataException("fundsource", ErrorCode.INVALID_REF_VALUE.getCode(),
                                voucher.getFundsource().getId());
                    }
                    voucher.setFundsource(fundsource);
                }

                if (voucher.getScheme() != null) {
                    voucher.getScheme().setTenantId(voucher.getTenantId());
                    SchemeContract scheme = schemeContractRepository.findById(voucher.getScheme(), requestInfo);
                    if (scheme == null) {
                        throw new InvalidDataException("scheme", ErrorCode.INVALID_REF_VALUE.getCode(),
                                voucher.getScheme().getId());
                    }
                    voucher.setScheme(scheme);
                }

                if (voucher.getSubScheme() != null) {
                    voucher.getSubScheme().setTenantId(voucher.getTenantId());
                    SubSchemeContract subScheme = subSchemeContractRepository.findById(voucher.getSubScheme(),
                            requestInfo);
                    if (subScheme == null) {
                        throw new InvalidDataException("subScheme", ErrorCode.INVALID_REF_VALUE.getCode(),
                                voucher.getSubScheme().getId());
                    }
                    voucher.setSubScheme(subScheme);
                }

                if (voucher.getFunctionary() != null) {
                    voucher.getFunctionary().setTenantId(voucher.getTenantId());
                    FunctionaryContract functionary = functionaryContractRepository.findById(voucher.getFunctionary(),
                            requestInfo);
                    if (functionary == null) {
                        throw new InvalidDataException("functionary", ErrorCode.INVALID_REF_VALUE.getCode(),
                                voucher.getFunctionary().getId());
                    }
                    voucher.setFunctionary(functionary);
                }

                if (voucher.getDivision() != null) {
                    voucher.getDivision().setTenantId(voucher.getTenantId());
                    Boundary devision = boundaryRepository.getBoundaryById(voucher.getDivision().getId(),
                            voucher.getDivision().getTenantId());
                    if (devision == null) {
                        throw new InvalidDataException("division", ErrorCode.INVALID_REF_VALUE.getCode(),
                                voucher.getDivision().getId());
                    }
                    voucher.setDivision(devision);
                }

                if (voucher.getDepartment() != null) {
                    voucher.getDepartment().setTenantId(voucher.getTenantId());
                    DepartmentResponse department = departmentRepository
                            .getDepartmentById(voucher.getDepartment().getId(), voucher.getDepartment().getTenantId());
                    if (department == null || department.getDepartment() == null
                            || department.getDepartment().isEmpty()) {
                        throw new InvalidDataException("department", ErrorCode.INVALID_REF_VALUE.getCode(),
                                voucher.getDepartment().getId());
                    }
                    voucher.setDepartment(department.getDepartment().get(0));
                }
                fetchRelatedForLedger(voucher, requestInfo);
                fetchRelatedForLedgerDetail(voucher, requestInfo);

            }
        return vouchers;
    }

    private void fetchRelatedForLedger(Voucher voucher, RequestInfo requestInfo) {

        Map<String, ChartOfAccountContract> coaMap = new HashMap<>();
        Map<String, FunctionContract> functionMap = new HashMap<>();
        String tenantId = voucher.getTenantId();

        for (Ledger ledger : voucher.getLedgers()) {
            if (ledger.getGlcode() != null) {
                ChartOfAccountContract coa = null;
                if (coaMap.get(ledger.getGlcode()) == null) {
                    ChartOfAccountContract coaContract = new ChartOfAccountContract();
                    coaContract.setGlcode(ledger.getGlcode());
                    coaContract.setTenantId(tenantId);
                    coa = chartOfAccountContractRepository.findByGlcode(coaContract, requestInfo);
                    if (coa == null) {
                        throw new InvalidDataException("glcode", ErrorCode.INVALID_REF_VALUE.getCode(),
                                ledger.getGlcode());
                    }
                    coaMap.put(ledger.getGlcode(), coa);

                }
                ledger.setChartOfAccount(coaMap.get(ledger.getGlcode()));
            }

            if (ledger.getFunction() != null) {
                if (functionMap.get(ledger.getFunction().getId()) == null) {
                    ledger.getFunction().setTenantId(tenantId);
                    FunctionContract function = funnctionContractRepository.findById(ledger.getFunction(), requestInfo);

                    if (function == null) {
                        throw new InvalidDataException("function", ErrorCode.INVALID_REF_VALUE.getCode(),
                                ledger.getFunction().getId());
                    }
                    functionMap.put(ledger.getFunction().getId(), function);
                }

                ledger.setFunction(functionMap.get(ledger.getFunction().getId()));
            }
        }

    }

    private void fetchRelatedForLedgerDetail(Voucher voucher, RequestInfo requestInfo) {

        Map<String, AccountDetailTypeContract> adtMap = new HashMap<>();
        Map<String, AccountDetailKeyContract> adkMap = new HashMap<>();
        String tenantId = voucher.getTenantId();

        for (Ledger ledger : voucher.getLedgers()) {

            if (ledger.getLedgerDetails() != null)

                for (LedgerDetail detail : ledger.getLedgerDetails()) {

                    if (detail.getAccountDetailType() != null) {

                        if (adtMap.get(detail.getAccountDetailType().getId()) == null) {
                            detail.getAccountDetailType().setTenantId(tenantId);
                            AccountDetailTypeContract accountDetailType = accountDetailTypeContractRepository
                                    .findById(detail.getAccountDetailType(), requestInfo);

                            if (accountDetailType == null) {
                                throw new InvalidDataException("accountDetailType",
                                        ErrorCode.INVALID_REF_VALUE.getCode(), detail.getAccountDetailType().getId());
                            }
                            adtMap.put(detail.getAccountDetailType().getId(), accountDetailType);
                        }
                        detail.setAccountDetailType(adtMap.get(detail.getAccountDetailType().getId()));
                    }

                    if (detail.getAccountDetailKey() != null) {

                        if (adkMap.get(detail.getAccountDetailKey().getId()) == null) {
                            detail.getAccountDetailKey().setTenantId(tenantId);
                            AccountDetailKeyContract accountDetailKey = accountDetailKeyContractRepository
                                    .findById(detail.getAccountDetailKey(), requestInfo);

                            if (accountDetailKey == null) {
                                throw new InvalidDataException("accountDetailKey",
                                        ErrorCode.INVALID_REF_VALUE.getCode(), detail.getAccountDetailKey().getId());
                            }

                            adkMap.put(detail.getAccountDetailKey().getId(), accountDetailKey);
                        }
                        detail.setAccountDetailKey(adkMap.get(detail.getAccountDetailKey().getId()));
                    }
                }
        }
    }

    private void getHeaderMandateFields(String tenantId, RequestInfo requestInfo) {

        FinancialConfigurationContract financialConfigurationContract = new FinancialConfigurationContract();
        financialConfigurationContract.setModule(VoucherConstants.CONFIG_MODULE_NAME);
        financialConfigurationContract.setName(VoucherConstants.DEFAULT_TXN_MIS_ATTRRIBUTES_CONFIG_NAME);
        financialConfigurationContract.setTenantId(tenantId);

        FinancialConfigurationContract response = financialConfigurationContractRepository
                .findByModuleAndName(financialConfigurationContract, requestInfo);

        if (response != null)
            for (FinancialConfigurationValueContract configValue : response.getValues()) {
                String value = configValue.getValue();
                final String header = value.substring(0, value.indexOf("|"));
                headerFields.add(header);
                final String mandate = value.substring(value.indexOf("|") + 1);
                if (mandate.equalsIgnoreCase("M"))
                    mandatoryFields.add(header);
            }

        mandatoryFields.add("voucherdate");
    }

    protected void validateMandatoryFields(List<Voucher> vouchers) {

        if (vouchers != null)
            for (Voucher voucher : vouchers) {

                checkMandatoryField("vouchernumber", voucher.getVoucherNumber());
                checkMandatoryField("voucherdate", voucher.getVoucherDate());
                checkMandatoryField("fund", voucher.getFund() != null ? voucher.getFund().getId() : null);
                checkMandatoryField("function", voucher.getFunction() != null ? voucher.getFunction().getId() : null);
                checkMandatoryField("department",
                        voucher.getDepartment() != null ? voucher.getDepartment().getId() : null);
                checkMandatoryField("scheme", voucher.getScheme() != null ? voucher.getScheme().getId() : null);
                checkMandatoryField("subscheme",
                        voucher.getSubScheme() != null ? voucher.getSubScheme().getId() : null);
                checkMandatoryField("functionary",
                        voucher.getFunctionary() != null ? voucher.getFunctionary().getId() : null);
                checkMandatoryField("fundsource",
                        voucher.getFundsource() != null ? voucher.getFundsource().getId() : null);
                checkMandatoryField("field", voucher.getDivision() != null ? voucher.getDivision().getId() : null);
            }

    }

    protected void checkMandatoryField(final String fieldName, final Object value) {
    	LOG.warn("checkMandatoryField---------fieldName--"+fieldName);
    	LOG.warn("checkMandatoryField---------value--"+value);
    	LOG.warn("checkMandatoryField---------StringUtils.isEmpty(value.toString())--"+StringUtils.isEmpty(value.toString()));
        if (mandatoryFields.contains(fieldName) && (value == null || StringUtils.isEmpty(value.toString())))
            throw new InvalidDataException(fieldName, ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
                    null != value ? value.toString() : null);
    }

    public Pagination<Voucher> search(VoucherSearch voucherSearch, BindingResult errors) {

        try {
            List<Voucher> vouchers = new ArrayList<>();
            vouchers.add(voucherSearch);
            validate(vouchers, Constants.ACTION_SEARCH, errors, new RequestInfo());
            if (errors.hasErrors()) {
                throw new CustomBindException(errors);
            }
        } catch (CustomBindException e) {

            throw new CustomBindException(errors);
        }

        return voucherRepository.search(voucherSearch);
    }

    @Transactional
    public Voucher save(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    @Transactional
    public Voucher update(Voucher voucher) {
        return voucherRepository.update(voucher);
    }

}