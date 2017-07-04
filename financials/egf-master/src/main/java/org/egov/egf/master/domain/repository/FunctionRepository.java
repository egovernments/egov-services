package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.Function;
import org.egov.egf.master.domain.model.FunctionSearch;
import org.egov.egf.master.persistence.entity.FunctionEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.FunctionJdbcRepository;
import org.egov.egf.master.web.contract.FunctionContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FunctionRepository {

	@Autowired
	private FunctionJdbcRepository functionJdbcRepository;
	@Autowired
	private MastersQueueRepository functionQueueRepository;

	public Function findById(Function function) {
		return functionJdbcRepository.findById(new FunctionEntity().toEntity(function)).toDomain();

	}

	public Function save(Function function) {
		return functionJdbcRepository.create(new FunctionEntity().toEntity(function)).toDomain();
	}

	public Function update(Function entity) {
		return functionJdbcRepository.update(new FunctionEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<FunctionContract> request) {
		functionQueueRepository.add(request);
	}

	public Pagination<Function> search(FunctionSearch domain) {

		return functionJdbcRepository.search(domain);

	}

}