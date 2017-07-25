package org.egov.egf.master.domain.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Scheme;
import org.egov.egf.master.domain.model.SchemeSearch;
import org.egov.egf.master.persistence.entity.SchemeEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.SchemeJdbcRepository;
import org.egov.egf.master.web.requests.SchemeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SchemeRepository {

	@Autowired
	private SchemeJdbcRepository schemeJdbcRepository;
	@Autowired
	private MastersQueueRepository schemeQueueRepository;

	public Scheme findById(Scheme scheme) {
		SchemeEntity entity = schemeJdbcRepository.findById(new SchemeEntity().toEntity(scheme));
		return entity.toDomain();

	}

	@Transactional
	public Scheme save(Scheme scheme) {
		SchemeEntity entity = schemeJdbcRepository.create(new SchemeEntity().toEntity(scheme));
		return entity.toDomain();
	}

	@Transactional
	public Scheme update(Scheme scheme) {
		SchemeEntity entity = schemeJdbcRepository.update(new SchemeEntity().toEntity(scheme));
		return entity.toDomain();
	}

	public void add(SchemeRequest request) {
		Map<String, Object> message = new HashMap<>();

		if (request.getRequestInfo().getAction().equalsIgnoreCase("create")) {
			message.put("scheme_create", request);
		} else {
			message.put("scheme_update", request);
		}
		schemeQueueRepository.add(message);
	}

	public Pagination<Scheme> search(SchemeSearch domain) {

		return schemeJdbcRepository.search(domain);

	}

}