package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.AccountDetailType;
import org.egov.egf.master.domain.model.AccountDetailTypeSearch;
import org.egov.egf.master.persistence.entity.AccountDetailTypeEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.AccountDetailTypeJdbcRepository;
import org.egov.egf.master.web.contract.AccountDetailTypeContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailTypeRepository {

	@Autowired
	private AccountDetailTypeJdbcRepository accountDetailTypeJdbcRepository;
	@Autowired
	private MastersQueueRepository accountDetailTypeQueueRepository;

	public AccountDetailType findById(AccountDetailType accountDetailType) {
		return accountDetailTypeJdbcRepository.findById(new AccountDetailTypeEntity().toEntity(accountDetailType))
				.toDomain();

	}

	public AccountDetailType save(AccountDetailType accountDetailType) {
		return accountDetailTypeJdbcRepository.create(new AccountDetailTypeEntity().toEntity(accountDetailType))
				.toDomain();
	}

	public AccountDetailType update(AccountDetailType entity) {
		return accountDetailTypeJdbcRepository.update(new AccountDetailTypeEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<AccountDetailTypeContract> request) {
		accountDetailTypeQueueRepository.add(request);
	}

	public Pagination<AccountDetailType> search(AccountDetailTypeSearch domain) {

		return accountDetailTypeJdbcRepository.search(domain);

	}

}