package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.model.BankSearch;
import org.egov.egf.master.persistence.entity.BankEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.BankJdbcRepository;
import org.egov.egf.master.web.contract.BankContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankRepository {

	@Autowired
	private BankJdbcRepository bankJdbcRepository;
	@Autowired
	private MastersQueueRepository bankQueueRepository;

	public Bank findById(Bank bank) {
		return bankJdbcRepository.findById(new BankEntity().toEntity(bank)).toDomain();

	}

	public Bank save(Bank bank) {
		return bankJdbcRepository.create(new BankEntity().toEntity(bank)).toDomain();
	}

	public Bank update(Bank entity) {
		return bankJdbcRepository.update(new BankEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<BankContract> request) {
		bankQueueRepository.add(request);
	}

	public Pagination<Bank> search(BankSearch domain) {

		return bankJdbcRepository.search(domain);

	}

}