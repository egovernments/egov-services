package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.AccountEntity;
import org.egov.egf.master.domain.model.AccountEntitySearch;
import org.egov.egf.master.persistence.entity.AccountEntityEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.AccountEntityJdbcRepository;
import org.egov.egf.master.web.contract.AccountEntityContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountEntityRepository {

	@Autowired
	private AccountEntityJdbcRepository accountEntityJdbcRepository;
	@Autowired
	private MastersQueueRepository accountEntityQueueRepository;

	public AccountEntity findById(AccountEntity accountEntity) {
		return accountEntityJdbcRepository.findById(new AccountEntityEntity().toEntity(accountEntity)).toDomain();

	}

	public AccountEntity save(AccountEntity accountEntity) {
		return accountEntityJdbcRepository.create(new AccountEntityEntity().toEntity(accountEntity)).toDomain();
	}

	public AccountEntity update(AccountEntity entity) {
		return accountEntityJdbcRepository.update(new AccountEntityEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<AccountEntityContract> request) {
		accountEntityQueueRepository.add(request);
	}

	public Pagination<AccountEntity> search(AccountEntitySearch domain) {

		return accountEntityJdbcRepository.search(domain);

	}

}