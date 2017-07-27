package org.egov.egf.master.domain.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.SubScheme;
import org.egov.egf.master.domain.model.SubSchemeSearch;
import org.egov.egf.master.persistence.entity.SubSchemeEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.SubSchemeJdbcRepository;
import org.egov.egf.master.web.requests.SubSchemeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubSchemeRepository {

	@Autowired
	private SubSchemeJdbcRepository subSchemeJdbcRepository;
	@Autowired
	private MastersQueueRepository subSchemeQueueRepository;

	public SubScheme findById(SubScheme subScheme) {
		SubSchemeEntity entity = subSchemeJdbcRepository.findById(new SubSchemeEntity().toEntity(subScheme));
		return entity.toDomain();

	}

	@Transactional
	public SubScheme save(SubScheme subScheme) {
		SubSchemeEntity entity = subSchemeJdbcRepository.create(new SubSchemeEntity().toEntity(subScheme));
		return entity.toDomain();
	}

	@Transactional
	public SubScheme update(SubScheme subScheme) {
		SubSchemeEntity entity = subSchemeJdbcRepository.update(new SubSchemeEntity().toEntity(subScheme));
		return entity.toDomain();
	}

	public void add(SubSchemeRequest request) {
		Map<String, Object> message = new HashMap<>();

		if (request.getRequestInfo().getAction().equalsIgnoreCase("create")) {
			message.put("subscheme_create", request);
		} else {
			message.put("subscheme_update", request);
		}
		subSchemeQueueRepository.add(message);
	}

	public Pagination<SubScheme> search(SubSchemeSearch domain) {

		return subSchemeJdbcRepository.search(domain);

	}

}