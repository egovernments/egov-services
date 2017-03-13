package org.egov.audit.domain.service;

import org.egov.audit.persistence.entity.Audit;
import org.egov.audit.persistence.repository.AuditJpaRepository;

public class AuditService {

	private AuditJpaRepository auditRepository;
	
	public void save(Audit audit){
		auditRepository.save(audit);
	}
}
