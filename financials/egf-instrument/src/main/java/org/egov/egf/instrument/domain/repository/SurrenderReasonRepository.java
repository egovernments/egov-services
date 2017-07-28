package org.egov.egf.instrument.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.instrument.domain.model.SurrenderReason;
import org.egov.egf.instrument.domain.model.SurrenderReasonSearch;
import org.egov.egf.instrument.persistence.entity.SurrenderReasonEntity;
import org.egov.egf.instrument.persistence.repository.SurrenderReasonJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SurrenderReasonRepository {

	@Autowired
	private SurrenderReasonJdbcRepository surrenderReasonJdbcRepository;

	public SurrenderReason findById(SurrenderReason surrenderReason) {
		SurrenderReasonEntity entity = surrenderReasonJdbcRepository
				.findById(new SurrenderReasonEntity().toEntity(surrenderReason));
		if (entity != null)
			return entity.toDomain();

		return null;


	}

	@Transactional
	public SurrenderReason save(SurrenderReason surrenderReason) {
		SurrenderReasonEntity entity = surrenderReasonJdbcRepository
				.create(new SurrenderReasonEntity().toEntity(surrenderReason));
		return entity.toDomain();
	}

	@Transactional
	public SurrenderReason update(SurrenderReason surrenderReason) {
		SurrenderReasonEntity entity = surrenderReasonJdbcRepository
				.update(new SurrenderReasonEntity().toEntity(surrenderReason));
		return entity.toDomain();
	}

	public Pagination<SurrenderReason> search(SurrenderReasonSearch domain) {

		// if() {
		// SurrenderReasonSearchContract surrenderReasonSearchContract = new
		// SurrenderReasonSearchContract();
		// ModelMapper mapper = new ModelMapper();
		// mapper.map(domain,surrenderReasonSearchContract );
		// Pagination<SurrenderReason> surrenderreasons =
		// surrenderReasonESRepository.search(surrenderReasonSearchContract);
		// return surrenderreasons;
		// }

		return surrenderReasonJdbcRepository.search(domain);

	}

}