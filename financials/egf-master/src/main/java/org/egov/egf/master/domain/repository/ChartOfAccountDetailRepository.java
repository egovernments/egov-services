package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.ChartOfAccountDetail;
import org.egov.egf.master.domain.model.ChartOfAccountDetailSearch;
import org.egov.egf.master.persistence.entity.ChartOfAccountDetailEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.ChartOfAccountDetailJdbcRepository;
import org.egov.egf.master.web.contract.ChartOfAccountDetailContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChartOfAccountDetailRepository {

	@Autowired
	private ChartOfAccountDetailJdbcRepository chartOfAccountDetailJdbcRepository;
	@Autowired
	private MastersQueueRepository chartOfAccountDetailQueueRepository;

	public ChartOfAccountDetail findById(ChartOfAccountDetail chartOfAccountDetail) {
		return chartOfAccountDetailJdbcRepository
				.findById(new ChartOfAccountDetailEntity().toEntity(chartOfAccountDetail)).toDomain();

	}

	public ChartOfAccountDetail save(ChartOfAccountDetail chartOfAccountDetail) {
		return chartOfAccountDetailJdbcRepository
				.create(new ChartOfAccountDetailEntity().toEntity(chartOfAccountDetail)).toDomain();
	}

	public ChartOfAccountDetail update(ChartOfAccountDetail entity) {
		return chartOfAccountDetailJdbcRepository.update(new ChartOfAccountDetailEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<ChartOfAccountDetailContract> request) {
		chartOfAccountDetailQueueRepository.add(request);
	}

	public Pagination<ChartOfAccountDetail> search(ChartOfAccountDetailSearch domain) {

		return chartOfAccountDetailJdbcRepository.search(domain);

	}

}