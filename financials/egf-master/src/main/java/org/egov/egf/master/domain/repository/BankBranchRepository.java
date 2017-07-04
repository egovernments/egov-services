package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.BankBranch;
import org.egov.egf.master.domain.model.BankBranchSearch;
import org.egov.egf.master.persistence.entity.BankBranchEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.BankBranchJdbcRepository;
import org.egov.egf.master.web.contract.BankBranchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankBranchRepository {

	@Autowired
	private BankBranchJdbcRepository bankBranchJdbcRepository;
	@Autowired
	private MastersQueueRepository bankBranchQueueRepository;

	public BankBranch findById(BankBranch bankBranch) {
		return bankBranchJdbcRepository.findById(new BankBranchEntity().toEntity(bankBranch)).toDomain();

	}

	public BankBranch save(BankBranch bankBranch) {
		return bankBranchJdbcRepository.create(new BankBranchEntity().toEntity(bankBranch)).toDomain();
	}

	public BankBranch update(BankBranch entity) {
		return bankBranchJdbcRepository.update(new BankBranchEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<BankBranchContract> request) {
		bankBranchQueueRepository.add(request);
	}

	public Pagination<BankBranch> search(BankBranchSearch domain) {

		return bankBranchJdbcRepository.search(domain);

	}

}