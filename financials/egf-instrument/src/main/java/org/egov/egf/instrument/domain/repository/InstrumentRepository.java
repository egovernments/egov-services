package org.egov.egf.instrument.domain.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.instrument.domain.model.Instrument;
import org.egov.egf.instrument.domain.model.InstrumentSearch;
import org.egov.egf.instrument.persistence.entity.InstrumentEntity;
import org.egov.egf.instrument.persistence.queue.InstrumentQueueRepository;
import org.egov.egf.instrument.persistence.repository.InstrumentJdbcRepository;
import org.egov.egf.instrument.web.requests.InstrumentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstrumentRepository {

	@Autowired
	private InstrumentJdbcRepository instrumentJdbcRepository;
	@Autowired
	private InstrumentQueueRepository instrumentQueueRepository;

	/*@Autowired
	private InstrumentESRepository instrumentESRepository;*/

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

	public void add(InstrumentRequest request) {
		Map<String, Object> message = new HashMap<>();

		if (request.getRequestInfo().getAction().equalsIgnoreCase("create")) {
			message.put("instrument_create", request);
		} else {
			message.put("instrument_update", request);
		}
		instrumentQueueRepository.add(message);
	}

	public Pagination<Instrument> search(InstrumentSearch domain) {

		// if() {
		// InstrumentSearchContract instrumentSearchContract = new
		// InstrumentSearchContract();
		// ModelMapper mapper = new ModelMapper();
		// mapper.map(domain,instrumentSearchContract );
		// Pagination<Instrument> instruments =
		// instrumentESRepository.search(instrumentSearchContract);
		// return instruments;
		// }

		return instrumentJdbcRepository.search(domain);

	}

}