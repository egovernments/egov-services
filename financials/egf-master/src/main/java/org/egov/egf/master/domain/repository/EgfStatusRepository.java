package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.EgfStatus;
import org.egov.egf.master.domain.model.EgfStatusSearch;
import org.egov.egf.master.persistence.entity.EgfStatusEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.EgfStatusJdbcRepository;
import org.egov.egf.master.web.contract.EgfStatusContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EgfStatusRepository {

	@Autowired
	private EgfStatusJdbcRepository egfStatusJdbcRepository;
	@Autowired
	private MastersQueueRepository egfStatusQueueRepository;

	public EgfStatus findById(EgfStatus egfStatus) {
		return egfStatusJdbcRepository.findById(new EgfStatusEntity().toEntity(egfStatus)).toDomain();

	}

	public EgfStatus save(EgfStatus egfStatus) {
		return egfStatusJdbcRepository.create(new EgfStatusEntity().toEntity(egfStatus)).toDomain();
	}

	public EgfStatus update(EgfStatus entity) {
		return egfStatusJdbcRepository.update(new EgfStatusEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<EgfStatusContract> request) {
		egfStatusQueueRepository.add(request);
	}

	public Pagination<EgfStatus> search(EgfStatusSearch domain) {

		return egfStatusJdbcRepository.search(domain);

	}

}