package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.Functionary;
import org.egov.egf.master.domain.model.FunctionarySearch;
import org.egov.egf.master.persistence.entity.FunctionaryEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.FunctionaryJdbcRepository;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FunctionaryRepository {

	@Autowired
	private FunctionaryJdbcRepository functionaryJdbcRepository;
	@Autowired
	private MastersQueueRepository functionaryQueueRepository;

	public Functionary findById(Functionary functionary) {
		return functionaryJdbcRepository.findById(new FunctionaryEntity().toEntity(functionary)).toDomain();

	}

	public Functionary save(Functionary functionary) {
		return functionaryJdbcRepository.create(new FunctionaryEntity().toEntity(functionary)).toDomain();
	}

	public Functionary update(Functionary entity) {
		return functionaryJdbcRepository.update(new FunctionaryEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<FunctionaryContract> request) {
		functionaryQueueRepository.add(request);
	}

	public Pagination<Functionary> search(FunctionarySearch domain) {

		return functionaryJdbcRepository.search(domain);

	}

}