package org.egov.egf.master.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.FinancialStatus;
import org.egov.egf.master.domain.model.FinancialStatusSearch;
import org.egov.egf.master.domain.repository.FinancialStatusRepository;
import org.egov.egf.master.web.requests.FinancialStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class FinancialStatusService {

	@Autowired
	private FinancialStatusRepository financialStatusRepository;

	@Autowired
	private SmartValidator validator;

	private BindingResult validate(List<FinancialStatus> financialstatuses, String method, BindingResult errors) {

		try {
			switch (method) {
			case Constants.ACTION_VIEW:
				// validator.validate(financialStatusContractRequest.getFinancialStatus(),
				// errors);
				break;
			case Constants.ACTION_CREATE:
				Assert.notNull(financialstatuses, "FinancialStatuses to create must not be null");
				for (FinancialStatus financialStatus : financialstatuses) {
					validator.validate(financialStatus, errors);
				}
				break;
			case Constants.ACTION_UPDATE:
				Assert.notNull(financialstatuses, "FinancialStatuses to update must not be null");
				for (FinancialStatus financialStatus : financialstatuses) {
				        Assert.notNull(financialStatus.getId(), "FinancialStatus ID to update must not be null");
					validator.validate(financialStatus, errors);
				}
				break;
                        case Constants.ACTION_SEARCH:
                                Assert.notNull(financialstatuses, "Financialstatuses to search must not be null");
                                for (FinancialStatus financialstatus : financialstatuses) {
                                        Assert.notNull(financialstatus.getTenantId(), "TenantID must not be null for search");
                                }
                                break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<FinancialStatus> fetchRelated(List<FinancialStatus> financialstatuses) {
		for (FinancialStatus financialStatus : financialstatuses) {
			// fetch related items

		}

		return financialstatuses;
	}

	@Transactional
	public List<FinancialStatus> add(List<FinancialStatus> financialstatuses, BindingResult errors) {
		financialstatuses = fetchRelated(financialstatuses);
		validate(financialstatuses, Constants.ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return financialstatuses;

	}

	@Transactional
	public List<FinancialStatus> update(List<FinancialStatus> financialstatuses, BindingResult errors) {
		financialstatuses = fetchRelated(financialstatuses);
		validate(financialstatuses, Constants.ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return financialstatuses;

	}

	public void addToQue(FinancialStatusRequest request) {
		financialStatusRepository.add(request);
	}

        public Pagination<FinancialStatus> search(FinancialStatusSearch financialStatusSearch, BindingResult errors) {
            
            try {
                
                List<FinancialStatus> financialStatuses = new ArrayList<>();
                financialStatuses.add(financialStatusSearch);
                validate(financialStatuses, Constants.ACTION_SEARCH, errors);
    
                if (errors.hasErrors()) {
                    throw new CustomBindException(errors);
                }
            
            } catch (CustomBindException e) {
    
                throw new CustomBindException(errors);
            }
    
            return financialStatusRepository.search(financialStatusSearch);
        }

	@Transactional
	public FinancialStatus save(FinancialStatus financialStatus) {
		return financialStatusRepository.save(financialStatus);
	}

	@Transactional
	public FinancialStatus update(FinancialStatus financialStatus) {
		return financialStatusRepository.update(financialStatus);
	}

}