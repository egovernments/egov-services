package org.egov.swm.domain.repository;

import java.util.List;

import org.egov.swm.domain.model.Contractor;
import org.egov.swm.persistence.repository.ContractorJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractorRepository {

	@Autowired
	private ContractorJdbcRepository contractorJdbcRepository;

	public List<Contractor> search(Contractor contractorSearch) {

		return contractorJdbcRepository.search(contractorSearch);

	}

	public Boolean uniqueCheck(String tenantId, String fieldName, String fieldValue, String uniqueFieldName,
			String uniqueFieldValue) {

		return contractorJdbcRepository.uniqueCheck(tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
	}

}