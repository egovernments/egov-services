package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.AccountDetailKey;
import org.egov.egf.master.domain.model.AccountDetailKeySearch;
import org.egov.egf.master.persistence.entity.AccountDetailKeyEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.AccountDetailKeyJdbcRepository;
import org.egov.egf.master.web.contract.AccountDetailKeyContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailKeyRepository {

	@Autowired
	private AccountDetailKeyJdbcRepository accountDetailKeyJdbcRepository;
	@Autowired
	private MastersQueueRepository accountDetailKeyQueueRepository;

	public AccountDetailKey findById(AccountDetailKey accountDetailKey) {
		return accountDetailKeyJdbcRepository.findById(new AccountDetailKeyEntity().toEntity(accountDetailKey))
				.toDomain();

	}

	public AccountDetailKey save(AccountDetailKey accountDetailKey) {
		return accountDetailKeyJdbcRepository.create(new AccountDetailKeyEntity().toEntity(accountDetailKey))
				.toDomain();
	}

	public AccountDetailKey update(AccountDetailKey entity) {
		return accountDetailKeyJdbcRepository.update(new AccountDetailKeyEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<AccountDetailKeyContract> request) {
		accountDetailKeyQueueRepository.add(request);
	}

	public Pagination<AccountDetailKey> search(AccountDetailKeySearch domain) {

		return accountDetailKeyJdbcRepository.search(domain);

	}

}