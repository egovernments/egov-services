package org.egov.egf.bill.domain.service;

import static org.egov.common.constants.Constants.ACTION_VIEW;

import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.ErrorCode;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.domain.repository.BillPayeeDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
		    if (!billPayeeDetailRepository.uniqueCheck("id", billPayeeDetail)) {
                errors.addError(new FieldError("billPayeeDetail", "id", billPayeeDetail.getId(), false,
                        new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
            }
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
		    if (!billPayeeDetailRepository.uniqueCheck("id", billPayeeDetail)) {
                errors.addError(new FieldError("billPayeeDetail", "id", billPayeeDetail.getId(), false,
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

	public List<BillPayeeDetail> fetchRelated(List<BillPayeeDetail> billpayeedetails) {
		return billpayeedetails;
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