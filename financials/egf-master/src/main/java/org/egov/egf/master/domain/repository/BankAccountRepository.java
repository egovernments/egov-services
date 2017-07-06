package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.BankAccount;
import org.egov.egf.master.domain.model.BankAccountSearch;
import org.egov.egf.master.persistence.entity.BankAccountEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.BankAccountJdbcRepository;
import org.egov.egf.master.web.contract.BankAccountContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountRepository {

	@Autowired
	private BankAccountJdbcRepository bankAccountJdbcRepository;
	@Autowired
	private MastersQueueRepository bankAccountQueueRepository;

	public BankAccount findById(BankAccount bankAccount) {
		return bankAccountJdbcRepository.findById(new BankAccountEntity().toEntity(bankAccount)).toDomain();

	}

	public BankAccount save(BankAccount bankAccount) {
		return bankAccountJdbcRepository.create(new BankAccountEntity().toEntity(bankAccount)).toDomain();
	}

	public BankAccount update(BankAccount entity) {
		return bankAccountJdbcRepository.update(new BankAccountEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<BankAccountContract> request) {
		bankAccountQueueRepository.add(request);
	}

	public Pagination<BankAccount> search(BankAccountSearch domain) {

		return bankAccountJdbcRepository.search(domain);

	}

}