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
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.model.BillDetailSearch;
import org.egov.egf.bill.domain.repository.BillDetailRepository;
import org.egov.egf.master.web.repository.ChartOfAccountContractRepository;
import org.egov.egf.master.web.repository.FunctionContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
public class BillDetailService {
    @Autowired
    private BillDetailRepository billDetailRepository;
    @Autowired
    private SmartValidator validator;
    @Autowired
    private FunctionContractRepository functionContractRepository;
    @Autowired
    private ChartOfAccountContractRepository chartOfAccountContractRepository;

    @Transactional
	public List<BillDetail> create(List<BillDetail> billdetails,
			BindingResult errors, RequestInfo requestInfo) {
		try {
			billdetails = fetchRelated(billdetails);
			validate(billdetails, Constants.ACTION_CREATE, errors);
			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}
			for (BillDetail b : billdetails) {
				b.setId(billDetailRepository.getNextSequence());
			}
		} catch (CustomBindException e) {
			throw e;
		}
		return billDetailRepository.save(billdetails, requestInfo);
	}

    @Transactional
    public List<BillDetail> update(List<BillDetail> billdetails, BindingResult errors, RequestInfo requestInfo) {
	try {
	    billdetails = fetchRelated(billdetails);
	    validate(billdetails, Constants.ACTION_UPDATE, errors);
	    if (errors.hasErrors()) {
		throw new CustomBindException(errors);
	    }
	} catch (CustomBindException e) {
	    throw new CustomBindException(errors);
	}
	return billDetailRepository.update(billdetails, requestInfo);
    }

    private BindingResult validate(List<BillDetail> billdetails, String method, BindingResult errors) {
	try {
	    switch (method) {
	    case ACTION_VIEW:
		break;
	    case Constants.ACTION_CREATE:
		if (billdetails == null) {
		    throw new InvalidDataException("billdetails", ErrorCode.NOT_NULL.getCode(), null);
		}
		for (BillDetail billDetail : billdetails) {
		    validator.validate(billDetail, errors);
		}
		break;
	    case Constants.ACTION_UPDATE:
		if (billdetails == null) {
		    throw new InvalidDataException("billdetails", ErrorCode.NOT_NULL.getCode(), null);
		}
		for (BillDetail billDetail : billdetails) {
		    if (billDetail.getId() == null) {
			throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
				billDetail.getId());
		    }
		    validator.validate(billDetail, errors);
		}
		break;
	    case Constants.ACTION_SEARCH:
		if (billdetails == null) {
		    throw new InvalidDataException("billdetails", ErrorCode.NOT_NULL.getCode(), null);
		}
		for (BillDetail billDetail : billdetails) {
		    if (billDetail.getTenantId() == null) {
			throw new InvalidDataException("tenantId", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
				billDetail.getTenantId());
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

    public List<BillDetail> fetchRelated(List<BillDetail> billdetails) {
	for (BillDetail billDetail : billdetails) {
	    // fetch related items
//	    if (billDetail.getFunction() != null) {
//		FunctionContract function = functionContractRepository.findById(billDetail.getFunction());
//		if (function == null) {
//		    throw new InvalidDataException("function", "function.invalid", " Invalid function");
//		}
//		billDetail.setFunction(function);
//	    }
//	    ChartOfAccountContract chartOfAccount = chartOfAccountContractRepository.findById(billDetail.getChartOfAccount());
//	    if(chartOfAccount == null) {
//	    	throw new InvalidDataException("chartOfAccount", "chartOfAccount.invalid", " Invalid chartOfAccount");
//	    }
	}
	return billdetails;
    }

    public Pagination<BillDetail> search(BillDetailSearch billDetailSearch, BindingResult errors) {
	try {
	    List<BillDetail> billdetails = new ArrayList<>();
	    billdetails.add(billDetailSearch);
	    validate(billdetails, Constants.ACTION_SEARCH, errors);
	    if (errors.hasErrors()) {
		throw new CustomBindException(errors);
	    }
	} catch (CustomBindException e) {
	    throw new CustomBindException(errors);
	}
	return billDetailRepository.search(billDetailSearch);
    }

    @Transactional
    public BillDetail save(BillDetail billDetail) {
	return billDetailRepository.save(billDetail);
    }

    @Transactional
    public BillDetail update(BillDetail billDetail) {
	return billDetailRepository.update(billDetail);
    }
}
