package org.egov.egf.master.domain.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.common.constants.EgfConstants;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Supplier;
import org.egov.egf.master.domain.model.SupplierSearch;
import org.egov.egf.master.persistence.entity.SupplierEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.SupplierJdbcRepository;
import org.egov.egf.master.web.requests.SupplierRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SupplierRepository {

	@Autowired
	private SupplierJdbcRepository supplierJdbcRepository;
	@Autowired
	private MastersQueueRepository supplierQueueRepository;

	public Supplier findById(Supplier supplier) {
		SupplierEntity entity = supplierJdbcRepository.findById(new SupplierEntity().toEntity(supplier));
		return entity.toDomain();

	}

	@Transactional
	public Supplier save(Supplier supplier) {
		SupplierEntity entity = supplierJdbcRepository.create(new SupplierEntity().toEntity(supplier));
		return entity.toDomain();
	}

	@Transactional
	public Supplier update(Supplier supplier) {
		SupplierEntity entity = supplierJdbcRepository.update(new SupplierEntity().toEntity(supplier));
		return entity.toDomain();
	}

	public void add(SupplierRequest request) {
		Map<String, Object> message = new HashMap<>();

		if (request.getRequestInfo().getAction().equalsIgnoreCase(EgfConstants.ACTION_CREATE)) {
			message.put("supplier_create", request);
		} else {
			message.put("supplier_update", request);
		}
		supplierQueueRepository.add(message);
	}

	public Pagination<Supplier> search(SupplierSearch domain) {

		return supplierJdbcRepository.search(domain);

	}

}