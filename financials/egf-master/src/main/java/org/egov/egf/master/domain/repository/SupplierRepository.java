package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.Supplier;
import org.egov.egf.master.domain.model.SupplierSearch;
import org.egov.egf.master.persistence.entity.SupplierEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.SupplierJdbcRepository;
import org.egov.egf.master.web.contract.SupplierContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierRepository {

	@Autowired
	private SupplierJdbcRepository supplierJdbcRepository;
	@Autowired
	private MastersQueueRepository supplierQueueRepository;

	public Supplier findById(Supplier supplier) {
		return supplierJdbcRepository.findById(new SupplierEntity().toEntity(supplier)).toDomain();

	}

	public Supplier save(Supplier supplier) {
		return supplierJdbcRepository.create(new SupplierEntity().toEntity(supplier)).toDomain();
	}

	public Supplier update(Supplier entity) {
		return supplierJdbcRepository.update(new SupplierEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<SupplierContract> request) {
		supplierQueueRepository.add(request);
	}

	public Pagination<Supplier> search(SupplierSearch domain) {

		return supplierJdbcRepository.search(domain);

	}

}