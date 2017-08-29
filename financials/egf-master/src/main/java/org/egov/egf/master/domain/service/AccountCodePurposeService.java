package org.egov.egf.master.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.AccountCodePurpose;
import org.egov.egf.master.domain.model.AccountCodePurposeSearch;
import org.egov.egf.master.domain.repository.AccountCodePurposeRepository;
import org.egov.egf.master.web.requests.AccountCodePurposeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class AccountCodePurposeService {

	@Autowired
	private AccountCodePurposeRepository accountCodePurposeRepository;

	@Autowired
	private SmartValidator validator;

	private BindingResult validate(List<AccountCodePurpose> accountcodepurposes, String method, BindingResult errors) {

		try {
			switch (method) {
			case Constants.ACTION_VIEW:
				// validator.validate(accountCodePurposeContractRequest.getAccountCodePurpose(),
				// errors);
				break;
			case Constants.ACTION_CREATE:
				Assert.notNull(accountcodepurposes, "AccountCodePurposes to create must not be null");
				for (AccountCodePurpose accountCodePurpose : accountcodepurposes) {
					validator.validate(accountCodePurpose, errors);
				}
				break;
			case Constants.ACTION_UPDATE:
				Assert.notNull(accountcodepurposes, "AccountCodePurposes to update must not be null");
				for (AccountCodePurpose accountCodePurpose : accountcodepurposes) {
				        Assert.notNull(accountCodePurpose.getId(), "Account Code Purpose ID to update must not be null");
					validator.validate(accountCodePurpose, errors);
				}
				break;
                        case Constants.ACTION_SEARCH:
                            Assert.notNull(accountcodepurposes, "Accountcodepurposes to search must not be null");
                            for (AccountCodePurpose accountcodepurpose : accountcodepurposes) {
                                    Assert.notNull(accountcodepurpose.getTenantId(), "TenantID must not be null for search");
                            }
                                break;                              
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<AccountCodePurpose> fetchRelated(List<AccountCodePurpose> accountcodepurposes) {
		for (AccountCodePurpose accountCodePurpose : accountcodepurposes) {
			// fetch related items

		}

		return accountcodepurposes;
	}

	@Transactional
	public List<AccountCodePurpose> add(List<AccountCodePurpose> accountcodepurposes, BindingResult errors) {
		accountcodepurposes = fetchRelated(accountcodepurposes);
		validate(accountcodepurposes, Constants.ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		for(AccountCodePurpose b:accountcodepurposes)b.setId(accountCodePurposeRepository.getNextSequence());
		return accountcodepurposes;

	}

	@Transactional
	public List<AccountCodePurpose> update(List<AccountCodePurpose> accountcodepurposes, BindingResult errors) {
		accountcodepurposes = fetchRelated(accountcodepurposes);
		validate(accountcodepurposes, Constants.ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return accountcodepurposes;

	}

	public void addToQue(AccountCodePurposeRequest request) {
		accountCodePurposeRepository.add(request);
	}

        public Pagination<AccountCodePurpose> search(AccountCodePurposeSearch accountCodePurposeSearch, BindingResult errors) {
            
            try {
                
                List<AccountCodePurpose> accountCodePurposes = new ArrayList<>();
                accountCodePurposes.add(accountCodePurposeSearch);
                validate(accountCodePurposes, Constants.ACTION_SEARCH, errors);
    
                if (errors.hasErrors()) {
                    throw new CustomBindException(errors);
                }
            
            } catch (CustomBindException e) {
    
                throw new CustomBindException(errors);
            }
    
            return accountCodePurposeRepository.search(accountCodePurposeSearch);
        }

	@Transactional
	public AccountCodePurpose save(AccountCodePurpose accountCodePurpose) {
		return accountCodePurposeRepository.save(accountCodePurpose);
	}

	@Transactional
	public AccountCodePurpose update(AccountCodePurpose accountCodePurpose) {
		return accountCodePurposeRepository.update(accountCodePurpose);
	}

}