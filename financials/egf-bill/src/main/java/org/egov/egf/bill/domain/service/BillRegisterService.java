package org.egov.egf.bill.domain.service;

import static org.egov.common.constants.Constants.ACTION_VIEW;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.ErrorCode;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.domain.repository.BillRegisterRepository;
import org.egov.egf.bill.domain.repository.BoundaryRepository;
import org.egov.egf.bill.domain.repository.DepartmentRepository;
import org.egov.egf.bill.web.contract.Boundary;
import org.egov.egf.bill.web.contract.Department;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.FundsourceContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;
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
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class BillRegisterService {
    @Autowired
    private BillRegisterRepository billRegisterRepository;
    @Autowired
    private SmartValidator validator;
    @Autowired
    private SchemeContractRepository schemeContractRepository;
    @Autowired
    private BoundaryRepository boundaryRepository;
    @Autowired
    private FunctionaryContractRepository functionaryContractRepository;
    @Autowired
    private FunctionContractRepository functionContractRepository;
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
    public List<BillRegister> create(List<BillRegister> billregisters, BindingResult errors, RequestInfo requestInfo) {
	try {
	    billregisters = fetchRelated(billregisters);
	    validate(billregisters, Constants.ACTION_CREATE, errors);
	    if (errors.hasErrors()) {
		throw new CustomBindException(errors);
	    }
	    for (BillRegister b : billregisters) {
		b.setId(billRegisterRepository.getNextSequence());
		//b.add();
	    }
	} catch (CustomBindException e) {
	    throw e;
	}
	return billRegisterRepository.save(billregisters, requestInfo);
    }

    @Transactional
    public List<BillRegister> update(List<BillRegister> billregisters, BindingResult errors, RequestInfo requestInfo) {
	try {
	    billregisters = fetchRelated(billregisters);
	    validate(billregisters, Constants.ACTION_UPDATE, errors);
	    if (errors.hasErrors()) {
		throw new CustomBindException(errors);
	    }
	    for (BillRegister b : billregisters) {
		//b.update();
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
		    validator.validate(billRegister, errors);
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
		}
		break;
	    default:
	    }
	} catch (IllegalArgumentException e) {
	    errors.addError(new ObjectError("Missing data", e.getMessage()));
	}
	return errors;
    }

    public List<BillRegister> fetchRelated(List<BillRegister> billregisters) {
	for (BillRegister billRegister : billregisters) {
	    // fetch related items
	    if (billRegister.getStatus() != null) {
		FinancialStatusContract status = financialStatusContractRepository.findById(billRegister.getStatus());
		if (status == null) {
		    throw new InvalidDataException("status", "status.invalid", " Invalid status");
		}
		billRegister.setStatus(status);
	    }
	    if (billRegister.getFund() != null) {
		FundContract fund = fundContractRepository.findById(billRegister.getFund());
		if (fund == null) {
		    throw new InvalidDataException("fund", "fund.invalid", " Invalid fund");
		}
		billRegister.setFund(fund);
	    }
	    if (billRegister.getFunction() != null) {
		FunctionContract function = functionContractRepository.findById(billRegister.getFunction());
		if (function == null) {
		    throw new InvalidDataException("function", "function.invalid", " Invalid function");
		}
		billRegister.setFunction(function);
	    }
	    if (billRegister.getFundsource() != null) {
		FundsourceContract fundsource = fundsourceContractRepository.findById(billRegister.getFundsource());
		if (fundsource == null) {
		    throw new InvalidDataException("fundsource", "fundsource.invalid", " Invalid fundsource");
		}
		billRegister.setFundsource(fundsource);
	    }
	    if (billRegister.getScheme() != null) {
		SchemeContract scheme = schemeContractRepository.findById(billRegister.getScheme());
		if (scheme == null) {
		    throw new InvalidDataException("scheme", "scheme.invalid", " Invalid scheme");
		}
		billRegister.setScheme(scheme);
	    }
	    if (billRegister.getSubScheme() != null) {
		SubSchemeContract subScheme = subSchemeContractRepository.findById(billRegister.getSubScheme());
		if (subScheme == null) {
		    throw new InvalidDataException("subScheme", "subScheme.invalid", " Invalid subScheme");
		}
		billRegister.setSubScheme(subScheme);
	    }
	    if (billRegister.getFunctionary() != null) {
		FunctionaryContract functionary = functionaryContractRepository.findById(billRegister.getFunctionary());
		if (functionary == null) {
		    throw new InvalidDataException("functionary", "functionary.invalid", " Invalid functionary");
		}
		billRegister.setFunctionary(functionary);
	    }
	    if (billRegister.getDivision() != null) {
		Boundary division = boundaryRepository.findById(billRegister.getDivision());
		if (division == null) {
		    throw new InvalidDataException("division", "division.invalid", " Invalid division");
		}
		billRegister.setDivision(division);
	    }
	    if (billRegister.getDepartment() != null) {
		Department department = departmentRepository.findById(billRegister.getDepartment());
		if (department == null) {
		    throw new InvalidDataException("department", "department.invalid", " Invalid department");
		}
		billRegister.setDepartment(department);
	    }
	    if (billRegister.getTenantId() != null)
		if (billRegister.getParent() != null && billRegister.getParent().getId() != null) {
		    billRegister.getParent().setTenantId(billRegister.getTenantId());
		    BillRegister parentId = billRegisterRepository.findById(billRegister.getParent());
		    if (parentId == null) {
			throw new InvalidDataException("parentId", ErrorCode.INVALID_REF_VALUE.getCode(),
				billRegister.getParent().getId());
		    }
		    billRegister.setParent(parentId);
		}
	}
	return billregisters;
    }

    public Pagination<BillRegister> search(BillRegisterSearch billRegisterSearch, BindingResult errors) {
	try {
	    List<BillRegister> billregisters = new ArrayList<>();
	    billregisters.add(billRegisterSearch);
	    validate(billregisters, Constants.ACTION_SEARCH, errors);
	    if (errors.hasErrors()) {
		throw new CustomBindException(errors);
	    }
	} catch (CustomBindException e) {
	    throw new CustomBindException(errors);
	}
	return billRegisterRepository.search(billRegisterSearch);
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