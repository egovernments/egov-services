package org.egov.egf.master.domain.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.AccountDetailType;
import org.egov.egf.master.domain.model.AccountDetailTypeSearch;
import org.egov.egf.master.persistence.entity.AccountDetailTypeEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.AccountDetailTypeJdbcRepository;
import org.egov.egf.master.web.requests.AccountDetailTypeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountDetailTypeRepository {

	@Autowired
	private AccountDetailTypeJdbcRepository accountDetailTypeJdbcRepository;
	@Autowired
	private MastersQueueRepository accountDetailTypeQueueRepository;

	public AccountDetailType findById(AccountDetailType accountDetailType) {
		AccountDetailTypeEntity entity = accountDetailTypeJdbcRepository
				.findById(new AccountDetailTypeEntity().toEntity(accountDetailType));
		return entity.toDomain();

	}

	@Transactional
	public AccountDetailType save(AccountDetailType accountDetailType) {
		AccountDetailTypeEntity entity = accountDetailTypeJdbcRepository
				.create(new AccountDetailTypeEntity().toEntity(accountDetailType));
		return entity.toDomain();
	}

	@Transactional
	public AccountDetailType update(AccountDetailType accountDetailType) {
		AccountDetailTypeEntity entity = accountDetailTypeJdbcRepository
				.update(new AccountDetailTypeEntity().toEntity(accountDetailType));
		return entity.toDomain();
	}

	public void add(AccountDetailTypeRequest request) {
		Map<String, Object> message = new HashMap<>();

		if (request.getRequestInfo().getAction().equalsIgnoreCase("create")) {
			message.put("accountdetailtype_create", request);
		} else {
			message.put("accountdetailtype_update", request);
		}
		accountDetailTypeQueueRepository.add(message);
	}

	public Pagination<AccountDetailType> search(AccountDetailTypeSearch domain) {

		return accountDetailTypeJdbcRepository.search(domain);

	}

}