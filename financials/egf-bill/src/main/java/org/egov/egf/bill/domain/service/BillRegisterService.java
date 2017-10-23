package org.egov.egf.bill.domain.service;

import static org.egov.common.constants.Constants.ACTION_VIEW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.ErrorCode;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.repository.BillRegisterRepository;
import org.egov.egf.bill.domain.repository.BoundaryRepository;
import org.egov.egf.bill.domain.repository.ChecklistRepository;
import org.egov.egf.bill.domain.repository.DepartmentRepository;
import org.egov.egf.bill.web.contract.Boundary;
import org.egov.egf.bill.web.contract.Department;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class BillRegisterService {
    private BillRegisterRepository billRegisterRepository;
    private SmartValidator validator;
    private SchemeContractRepository schemeContractRepository;
    private BoundaryRepository boundaryRepository;
    private FunctionaryContractRepository functionaryContractRepository;
    private FunctionContractRepository functionContractRepository;
    private ChartOfAccountContractRepository chartOfAccountContractRepository;
    private ChecklistRepository checklistRepository;
    private AccountDetailTypeContractRepository accountDetailTypeContractRepository;
    private AccountDetailKeyContractRepository accountDetailKeyContractRepository;
    private FundsourceContractRepository fundsourceContractRepository;
    private FinancialStatusContractRepository financialStatusContractRepository;
    private FundContractRepository fundContractRepository;
    private DepartmentRepository departmentRepository;
    private SubSchemeContractRepository subSchemeContractRepository;
    
    @Autowired
    public BillRegisterService(BillRegisterRepository billRegisterRepository, SchemeContractRepository schemeContractRepository,
    		BoundaryRepository boundaryRepository, FunctionaryContractRepository functionaryContractRepository, FunctionContractRepository functionContractRepository,
    		ChartOfAccountContractRepository chartOfAccountContractRepository, ChecklistRepository checklistRepository,
    		AccountDetailTypeContractRepository accountDetailTypeContractRepository, AccountDetailKeyContractRepository accountDetailKeyContractRepository,
    		FundsourceContractRepository fundsourceContractRepository, FinancialStatusContractRepository financialStatusContractRepository,
    		FundContractRepository fundContractRepository, DepartmentRepository departmentRepository, SubSchemeContractRepository subSchemeContractRepository, SmartValidator validator) {
    	this.billRegisterRepository = billRegisterRepository;
    	this.schemeContractRepository = schemeContractRepository;
    	this.boundaryRepository = boundaryRepository;
    	this.functionaryContractRepository = functionaryContractRepository;
    	this.functionContractRepository = functionContractRepository;
    	this.chartOfAccountContractRepository = chartOfAccountContractRepository;
    	this.checklistRepository = checklistRepository;
    	this.accountDetailTypeContractRepository = accountDetailTypeContractRepository;
    	this.accountDetailKeyContractRepository = accountDetailKeyContractRepository;
    	this.fundsourceContractRepository = fundsourceContractRepository;
    	this.financialStatusContractRepository = financialStatusContractRepository;
    	this.fundContractRepository = fundContractRepository;
    	this. departmentRepository = departmentRepository;
    	this.subSchemeContractRepository = subSchemeContractRepository;
    	this.validator = validator;
    }

    @Transactional
	public List<BillRegister> create(List<BillRegister> billregisters,
			BindingResult errors, RequestInfo requestInfo) {
		try {
			billregisters = fetchRelated(billregisters,requestInfo);
			validate(billregisters, Constants.ACTION_CREATE, errors);
			populateIds(billregisters);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}
			for (BillRegister b : billregisters) {
				b.setId(billRegisterRepository.getNextSequence());
			}
		} catch (CustomBindException e) {
			throw e;
		}
		return billRegisterRepository.save(billregisters, requestInfo);
	}

    @Transactional
    public List<BillRegister> update(List<BillRegister> billregisters, BindingResult errors, RequestInfo requestInfo) {
	try {
	    billregisters = fetchRelated(billregisters,requestInfo);
	    validate(billregisters, Constants.ACTION_UPDATE, errors);
	    if (errors.hasErrors()) {
		throw new CustomBindException(errors);
	    }
	} catch (CustomBindException e) {
	    throw new CustomBindException(errors);
	}
	return billRegisterRepository.update(billregisters, requestInfo);
    }

    private BindingResult validate(List<BillRegister> billregisters, String method, BindingResult errors) {
	try {
	    switch (method) {
	    case ACTION_VIEW:
		// validator.validate(billRegisterContractRequest.getBillRegister(),
		// errors);
		break;
	    case Constants.ACTION_CREATE:
		if (billregisters == null) {
		    throw new InvalidDataException("billregisters", ErrorCode.NOT_NULL.getCode(), null);
		}
		for (BillRegister billRegister : billregisters) {
			if(billRegister.getPassedAmount().compareTo(billRegister.getBillAmount()) > 0){
				errors.addError(new FieldError("billRegister", "passedAmount", billRegister.getPassedAmount(), false,
                        new String[] { "Invalid Amount" }, null, "passedAmount must be less than billAmount"));
			}
		    validator.validate(billRegister, errors);
		    if (!billRegisterRepository.uniqueCheck("id", billRegister)) {
                errors.addError(new FieldError("billRegister", "id", billRegister.getId(), false,
                        new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
            }
		}
		break;
	    case Constants.ACTION_UPDATE:
		if (billregisters == null) {
		    throw new InvalidDataException("billregisters", ErrorCode.NOT_NULL.getCode(), null);
		}
		for (BillRegister billRegister : billregisters) {
		    if (billRegister.getId() == null) {
			throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
				billRegister.getId());
		    }
		    validator.validate(billRegister, errors);
		    if (!billRegisterRepository.uniqueCheck("id", billRegister)) {
                errors.addError(new FieldError("billRegister", "id", billRegister.getId(), false,
                        new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
            }
		}
		break;
	    case Constants.ACTION_SEARCH:
		if (billregisters == null) {
		    throw new InvalidDataException("billregisters", ErrorCode.NOT_NULL.getCode(), null);
		}
		for (BillRegister billRegister : billregisters) {
		    if (billRegister.getTenantId() == null) {
			throw new InvalidDataException("tenantId", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
				billRegister.getTenantId());
		    }
		    if (!billRegisterRepository.uniqueCheck("id", billRegister)) {
                errors.addError(new FieldError("billRegister", "id", billRegister.getId(), false,
                        new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
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

	public List<BillRegister> fetchRelated(List<BillRegister> billregisters,RequestInfo requestInfo) {
		if (null != billregisters){
			for (BillRegister billRegister : billregisters) {
				// fetch related items
				if (billRegister.getStatus() != null) {
					billRegister.getStatus().setTenantId(billRegister.getTenantId());
					FinancialStatusContract status = financialStatusContractRepository
							.findById(billRegister.getStatus(),requestInfo);
					if (status == null) {
						throw new InvalidDataException("status",
								"status.invalid", " Invalid status");
					}
					billRegister.setStatus(status);
				}
				if (billRegister.getFund() != null) {
					billRegister.getFund().setTenantId(billRegister.getTenantId());
					FundContract fund = fundContractRepository
							.findById(billRegister.getFund(),requestInfo);
					if (fund == null) {
						throw new InvalidDataException("fund", "fund.invalid",
								" Invalid fund");
					}
					billRegister.setFund(fund);
				}
				if (billRegister.getFunction() != null) {
					billRegister.getFunction().setTenantId(billRegister.getTenantId());
					FunctionContract function = functionContractRepository
							.findById(billRegister.getFunction(),requestInfo);
					if (function == null) {
						throw new InvalidDataException("function",
								"function.invalid", " Invalid function");
					}
					billRegister.setFunction(function);
				}
				if (billRegister.getFundsource() != null) {
					billRegister.getFundsource().setTenantId(billRegister.getTenantId());
					FundsourceContract fundsource = fundsourceContractRepository
							.findById(billRegister.getFundsource(),requestInfo);
					if (fundsource == null) {
						throw new InvalidDataException("fundsource",
								"fundsource.invalid", " Invalid fundsource");
					}
					billRegister.setFundsource(fundsource);
				}
				if (billRegister.getScheme() != null) {
					billRegister.getScheme().setTenantId(billRegister.getTenantId());
					SchemeContract scheme = schemeContractRepository
							.findById(billRegister.getScheme(),requestInfo);
					if (scheme == null) {
						throw new InvalidDataException("scheme",
								"scheme.invalid", " Invalid scheme");
					}
					billRegister.setScheme(scheme);
				}
				if (billRegister.getSubScheme() != null) {
					billRegister.getSubScheme().setTenantId(billRegister.getTenantId());
					SubSchemeContract subScheme = subSchemeContractRepository
							.findById(billRegister.getSubScheme(),requestInfo);
					if (subScheme == null) {
						throw new InvalidDataException("subScheme",
								"subScheme.invalid", " Invalid subScheme");
					}
					billRegister.setSubScheme(subScheme);
				}
				if (billRegister.getFunctionary() != null) {
					billRegister.getFunctionary().setTenantId(billRegister.getTenantId());
					FunctionaryContract functionary = functionaryContractRepository
							.findById(billRegister.getFunctionary(),requestInfo);
					if (functionary == null) {
						throw new InvalidDataException("functionary",
								"functionary.invalid", " Invalid functionary");
					}
					billRegister.setFunctionary(functionary);
				}
				if (billRegister.getDivision() != null) {
					billRegister.getDivision().setTenantId(billRegister.getTenantId());
					Boundary division = boundaryRepository
							.findById(billRegister.getDivision());
					if (division == null) {
						throw new InvalidDataException("division",
								"division.invalid", " Invalid division");
					}
					billRegister.setDivision(division);
				}
				if (billRegister.getDepartment() != null) {
					billRegister.getDepartment().setTenantId(billRegister.getTenantId());
					Department department = departmentRepository
							.findById(billRegister.getDepartment());
					if (department == null) {
						throw new InvalidDataException("department",
								"department.invalid", " Invalid department");
					}
					billRegister.setDepartment(department);
				}
				fetchRelatedForBillDetail(billRegister, requestInfo);
				fetchRelatedForBillPayeeDetail(billRegister, requestInfo);
				fetchRelatedForChecklist(billRegister);
			}
		}
		return billregisters;
	}
	
	private void fetchRelatedForBillDetail(BillRegister billRegister, RequestInfo requestInfo) {

		Map<String, ChartOfAccountContract> coaMap = new HashMap<>();
		Map<String, FunctionContract> functionMap = new HashMap<>();
		String tenantId = billRegister.getTenantId();

		for (BillDetail billDetail : billRegister.getBillDetails()) {
			if (billDetail.getGlcode() != null) {
				ChartOfAccountContract coa = null;
				if (coaMap.get(billDetail.getGlcode()) == null) {
					ChartOfAccountContract coaContract = new ChartOfAccountContract();
					coaContract.setGlcode(billDetail.getGlcode());
					coaContract.setTenantId(tenantId);
					coa = chartOfAccountContractRepository.findByGlcode(coaContract, requestInfo);
					if (coa == null) {
						throw new InvalidDataException("glcode", ErrorCode.INVALID_REF_VALUE.getCode(),
								billDetail.getGlcode());
					}
					coaMap.put(billDetail.getGlcode(), coa);

				}
				billDetail.setChartOfAccount(coaMap.get(billDetail.getGlcode()));
			}

			if (billDetail.getFunction() != null) {
				if (functionMap.get(billDetail.getFunction().getId()) == null) {
					billDetail.getFunction().setTenantId(tenantId);
					FunctionContract function = functionContractRepository.findById(billDetail.getFunction(), requestInfo);

					if (function == null) {
						throw new InvalidDataException("function", ErrorCode.INVALID_REF_VALUE.getCode(),
								billDetail.getFunction().getId());
					}
					functionMap.put(billDetail.getFunction().getId(), function);
				}

				billDetail.setFunction(functionMap.get(billDetail.getFunction().getId()));
			}
		}

	}

	private void fetchRelatedForBillPayeeDetail(BillRegister billRegister, RequestInfo requestInfo) {

		Map<String, AccountDetailTypeContract> adtMap = new HashMap<>();
		Map<String, AccountDetailKeyContract> adkMap = new HashMap<>();
		String tenantId = billRegister.getTenantId();

		for (BillDetail billDetail : billRegister.getBillDetails()) {

			if (billDetail.getBillPayeeDetails() != null)

				for (BillPayeeDetail detail : billDetail.getBillPayeeDetails()) {

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
	
	private void fetchRelatedForChecklist(BillRegister billRegister) {
		
		Map<String, Checklist> checklistMap = new HashMap<>();
		String tenantId = billRegister.getTenantId();
		for (BillChecklist billChecklist : billRegister.getCheckLists()) {
			if (billChecklist.getChecklist() != null) {
				if (checklistMap.get(billChecklist.getChecklist().getId()) == null) {
					billChecklist.getChecklist().setTenantId(tenantId);
					Checklist checklist = checklistRepository.findById(billChecklist.getChecklist());

					if (checklist == null) {
						throw new InvalidDataException("checklist", ErrorCode.INVALID_REF_VALUE.getCode(),
								billChecklist.getChecklist().getId());
					}
					checklistMap.put(billChecklist.getChecklist().getId(), checklist);
				}

				billChecklist.setChecklist(checklistMap.get(billChecklist.getChecklist().getId()));
			}
		}
	}

    public Pagination<BillRegister> search(BillRegisterSearch billRegisterSearch, BindingResult errors) {
	try {
	    List<BillRegister> billregisters = new ArrayList<>();
		if (billRegisterSearch != null){
			billregisters.add(billRegisterSearch);
			validate(billregisters, Constants.ACTION_SEARCH, errors);					
		}
		else
			validate(null, Constants.ACTION_SEARCH, errors);
	    if (errors.hasErrors()) {
		throw new CustomBindException(errors);
	    }
	} catch (CustomBindException e) {
	    throw e;
	}
	return billRegisterRepository.search(billRegisterSearch);
    }

	private void populateIds(List<BillRegister> billregisters) {

		for (BillRegister billregister : billregisters) {
			billregister.setId(UUID.randomUUID().toString().replace("-", ""));
			if (null != billregister.getBillDetails())
				for (BillDetail billDetail : billregister.getBillDetails()) {
					billDetail.setId(UUID.randomUUID().toString().replace("-", ""));
					if (null != billDetail.getBillPayeeDetails())
						for (BillPayeeDetail billPayeeDetail : billDetail.getBillPayeeDetails()) {
							billPayeeDetail.setId(UUID.randomUUID().toString().replace("-", ""));
						}
				}
		}
		
		for(BillRegister billregister : billregisters){
			
			if(null != billregister.getCheckLists()){
				for(BillChecklist checklist : billregister.getCheckLists()){
					checklist.setId(UUID.randomUUID().toString().replace("-", ""));
				}
			}
		}

	}
    
    @Transactional
    public BillRegister save(BillRegister billRegister) {
	return billRegisterRepository.save(billRegister);
    }

    @Transactional
    public BillRegister update(BillRegister billRegister) {
	return billRegisterRepository.update(billRegister);
    }
}
