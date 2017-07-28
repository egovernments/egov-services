package org.egov.egf.instrument.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.instrument.domain.model.InstrumentAccountCode;
import org.egov.egf.instrument.domain.model.InstrumentAccountCodeSearch;
import org.egov.egf.instrument.persistence.entity.InstrumentAccountCodeEntity;
import org.egov.egf.instrument.persistence.repository.InstrumentAccountCodeJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstrumentAccountCodeRepository {

	@Autowired
	private InstrumentAccountCodeJdbcRepository instrumentAccountCodeJdbcRepository;

	public InstrumentAccountCode findById(InstrumentAccountCode instrumentAccountCode) {
		InstrumentAccountCodeEntity entity = instrumentAccountCodeJdbcRepository
				.findById(new InstrumentAccountCodeEntity().toEntity(instrumentAccountCode));
		if (entity != null)
			return entity.toDomain();

		return null;

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