package org.egov.egf.master.domain.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.AccountCodePurpose;
import org.egov.egf.master.domain.model.AccountCodePurposeSearch;
import org.egov.egf.master.persistence.entity.AccountCodePurposeEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.AccountCodePurposeJdbcRepository;
import org.egov.egf.master.web.requests.AccountCodePurposeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountCodePurposeRepository {

	@Autowired
	private AccountCodePurposeJdbcRepository accountCodePurposeJdbcRepository;
	@Autowired
	private MastersQueueRepository accountCodePurposeQueueRepository;

	public AccountCodePurpose findById(AccountCodePurpose accountCodePurpose) {
		AccountCodePurposeEntity entity = accountCodePurposeJdbcRepository
				.findById(new AccountCodePurposeEntity().toEntity(accountCodePurpose));
		return entity.toDomain();

	}

	@Transactional
	public AccountCodePurpose save(AccountCodePurpose accountCodePurpose) {
		AccountCodePurposeEntity entity = accountCodePurposeJdbcRepository
				.create(new AccountCodePurposeEntity().toEntity(accountCodePurpose));
		return entity.toDomain();
	}

	@Transactional
	public AccountCodePurpose update(AccountCodePurpose accountCodePurpose) {
		AccountCodePurposeEntity entity = accountCodePurposeJdbcRepository
				.update(new AccountCodePurposeEntity().toEntity(accountCodePurpose));
		return entity.toDomain();
	}

	public void add(AccountCodePurposeRequest request) {
		Map<String, Object> message = new HashMap<>();

		if (request.getRequestInfo().getAction().equalsIgnoreCase("create")) {
			message.put("accountcodepurpose_create", request);
		} else {
			message.put("accountcodepurpose_update", request);
		}
		accountCodePurposeQueueRepository.add(message);
	}

	public Pagination<AccountCodePurpose> search(AccountCodePurposeSearch domain) {

		return accountCodePurposeJdbcRepository.search(domain);

	}

}