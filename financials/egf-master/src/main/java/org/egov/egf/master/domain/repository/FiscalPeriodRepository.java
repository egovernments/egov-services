package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.FiscalPeriod;
import org.egov.egf.master.domain.model.FiscalPeriodSearch;
import org.egov.egf.master.persistence.entity.FiscalPeriodEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.FiscalPeriodJdbcRepository;
import org.egov.egf.master.web.contract.FiscalPeriodContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FiscalPeriodRepository {

	@Autowired
	private FiscalPeriodJdbcRepository fiscalPeriodJdbcRepository;
	@Autowired
	private MastersQueueRepository fiscalPeriodQueueRepository;

	public FiscalPeriod findById(FiscalPeriod fiscalPeriod) {
		return fiscalPeriodJdbcRepository.findById(new FiscalPeriodEntity().toEntity(fiscalPeriod)).toDomain();

	}

	public FiscalPeriod save(FiscalPeriod fiscalPeriod) {
		return fiscalPeriodJdbcRepository.create(new FiscalPeriodEntity().toEntity(fiscalPeriod)).toDomain();
	}

	public FiscalPeriod update(FiscalPeriod entity) {
		return fiscalPeriodJdbcRepository.update(new FiscalPeriodEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<FiscalPeriodContract> request) {
		fiscalPeriodQueueRepository.add(request);
	}

	public Pagination<FiscalPeriod> search(FiscalPeriodSearch domain) {

		return fiscalPeriodJdbcRepository.search(domain);

	}

}