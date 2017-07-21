package org.egov.egf.instrument.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.instrument.domain.model.SurrenderReason;
import org.egov.egf.instrument.domain.model.SurrenderReasonSearch;
import org.egov.egf.instrument.persistence.entity.SurrenderReasonEntity;
import org.egov.egf.instrument.persistence.queue.MastersQueueRepository;
import org.egov.egf.instrument.persistence.repository.SurrenderReasonJdbcRepository;
import org.egov.egf.instrument.web.contract.SurrenderReasonContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SurrenderReasonRepository {

	@Autowired
	private SurrenderReasonJdbcRepository surrenderReasonJdbcRepository;
	@Autowired
	private MastersQueueRepository surrenderReasonQueueRepository;

	public SurrenderReason findById(SurrenderReason surrenderReason) {
		SurrenderReasonEntity entity = surrenderReasonJdbcRepository.findById(new SurrenderReasonEntity().toEntity(surrenderReason));
		return entity.toDomain();

	}

	@Transactional
	public SurrenderReason save(SurrenderReason surrenderReason) {
		SurrenderReasonEntity entity = surrenderReasonJdbcRepository.create(new SurrenderReasonEntity().toEntity(surrenderReason));
		return entity.toDomain();
	}

	@Transactional
	public SurrenderReason update(SurrenderReason surrenderReason) {
		SurrenderReasonEntity entity = surrenderReasonJdbcRepository.update(new SurrenderReasonEntity().toEntity(surrenderReason));
		return entity.toDomain();
	}

	public void add(CommonRequest<SurrenderReasonContract> request) {
		surrenderReasonQueueRepository.add(request);
	}

	public Pagination<SurrenderReason> search(SurrenderReasonSearch domain) {

		return surrenderReasonJdbcRepository.search(domain);

	}

}