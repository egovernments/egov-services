package org.egov.egf.bill.domain.service;

import static org.egov.common.constants.Constants.ACTION_VIEW;

import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.ErrorCode;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.repository.BillDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
public class BillDetailService {
    @Autowired
    private BillDetailRepository billDetailRepository;
    @Autowired
    private SmartValidator validator;

	@Autowired
	public BillDetailService(BillDetailRepository billDetailRepository, SmartValidator validator) {
		this.billDetailRepository = billDetailRepository;
		this.validator = validator;
	}
    
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
	    throw e;
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
		    if (!billDetailRepository.uniqueCheck("id", billDetail)) {
                errors.addError(new FieldError("billDetail", "id", billDetail.getId(), false,
                        new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
            }
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
		    if (!billDetailRepository.uniqueCheck("id", billDetail)) {
                errors.addError(new FieldError("billDetail", "id", billDetail.getId(), false,
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

	public List<BillDetail> fetchRelated(List<BillDetail> billdetails) {
		return billdetails;
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
