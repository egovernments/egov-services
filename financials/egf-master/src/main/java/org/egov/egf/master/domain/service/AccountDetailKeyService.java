package org.egov.egf.master.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.ErrorCode;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.AccountDetailKey;
import org.egov.egf.master.domain.model.AccountDetailKeySearch;
import org.egov.egf.master.domain.model.AccountDetailType;
import org.egov.egf.master.domain.repository.AccountDetailKeyRepository;
import org.egov.egf.master.domain.repository.AccountDetailTypeRepository;
import org.egov.egf.master.web.requests.AccountDetailKeyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class AccountDetailKeyService {

	@Autowired
	private AccountDetailKeyRepository accountDetailKeyRepository;

	@Autowired
	private SmartValidator validator;
	@Autowired
	private AccountDetailTypeRepository accountDetailTypeRepository;

	private BindingResult validate(List<AccountDetailKey> accountdetailkeys, String method, BindingResult errors) {

                try {
                    switch (method) {
                    case Constants.ACTION_VIEW:
                        // validator.validate(accountDetailKeyContractRequest.getAccountDetailKey(),
                        // errors);
                        break;
                    case Constants.ACTION_CREATE:
                        if (accountdetailkeys == null) {
                            throw new InvalidDataException("accountdetailkeys", ErrorCode.NOT_NULL.getCode(),
                                    null);
                        }
                        for (AccountDetailKey accountDetailKey : accountdetailkeys) {
                            validator.validate(accountDetailKey, errors);
                        }
                        break;
                    case Constants.ACTION_UPDATE:
                        if (accountdetailkeys == null) {
                            throw new InvalidDataException("accountdetailkeys", ErrorCode.NOT_NULL.getCode(),
                                    null);
                        }
                        for (AccountDetailKey accountDetailKey : accountdetailkeys) {
                            if (accountDetailKey.getId() == null) {
                                throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
                                        accountDetailKey.getId());
                            }
                            validator.validate(accountDetailKey, errors);
                        }
                        break;
                    case Constants.ACTION_SEARCH:
                        if (accountdetailkeys == null) {
                            throw new InvalidDataException("accountdetailkeys", ErrorCode.NOT_NULL.getCode(),
                                    null);
                        }
                        for (AccountDetailKey accountdetailkey : accountdetailkeys) {
                            if (accountdetailkey.getId() == null) {
                                throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
                                        accountdetailkey.getId());
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

	public List<AccountDetailKey> fetchRelated(List<AccountDetailKey> accountdetailkeys) {
		for (AccountDetailKey accountDetailKey : accountdetailkeys) {
			// fetch related items
			if (accountDetailKey.getAccountDetailType() != null) {
				AccountDetailType accountDetailType = accountDetailTypeRepository
						.findById(accountDetailKey.getAccountDetailType());
				if (accountDetailType == null) {
					throw new InvalidDataException("accountDetailType", "accountDetailType.invalid",
							" Invalid accountDetailType");
				}
				accountDetailKey.setAccountDetailType(accountDetailType);
			}

		}

		return accountdetailkeys;
	}

	@Transactional
	public List<AccountDetailKey> add(List<AccountDetailKey> accountdetailkeys, BindingResult errors) {
		accountdetailkeys = fetchRelated(accountdetailkeys);
		validate(accountdetailkeys, Constants.ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		for(AccountDetailKey b:accountdetailkeys)b.setId(accountDetailKeyRepository.getNextSequence());
		return accountdetailkeys;

	}

	@Transactional
	public List<AccountDetailKey> update(List<AccountDetailKey> accountdetailkeys, BindingResult errors) {
		accountdetailkeys = fetchRelated(accountdetailkeys);
		validate(accountdetailkeys, Constants.ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return accountdetailkeys;

	}

	public void addToQue(AccountDetailKeyRequest request) {
		accountDetailKeyRepository.add(request);
	}

        public Pagination<AccountDetailKey> search(AccountDetailKeySearch accountDetailKeySearch, BindingResult errors) {
            
            try {
                
                List<AccountDetailKey> accountDetailKeys = new ArrayList<>();
                accountDetailKeys.add(accountDetailKeySearch);
                validate(accountDetailKeys, Constants.ACTION_SEARCH, errors);
    
                if (errors.hasErrors()) {
                    throw new CustomBindException(errors);
                }
            
            } catch (CustomBindException e) {
    
                throw new CustomBindException(errors);
            }
    
            return accountDetailKeyRepository.search(accountDetailKeySearch);
        }

	@Transactional
	public AccountDetailKey save(AccountDetailKey accountDetailKey) {
		return accountDetailKeyRepository.save(accountDetailKey);
	}

	@Transactional
	public AccountDetailKey update(AccountDetailKey accountDetailKey) {
		return accountDetailKeyRepository.update(accountDetailKey);
	}

}