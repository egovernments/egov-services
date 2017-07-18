package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.Fund;
import org.egov.egf.master.domain.model.FundSearch;
import org.egov.egf.master.persistence.entity.FundEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.FundJdbcRepository;
import org.egov.egf.master.web.contract.FundContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FundRepository {

	@Autowired
	private FundJdbcRepository fundJdbcRepository;
	@Autowired
	private MastersQueueRepository fundQueueRepository;

	public Fund findById(Fund fund) {
		FundEntity entity = fundJdbcRepository.findById(new FundEntity().toEntity(fund));
		return entity.toDomain();

	}

	@Transactional
	public Fund save(Fund fund) {
		FundEntity entity = fundJdbcRepository.create(new FundEntity().toEntity(fund));
		return entity.toDomain();
	}

	@Transactional
	public Fund update(Fund fund) {
		FundEntity entity = fundJdbcRepository.update(new FundEntity().toEntity(fund));
		return entity.toDomain();
	}

	public void add(CommonRequest<FundContract> request) {
		fundQueueRepository.add(request);
	}

	public Pagination<Fund> search(FundSearch domain) {

		return fundJdbcRepository.search(domain);

	}

}
