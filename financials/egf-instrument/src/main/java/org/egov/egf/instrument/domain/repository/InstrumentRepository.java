package org.egov.egf.instrument.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.instrument.domain.model.Instrument;
import org.egov.egf.instrument.domain.model.InstrumentSearch;
import org.egov.egf.instrument.persistence.entity.InstrumentEntity;
import org.egov.egf.instrument.persistence.repository.InstrumentJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstrumentRepository {

	@Autowired
	private InstrumentJdbcRepository instrumentJdbcRepository;

	public Instrument findById(Instrument instrument) {
		InstrumentEntity entity = instrumentJdbcRepository.findById(new InstrumentEntity().toEntity(instrument));
		if (entity != null)
			return entity.toDomain();

		return null;


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