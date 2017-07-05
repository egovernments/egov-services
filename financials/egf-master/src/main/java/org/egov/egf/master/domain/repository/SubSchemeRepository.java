package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.SubScheme;
import org.egov.egf.master.domain.model.SubSchemeSearch;
import org.egov.egf.master.persistence.entity.SubSchemeEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.SubSchemeJdbcRepository;
import org.egov.egf.master.web.contract.SubSchemeContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubSchemeRepository {

	@Autowired
	private SubSchemeJdbcRepository subSchemeJdbcRepository;
	@Autowired
	private MastersQueueRepository subSchemeQueueRepository;

	public SubScheme findById(SubScheme subScheme) {
		return subSchemeJdbcRepository.findById(new SubSchemeEntity().toEntity(subScheme)).toDomain();

	}

	public SubScheme save(SubScheme subScheme) {
		return subSchemeJdbcRepository.create(new SubSchemeEntity().toEntity(subScheme)).toDomain();
	}

	public SubScheme update(SubScheme entity) {
		return subSchemeJdbcRepository.update(new SubSchemeEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<SubSchemeContract> request) {
		subSchemeQueueRepository.add(request);
	}

	public Pagination<SubScheme> search(SubSchemeSearch domain) {

		return subSchemeJdbcRepository.search(domain);

	}

}