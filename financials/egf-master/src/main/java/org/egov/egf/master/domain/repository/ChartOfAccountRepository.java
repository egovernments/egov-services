package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.ChartOfAccount;
import org.egov.egf.master.domain.model.ChartOfAccountSearch;
import org.egov.egf.master.persistence.entity.ChartOfAccountEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.ChartOfAccountJdbcRepository;
import org.egov.egf.master.web.contract.ChartOfAccountContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChartOfAccountRepository {

	@Autowired
	private ChartOfAccountJdbcRepository chartOfAccountJdbcRepository;
	@Autowired
	private MastersQueueRepository chartOfAccountQueueRepository;

	public ChartOfAccount findById(ChartOfAccount chartOfAccount) {
		return chartOfAccountJdbcRepository.findById(new ChartOfAccountEntity().toEntity(chartOfAccount)).toDomain();

	}

	public ChartOfAccount save(ChartOfAccount chartOfAccount) {
		return chartOfAccountJdbcRepository.create(new ChartOfAccountEntity().toEntity(chartOfAccount)).toDomain();
	}

	public ChartOfAccount update(ChartOfAccount entity) {
		return chartOfAccountJdbcRepository.update(new ChartOfAccountEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<ChartOfAccountContract> request) {
		chartOfAccountQueueRepository.add(request);
	}

	public Pagination<ChartOfAccount> search(ChartOfAccountSearch domain) {

		return chartOfAccountJdbcRepository.search(domain);

	}

}