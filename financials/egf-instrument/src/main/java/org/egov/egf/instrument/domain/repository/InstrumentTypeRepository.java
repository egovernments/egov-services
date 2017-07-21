package org.egov.egf.instrument.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.instrument.domain.model.InstrumentTypeSearch;
import org.egov.egf.instrument.persistence.entity.InstrumentTypeEntity;
import org.egov.egf.instrument.persistence.queue.MastersQueueRepository;
import org.egov.egf.instrument.persistence.repository.InstrumentTypeJdbcRepository;
import org.egov.egf.instrument.web.contract.InstrumentTypeContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstrumentTypeRepository {

	@Autowired
	private InstrumentTypeJdbcRepository instrumentTypeJdbcRepository;
	@Autowired
	private MastersQueueRepository instrumentTypeQueueRepository;

	public InstrumentType findById(InstrumentType instrumentType) {
		InstrumentTypeEntity entity = instrumentTypeJdbcRepository.findById(new InstrumentTypeEntity().toEntity(instrumentType));
		return entity.toDomain();

	}

	@Transactional
	public InstrumentType save(InstrumentType instrumentType) {
		InstrumentTypeEntity entity = instrumentTypeJdbcRepository.create(new InstrumentTypeEntity().toEntity(instrumentType));
		return entity.toDomain();
	}

	@Transactional
	public InstrumentType update(InstrumentType instrumentType) {
		InstrumentTypeEntity entity = instrumentTypeJdbcRepository.update(new InstrumentTypeEntity().toEntity(instrumentType));
		return entity.toDomain();
	}

	public void add(CommonRequest<InstrumentTypeContract> request) {
		instrumentTypeQueueRepository.add(request);
	}

	public Pagination<InstrumentType> search(InstrumentTypeSearch domain) {

		return instrumentTypeJdbcRepository.search(domain);

	}

}