package org.egov.egf.instrument.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.instrument.domain.model.Instrument;
import org.egov.egf.instrument.domain.model.InstrumentSearch;
import org.egov.egf.instrument.persistence.entity.InstrumentEntity;
import org.egov.egf.instrument.persistence.queue.MastersQueueRepository;
import org.egov.egf.instrument.persistence.repository.InstrumentJdbcRepository;
import org.egov.egf.instrument.web.contract.InstrumentContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstrumentRepository {

	@Autowired
	private InstrumentJdbcRepository instrumentJdbcRepository;
	@Autowired
	private MastersQueueRepository instrumentQueueRepository;

	public Instrument findById(Instrument instrument) {
		InstrumentEntity entity = instrumentJdbcRepository.findById(new InstrumentEntity().toEntity(instrument));
		return entity.toDomain();

	}

	@Transactional
	public Instrument save(Instrument instrument) {
		InstrumentEntity entity = instrumentJdbcRepository.create(new InstrumentEntity().toEntity(instrument));
		return entity.toDomain();
	}

	@Transactional
	public Instrument update(Instrument instrument) {
		InstrumentEntity entity = instrumentJdbcRepository.update(new InstrumentEntity().toEntity(instrument));
		return entity.toDomain();
	}

	public void add(CommonRequest<InstrumentContract> request) {
		instrumentQueueRepository.add(request);
	}

	public Pagination<Instrument> search(InstrumentSearch domain) {

		return instrumentJdbcRepository.search(domain);

	}

}