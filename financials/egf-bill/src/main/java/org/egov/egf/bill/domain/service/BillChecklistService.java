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
import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillChecklistSearch;
import org.egov.egf.bill.domain.repository.BillChecklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class BillChecklistService {

	@Autowired
	private BillChecklistRepository billChecklistRepository;

	@Autowired
	private SmartValidator validator;


	@Autowired
	public BillChecklistService(BillChecklistRepository billChecklistRepository, SmartValidator validator) {
		this.billChecklistRepository = billChecklistRepository;
		this.validator = validator;
	}
	
	@Transactional
	public List<BillChecklist> create(List<BillChecklist> billChecklists, BindingResult errors, RequestInfo requestInfo) {

		try {

			billChecklists = fetchRelated(billChecklists);
			validate(billChecklists, Constants.ACTION_CREATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}
			for (BillChecklist b : billChecklists) {
				b.setId(billChecklistRepository.getNextSequence());
			}

		} catch (CustomBindException e) {
			throw e;
		}

		return billChecklistRepository.save(billChecklists, requestInfo);

	}

	@Transactional
	public List<BillChecklist> update(List<BillChecklist> billChecklists, BindingResult errors, RequestInfo requestInfo) {

		try {

			billChecklists = fetchRelated(billChecklists);

			validate(billChecklists, Constants.ACTION_UPDATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}

		} catch (CustomBindException e) {
			throw e;
		}

		return billChecklistRepository.update(billChecklists, requestInfo);

	}

	private BindingResult validate(List<BillChecklist> billChecklists, String method, BindingResult errors) {

                try {
                    switch (method) {
                    case ACTION_VIEW:
                        // validator.validate(billChecklistContractRequest.getBillChecklist(), errors);
                        break;
                    case Constants.ACTION_CREATE:
                        if (billChecklists == null) {
                            throw new InvalidDataException("billChecklists", ErrorCode.NOT_NULL.getCode(), null);
                        }
                        for (BillChecklist billChecklist : billChecklists) {
                            validator.validate(billChecklist, errors);
                            if (!billChecklistRepository.uniqueCheck("checklistid", billChecklist)) {
                                errors.addError(new FieldError("billChecklist", "checklistid", billChecklist.getChecklist(), false,
                                        new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
                            }
                        }
                        break;
                    case Constants.ACTION_UPDATE:
                        if (billChecklists == null) {
                            throw new InvalidDataException("billChecklists", ErrorCode.NOT_NULL.getCode(), null);
                        }
                        for (BillChecklist billChecklist : billChecklists) {
                            if (billChecklist.getId() == null) {
                                throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(), billChecklist.getId());
                            }
                            validator.validate(billChecklist, errors);
                            if (!billChecklistRepository.uniqueCheck("checklistid", billChecklist)) {
                                errors.addError(new FieldError("billChecklist", "checklistid", billChecklist.getChecklist(), false,
                                        new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, null));
                            }
                        }
                        break;
                    case Constants.ACTION_SEARCH:
                        if (billChecklists == null) {
                            throw new InvalidDataException("billChecklists", ErrorCode.NOT_NULL.getCode(), null);
                        }
                        for (BillChecklist billChecklist : billChecklists) {
                            if (billChecklist.getTenantId() == null) {
                                throw new InvalidDataException("tenantId", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
                                        billChecklist.getTenantId());
                            }
                            if (!billChecklistRepository.uniqueCheck("checklistid", billChecklist)) {
                                errors.addError(new FieldError("billChecklist", "checklistid", billChecklist.getChecklist(), false,
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

	public List<BillChecklist> fetchRelated(List<BillChecklist> billChecklists) {
		return billChecklists;
	}

    public Pagination<BillChecklist> search(BillChecklistSearch billChecklistSearch, BindingResult errors) {
        
        try {
            
            List<BillChecklist> billChecklists = new ArrayList<>();
			if (billChecklistSearch != null){
				billChecklists.add(billChecklistSearch);
				validate(billChecklists, Constants.ACTION_SEARCH, errors);					
			}
			else
				validate(null, Constants.ACTION_SEARCH, errors);
			
            if (errors.hasErrors()) {
                throw new CustomBindException(errors);
            }
        
        } catch (CustomBindException e) {

            throw e;
        }

        return billChecklistRepository.search(billChecklistSearch);
    }

	@Transactional
	public BillChecklist save(BillChecklist billChecklist) {
		return billChecklistRepository.save(billChecklist);
	}

	@Transactional
	public BillChecklist update(BillChecklist billChecklist) {
		return billChecklistRepository.update(billChecklist);
	}

}