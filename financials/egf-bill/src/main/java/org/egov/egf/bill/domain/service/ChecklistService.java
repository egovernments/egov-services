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
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.model.ChecklistSearch;
import org.egov.egf.bill.domain.repository.ChecklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class ChecklistService {

	@Autowired
	private ChecklistRepository checklistRepository;

	@Autowired
	private SmartValidator validator;


	@Autowired
	public ChecklistService(ChecklistRepository checklistRepository, SmartValidator validator) {
		this.checklistRepository = checklistRepository;
		this.validator = validator;
	}
	
	@Transactional
	public List<Checklist> create(List<Checklist> checklists, BindingResult errors, RequestInfo requestInfo) {

		try {

			checklists = fetchRelated(checklists);
			validate(checklists, Constants.ACTION_CREATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}
			for (Checklist b : checklists) {
				b.setId(checklistRepository.getNextSequence());
			}

		} catch (CustomBindException e) {
			throw e;
		}

		return checklistRepository.save(checklists, requestInfo);

	}

	@Transactional
	public List<Checklist> update(List<Checklist> checklists, BindingResult errors, RequestInfo requestInfo) {

		try {

			checklists = fetchRelated(checklists);

			validate(checklists, Constants.ACTION_UPDATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return checklistRepository.update(checklists, requestInfo);

	}

	private BindingResult validate(List<Checklist> checklists, String method, BindingResult errors) {

                try {
                    switch (method) {
                    case ACTION_VIEW:
                        // validator.validate(checklistContractRequest.getChecklist(), errors);
                        break;
                    case Constants.ACTION_CREATE:
                        if (checklists == null) {
                            throw new InvalidDataException("checklists", ErrorCode.NOT_NULL.getCode(), null);
                        }
                        for (Checklist checklist : checklists) {
                            validator.validate(checklist, errors);
                            if (!checklistRepository.uniqueCheck("subtype", "key", checklist)) {
                                errors.addError(new FieldError("checklist", "subtype", checklist.getSubType(), false,
                                        new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, "subtype and key combination is not unique"));
                            }
                        }
                        break;
                    case Constants.ACTION_UPDATE:
                        if (checklists == null) {
                            throw new InvalidDataException("checklists", ErrorCode.NOT_NULL.getCode(), null);
                        }
                        for (Checklist checklist : checklists) {
                            if (checklist.getId() == null) {
                                throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(), checklist.getId());
                            }
                            validator.validate(checklist, errors);
                            if (!checklistRepository.uniqueCheck("subtype", "key", checklist)) {
                                errors.addError(new FieldError("checklist", "subtype", checklist.getSubType(), false,
                                        new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, "subtype and key combination is not unique"));
                            }
                        }
                        break;
                    case Constants.ACTION_SEARCH:
                        if (checklists == null) {
                            throw new InvalidDataException("checklists", ErrorCode.NOT_NULL.getCode(), null);
                        }
                        for (Checklist checklist : checklists) {
                            if (checklist.getTenantId() == null) {
                                throw new InvalidDataException("tenantId", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
                                        checklist.getTenantId());
                            }
                            if (!checklistRepository.uniqueCheck("subtype", "key", checklist)) {
                                errors.addError(new FieldError("checklist", "subtype", checklist.getSubType(), false,
                                        new String[] { ErrorCode.NON_UNIQUE_VALUE.getCode() }, null, "subtype and key combination is not unique"));
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

	public List<Checklist> fetchRelated(List<Checklist> checklists) {
		return checklists;
	}

        public Pagination<Checklist> search(ChecklistSearch checklistSearch, BindingResult errors) {
            
            try {
                
                List<Checklist> checklists = new ArrayList<>();
				if (checklistSearch != null){
					checklists.add(checklistSearch);
					validate(checklists, Constants.ACTION_SEARCH, errors);					
				}
				else
					validate(null, Constants.ACTION_SEARCH, errors);
    
                if (errors.hasErrors()) {
                    throw new CustomBindException(errors);
                }
            
            } catch (CustomBindException e) {
    
                throw e;
            }
    
            return checklistRepository.search(checklistSearch);
        }

	@Transactional
	public Checklist save(Checklist checklist) {
		return checklistRepository.save(checklist);
	}

	@Transactional
	public Checklist update(Checklist checklist) {
		return checklistRepository.update(checklist);
	}

}