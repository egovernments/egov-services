package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.Fundsource;
import org.egov.egf.master.domain.model.FundsourceSearch;
import org.egov.egf.master.persistence.entity.FundsourceEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.FundsourceJdbcRepository;
import org.egov.egf.master.web.contract.FundsourceContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundsourceRepository {

	@Autowired
	private FundsourceJdbcRepository fundsourceJdbcRepository;
	@Autowired
	private MastersQueueRepository fundsourceQueueRepository;

	public Fundsource findById(Fundsource fundsource) {
		return fundsourceJdbcRepository.findById(new FundsourceEntity().toEntity(fundsource)).toDomain();

	}

	public Fundsource save(Fundsource fundsource) {
		return fundsourceJdbcRepository.create(new FundsourceEntity().toEntity(fundsource)).toDomain();
	}

	public Fundsource update(Fundsource entity) {
		return fundsourceJdbcRepository.update(new FundsourceEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<FundsourceContract> request) {
		fundsourceQueueRepository.add(request);
	}

	public Pagination<Fundsource> search(FundsourceSearch domain) {

		return fundsourceJdbcRepository.search(domain);

	}

}