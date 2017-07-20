package org.egov.egf.instrument.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.instrument.domain.model.InstrumentStatus;
import org.egov.egf.instrument.domain.model.InstrumentStatusSearch;
import org.egov.egf.instrument.persistence.entity.InstrumentStatusEntity;
import org.egov.egf.instrument.persistence.queue.MastersQueueRepository;
import org.egov.egf.instrument.persistence.repository.InstrumentStatusJdbcRepository;
import org.egov.egf.instrument.web.contract.InstrumentStatusContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstrumentStatusRepository {

	@Autowired
	private InstrumentStatusJdbcRepository instrumentStatusJdbcRepository;
	@Autowired
	private MastersQueueRepository instrumentStatusQueueRepository;

	public InstrumentStatus findById(InstrumentStatus instrumentStatus) {
		InstrumentStatusEntity entity = instrumentStatusJdbcRepository.findById(new InstrumentStatusEntity().toEntity(instrumentStatus));
		return entity.toDomain();

	}

	@Transactional
	public InstrumentStatus save(InstrumentStatus instrumentStatus) {
		InstrumentStatusEntity entity = instrumentStatusJdbcRepository.create(new InstrumentStatusEntity().toEntity(instrumentStatus));
		return entity.toDomain();
	}

	@Transactional
	public InstrumentStatus update(InstrumentStatus instrumentStatus) {
		InstrumentStatusEntity entity = instrumentStatusJdbcRepository.update(new InstrumentStatusEntity().toEntity(instrumentStatus));
		return entity.toDomain();
	}

	public void add(CommonRequest<InstrumentStatusContract> request) {
		instrumentStatusQueueRepository.add(request);
	}

	public Pagination<InstrumentStatus> search(InstrumentStatusSearch domain) {

		return instrumentStatusJdbcRepository.search(domain);

	}

}