package org.egov.egf.instrument.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.instrument.domain.model.InstrumentAccountCode;
import org.egov.egf.instrument.domain.model.InstrumentAccountCodeSearch;
import org.egov.egf.instrument.persistence.entity.InstrumentAccountCodeEntity;
import org.egov.egf.instrument.persistence.queue.MastersQueueRepository;
import org.egov.egf.instrument.persistence.repository.InstrumentAccountCodeJdbcRepository;
import org.egov.egf.instrument.web.contract.InstrumentAccountCodeContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstrumentAccountCodeRepository {

	@Autowired
	private InstrumentAccountCodeJdbcRepository instrumentAccountCodeJdbcRepository;
	@Autowired
	private MastersQueueRepository instrumentAccountCodeQueueRepository;

	public InstrumentAccountCode findById(InstrumentAccountCode instrumentAccountCode) {
		InstrumentAccountCodeEntity entity = instrumentAccountCodeJdbcRepository.findById(new InstrumentAccountCodeEntity().toEntity(instrumentAccountCode));
		return entity.toDomain();

	}

	@Transactional
	public InstrumentAccountCode save(InstrumentAccountCode instrumentAccountCode) {
		InstrumentAccountCodeEntity entity = instrumentAccountCodeJdbcRepository.create(new InstrumentAccountCodeEntity().toEntity(instrumentAccountCode));
		return entity.toDomain();
	}

	@Transactional
	public InstrumentAccountCode update(InstrumentAccountCode instrumentAccountCode) {
		InstrumentAccountCodeEntity entity = instrumentAccountCodeJdbcRepository.update(new InstrumentAccountCodeEntity().toEntity(instrumentAccountCode));
		return entity.toDomain();
	}

	public void add(CommonRequest<InstrumentAccountCodeContract> request) {
		instrumentAccountCodeQueueRepository.add(request);
	}

	public Pagination<InstrumentAccountCode> search(InstrumentAccountCodeSearch domain) {

		return instrumentAccountCodeJdbcRepository.search(domain);

	}

}