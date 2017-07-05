package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.Scheme;
import org.egov.egf.master.domain.model.SchemeSearch;
import org.egov.egf.master.persistence.entity.SchemeEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.SchemeJdbcRepository;
import org.egov.egf.master.web.contract.SchemeContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchemeRepository {

	@Autowired
	private SchemeJdbcRepository schemeJdbcRepository;
	@Autowired
	private MastersQueueRepository schemeQueueRepository;

	public Scheme findById(Scheme scheme) {
		return schemeJdbcRepository.findById(new SchemeEntity().toEntity(scheme)).toDomain();

	}

	public Scheme save(Scheme scheme) {
		return schemeJdbcRepository.create(new SchemeEntity().toEntity(scheme)).toDomain();
	}

	public Scheme update(Scheme entity) {
		return schemeJdbcRepository.update(new SchemeEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<SchemeContract> request) {
		schemeQueueRepository.add(request);
	}

	public Pagination<Scheme> search(SchemeSearch domain) {

		return schemeJdbcRepository.search(domain);

	}

}