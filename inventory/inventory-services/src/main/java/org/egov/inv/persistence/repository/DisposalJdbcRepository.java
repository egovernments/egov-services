package org.egov.inv.persistence.repository;

import org.egov.common.JdbcRepository;
import org.egov.inv.persistence.entity.DisposalEntity;
import org.springframework.stereotype.Service;
@Service
public class DisposalJdbcRepository extends JdbcRepository {
	   static {
	        init(DisposalEntity.class);
	    }
}
