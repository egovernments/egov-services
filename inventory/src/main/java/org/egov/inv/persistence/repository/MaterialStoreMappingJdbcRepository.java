package org.egov.inv.persistence.repository;

import org.egov.inv.persistence.entity.MaterialStoreMappingEntity;
import org.springframework.stereotype.Service;

@Service
public class MaterialStoreMappingJdbcRepository extends JdbcRepository {

    static {
        init(MaterialStoreMappingEntity.class);
    }

}
