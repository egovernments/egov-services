package org.egov.egf.instrument.domain.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.instrument.domain.model.InstrumentAccountCode;
import org.egov.egf.instrument.domain.model.InstrumentAccountCodeSearch;
import org.egov.egf.instrument.persistence.entity.InstrumentAccountCodeEntity;
import org.egov.egf.instrument.persistence.queue.InstrumentQueueRepository;
import org.egov.egf.instrument.persistence.repository.InstrumentAccountCodeJdbcRepository;
import org.egov.egf.instrument.web.requests.InstrumentAccountCodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstrumentAccountCodeRepository {

	@Autowired
	private InstrumentAccountCodeJdbcRepository instrumentAccountCodeJdbcRepository;
	@Autowired
	private InstrumentQueueRepository instrumentAccountCodeQueueRepository;

	/*@Autowired
	private InstrumentAccountCodeESRepository instrumentAccountCodeESRepository;*/

	public InstrumentAccountCode findById(InstrumentAccountCode instrumentAccountCode) {
		InstrumentAccountCodeEntity entity = instrumentAccountCodeJdbcRepository
				.findById(new InstrumentAccountCodeEntity().toEntity(instrumentAccountCode));
		return entity.toDomain();

	}

	@Transactional
	public InstrumentAccountCode save(InstrumentAccountCode instrumentAccountCode) {
		InstrumentAccountCodeEntity entity = instrumentAccountCodeJdbcRepository
				.create(new InstrumentAccountCodeEntity().toEntity(instrumentAccountCode));
		return entity.toDomain();
	}

	@Transactional
	public InstrumentAccountCode update(InstrumentAccountCode instrumentAccountCode) {
		InstrumentAccountCodeEntity entity = instrumentAccountCodeJdbcRepository
				.update(new InstrumentAccountCodeEntity().toEntity(instrumentAccountCode));
		return entity.toDomain();
	}

	public void add(InstrumentAccountCodeRequest request) {
		Map<String, Object> message = new HashMap<>();

		if (request.getRequestInfo().getAction().equalsIgnoreCase("create")) {
			message.put("instrumentAccountCode_create", request);
		} else {
			message.put("instrumentAccountCode_update", request);
		}
		instrumentAccountCodeQueueRepository.add(message);
	}

	public Pagination<InstrumentAccountCode> search(InstrumentAccountCodeSearch domain) {

		// if() {
		// InstrumentAccountCodeSearchContract
		// instrumentAccountCodeSearchContract = new
		// InstrumentAccountCodeSearchContract();
		// ModelMapper mapper = new ModelMapper();
		// mapper.map(domain,instrumentAccountCodeSearchContract );
		// Pagination<InstrumentAccountCode> instrumentaccountcodes =
		// instrumentAccountCodeESRepository.search(instrumentAccountCodeSearchContract);
		// return instrumentaccountcodes;
		// }

		return instrumentAccountCodeJdbcRepository.search(domain);

	}

}