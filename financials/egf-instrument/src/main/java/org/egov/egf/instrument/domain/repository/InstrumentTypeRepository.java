package org.egov.egf.instrument.domain.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.instrument.domain.model.InstrumentTypeSearch;
import org.egov.egf.instrument.persistence.entity.InstrumentTypeEntity;
import org.egov.egf.instrument.persistence.queue.InstrumentQueueRepository;
import org.egov.egf.instrument.persistence.repository.InstrumentTypeJdbcRepository;
import org.egov.egf.instrument.web.requests.InstrumentTypeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstrumentTypeRepository {

	@Autowired
	private InstrumentTypeJdbcRepository instrumentTypeJdbcRepository;
	@Autowired
	private InstrumentQueueRepository instrumentTypeQueueRepository;

	/*@Autowired
	private InstrumentTypeESRepository instrumentTypeESRepository;
*/
	public InstrumentType findById(InstrumentType instrumentType) {
		InstrumentTypeEntity entity = instrumentTypeJdbcRepository
				.findById(new InstrumentTypeEntity().toEntity(instrumentType));
		return entity.toDomain();

	}

	@Transactional
	public InstrumentType save(InstrumentType instrumentType) {
		InstrumentTypeEntity entity = instrumentTypeJdbcRepository
				.create(new InstrumentTypeEntity().toEntity(instrumentType));
		return entity.toDomain();
	}

	@Transactional
	public InstrumentType update(InstrumentType instrumentType) {
		InstrumentTypeEntity entity = instrumentTypeJdbcRepository
				.update(new InstrumentTypeEntity().toEntity(instrumentType));
		return entity.toDomain();
	}

	public void add(InstrumentTypeRequest request) {
		Map<String, Object> message = new HashMap<>();

		if (request.getRequestInfo().getAction().equalsIgnoreCase("create")) {
			message.put("instrumentType_create", request);
		} else {
			message.put("instrumentType_update", request);
		}
		instrumentTypeQueueRepository.add(message);
	}

	public Pagination<InstrumentType> search(InstrumentTypeSearch domain) {

		// if() {
		// InstrumentTypeSearchContract instrumentTypeSearchContract = new
		// InstrumentTypeSearchContract();
		// ModelMapper mapper = new ModelMapper();
		// mapper.map(domain,instrumentTypeSearchContract );
		// Pagination<InstrumentType> instrumenttypes =
		// instrumentTypeESRepository.search(instrumentTypeSearchContract);
		// return instrumenttypes;
		// }

		return instrumentTypeJdbcRepository.search(domain);

	}

}