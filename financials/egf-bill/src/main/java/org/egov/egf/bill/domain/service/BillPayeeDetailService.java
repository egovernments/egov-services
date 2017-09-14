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
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.domain.model.BillPayeeDetailSearch;
import org.egov.egf.bill.domain.repository.BillPayeeDetailRepository;
import org.egov.egf.bill.domain.repository.BoundaryRepository;
import org.egov.egf.bill.domain.repository.DepartmentRepository;
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
public class BillPayeeDetailService {
    @Autowired
    private BillPayeeDetailRepository billPayeeDetailRepository;
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
    
	@Autowired
	public BillPayeeDetailService(BillPayeeDetailRepository billPayeeDetailRepository, SmartValidator validator) {
		this.billPayeeDetailRepository = billPayeeDetailRepository;
		this.validator = validator;
	}

    @Transactional
	public List<BillPayeeDetail> create(List<BillPayeeDetail> billpayeedetails,
			BindingResult errors, RequestInfo requestInfo) {
		try {
			billpayeedetails = fetchRelated(billpayeedetails);
			validate(billpayeedetails, Constants.ACTION_CREATE, errors);
			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}
			for (BillPayeeDetail b : billpayeedetails) {
				b.setId(billPayeeDetailRepository.getNextSequence());
			}
		} catch (CustomBindException e) {
			throw e;
		}
		return billPayeeDetailRepository.save(billpayeedetails, requestInfo);
	}

    @Transactional
    public List<BillPayeeDetail> update(List<BillPayeeDetail> billpayeedetails, BindingResult errors, RequestInfo requestInfo) {
	try {
	    billpayeedetails = fetchRelated(billpayeedetails);
	    validate(billpayeedetails, Constants.ACTION_UPDATE, errors);
	    if (errors.hasErrors()) {
		throw new CustomBindException(errors);
	    }
	    for (BillPayeeDetail b : billpayeedetails) {
	    }
	} catch (CustomBindException e) {
	    throw new CustomBindException(errors);
	}
	return billPayeeDetailRepository.update(billpayeedetails, requestInfo);
    }

    private BindingResult validate(List<BillPayeeDetail> billpayeedetails, String method, BindingResult errors) {
	try {
	    switch (method) {
	    case ACTION_VIEW:
		// validator.validate(billPayeeDetailContractRequest.getBillPayeeDetail(),
		// errors);
		break;
	    case Constants.ACTION_CREATE:
		if (billpayeedetails == null) {
		    throw new InvalidDataException("billpayeedetails", ErrorCode.NOT_NULL.getCode(), null);
		}
		for (BillPayeeDetail billPayeeDetail : billpayeedetails) {
		    validator.validate(billPayeeDetail, errors);
		}
		break;
	    case Constants.ACTION_UPDATE:
		if (billpayeedetails == null) {
		    throw new InvalidDataException("billpayeedetails", ErrorCode.NOT_NULL.getCode(), null);
		}
		for (BillPayeeDetail billPayeeDetail : billpayeedetails) {
		    if (billPayeeDetail.getId() == null) {
			throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
				billPayeeDetail.getId());
		    }
		    validator.validate(billPayeeDetail, errors);
		}
		break;
	    case Constants.ACTION_SEARCH:
		if (billpayeedetails == null) {
		    throw new InvalidDataException("billpayeedetails", ErrorCode.NOT_NULL.getCode(), null);
		}
		for (BillPayeeDetail billPayeeDetail : billpayeedetails) {
		    if (billPayeeDetail.getTenantId() == null) {
			throw new InvalidDataException("tenantId", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
				billPayeeDetail.getTenantId());
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

	public List<BillPayeeDetail> fetchRelated(List<BillPayeeDetail> billpayeedetails) {
		if (null != billpayeedetails)
			for (BillPayeeDetail billPayeeDetail : billpayeedetails) {
				// fetch related items
			}
		return billpayeedetails;
	}

    public Pagination<BillPayeeDetail> search(BillPayeeDetailSearch billPayeeDetailSearch, BindingResult errors) {
	try {
	    List<BillPayeeDetail> billpayeedetails = new ArrayList<>();
	    billpayeedetails.add(billPayeeDetailSearch);
	    validate(billpayeedetails, Constants.ACTION_SEARCH, errors);
	    if (errors.hasErrors()) {
		throw new CustomBindException(errors);
	    }
	} catch (CustomBindException e) {
	    throw new CustomBindException(errors);
	}
	return billPayeeDetailRepository.search(billPayeeDetailSearch);
    }

    @Transactional
    public BillPayeeDetail save(BillPayeeDetail billPayeeDetail) {
	return billPayeeDetailRepository.save(billPayeeDetail);
    }

    @Transactional
    public BillPayeeDetail update(BillPayeeDetail billPayeeDetail) {
	return billPayeeDetailRepository.update(billPayeeDetail);
    }
}