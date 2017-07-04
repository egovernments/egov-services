package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.AccountCodePurpose;
import org.egov.egf.master.domain.model.AccountCodePurposeSearch;
import org.egov.egf.master.persistence.entity.AccountCodePurposeEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.AccountCodePurposeJdbcRepository;
import org.egov.egf.master.web.contract.AccountCodePurposeContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountCodePurposeRepository {

	@Autowired
	private AccountCodePurposeJdbcRepository accountCodePurposeJdbcRepository;
	@Autowired
	private MastersQueueRepository accountCodePurposeQueueRepository;

	public AccountCodePurpose findById(AccountCodePurpose accountCodePurpose) {
		return accountCodePurposeJdbcRepository.findById(new AccountCodePurposeEntity().toEntity(accountCodePurpose))
				.toDomain();

	}

	public AccountCodePurpose save(AccountCodePurpose accountCodePurpose) {
		return accountCodePurposeJdbcRepository.create(new AccountCodePurposeEntity().toEntity(accountCodePurpose))
				.toDomain();
	}

	public AccountCodePurpose update(AccountCodePurpose entity) {
		return accountCodePurposeJdbcRepository.update(new AccountCodePurposeEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<AccountCodePurposeContract> request) {
		accountCodePurposeQueueRepository.add(request);
	}

	public Pagination<AccountCodePurpose> search(AccountCodePurposeSearch domain) {

		return accountCodePurposeJdbcRepository.search(domain);

	}

}