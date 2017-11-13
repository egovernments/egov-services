package org.egov.swm.domain.repository;

import java.util.List;

import org.egov.swm.domain.model.Supplier;
import org.egov.swm.persistence.repository.SupplierJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierRepository {

	@Autowired
	private SupplierJdbcRepository supplierJdbcRepository;

	public List<Supplier> search(Supplier supplierSearch) {

		return supplierJdbcRepository.search(supplierSearch);

	}

	public Boolean uniqueCheck(String tenantId, String fieldName, String fieldValue, String uniqueFieldName,
			String uniqueFieldValue) {

		return supplierJdbcRepository.uniqueCheck(tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
	}

}