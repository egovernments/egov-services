package org.egov.egf.master.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.FinancialYear;
import org.egov.egf.master.domain.model.FinancialYearSearch;
import org.egov.egf.master.domain.repository.FinancialYearRepository;
import org.egov.egf.master.web.requests.FinancialYearRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class FinancialYearService {

	@Autowired
	private FinancialYearRepository financialYearRepository;

	@Autowired
	private SmartValidator validator;

	public BindingResult validate(List<FinancialYear> financialyears, String method, BindingResult errors) {

		try {
			switch (method) {
			case Constants.ACTION_VIEW:
				// validator.validate(financialYearContractRequest.getFinancialYear(),
				// errors);
				break;
			case Constants.ACTION_CREATE:
				Assert.notNull(financialyears, "FinancialYears to create must not be null");
				for (FinancialYear financialYear : financialyears) {
					validator.validate(financialYear, errors);
				}
				break;
			case Constants.ACTION_UPDATE:
				Assert.notNull(financialyears, "FinancialYears to update must not be null");
				for (FinancialYear financialYear : financialyears) {
				        Assert.notNull(financialYear.getId(), "FinancialYear ID to update must not be null");
					validator.validate(financialYear, errors);
				}
				break;
                        case Constants.ACTION_SEARCH:
                                Assert.notNull(financialyears, "Financialyears to search must not be null");
                                for (FinancialYear financialyear : financialyears) {
                                        Assert.notNull(financialyear.getTenantId(), "TenantID must not be null for search");
                                }
                                break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<FinancialYear> fetchRelated(List<FinancialYear> financialyears) {
		for (FinancialYear financialYear : financialyears) {
			// fetch related items

		}

		return financialyears;
	}

	@Transactional
	public List<FinancialYear> add(List<FinancialYear> financialyears, BindingResult errors) {
		financialyears = fetchRelated(financialyears);
		validate(financialyears, Constants.ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		for(FinancialYear b:financialyears)b.setId(financialYearRepository.getNextSequence());
		return financialyears;

	}

	public List<FinancialYear> update(List<FinancialYear> financialyears, BindingResult errors) {
		financialyears = fetchRelated(financialyears);
		validate(financialyears, Constants.ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return financialyears;

	}

	public void addToQue(FinancialYearRequest request) {
		financialYearRepository.add(request);
	}

        public Pagination<FinancialYear> search(FinancialYearSearch financialYearSearch, BindingResult errors) {
            
            try {
                
                List<FinancialYear> financialYears = new ArrayList<>();
                financialYears.add(financialYearSearch);
                validate(financialYears, Constants.ACTION_SEARCH, errors);
    
                if (errors.hasErrors()) {
                    throw new CustomBindException(errors);
                }
            
            } catch (CustomBindException e) {
    
                throw new CustomBindException(errors);
            }
    
            return financialYearRepository.search(financialYearSearch);
        }

	@Transactional
	public FinancialYear save(FinancialYear financialYear) {
		return financialYearRepository.save(financialYear);
	}

	@Transactional
	public FinancialYear update(FinancialYear financialYear) {
		return financialYearRepository.update(financialYear);
	}
}