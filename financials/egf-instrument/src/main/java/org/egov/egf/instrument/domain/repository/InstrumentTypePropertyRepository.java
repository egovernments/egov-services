package org.egov.egf.instrument.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.instrument.domain.model.InstrumentTypeProperty;
import org.egov.egf.instrument.persistence.entity.InstrumentTypePropertyEntity;
import org.egov.egf.instrument.persistence.queue.MastersQueueRepository;
import org.egov.egf.instrument.persistence.repository.InstrumentTypePropertyJdbcRepository;
import org.egov.egf.instrument.web.contract.InstrumentTypePropertyContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstrumentTypePropertyRepository {

	@Autowired
	private InstrumentTypePropertyJdbcRepository instrumentTypePropertyJdbcRepository;
	@Autowired
	private MastersQueueRepository instrumentTypePropertyQueueRepository;

	public InstrumentTypeProperty findById(InstrumentTypeProperty instrumentTypeProperty) {
		InstrumentTypePropertyEntity entity = instrumentTypePropertyJdbcRepository
				.findById(new InstrumentTypePropertyEntity().toEntity(instrumentTypeProperty));
		return entity.toDomain();

	}

	@Transactional
	public InstrumentTypeProperty save(InstrumentTypeProperty instrumentTypeProperty) {
		InstrumentTypePropertyEntity entity = instrumentTypePropertyJdbcRepository
				.create(new InstrumentTypePropertyEntity().toEntity(instrumentTypeProperty));
		return entity.toDomain();
	}

	@Transactional
	public InstrumentTypeProperty update(InstrumentTypeProperty instrumentTypeProperty) {
		InstrumentTypePropertyEntity entity = instrumentTypePropertyJdbcRepository
				.update(new InstrumentTypePropertyEntity().toEntity(instrumentTypeProperty));
		return entity.toDomain();
	}

	public void add(CommonRequest<InstrumentTypePropertyContract> request) {
		instrumentTypePropertyQueueRepository.add(request);
	}

	/*public Pagination<InstrumentTypeProperty> search(InstrumentTypePropertySearch domain) {

		return instrumentTypePropertyJdbcRepository.search(domain);

	}*/

}