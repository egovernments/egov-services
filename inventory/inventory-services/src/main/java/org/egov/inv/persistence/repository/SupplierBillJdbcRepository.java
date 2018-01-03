package org.egov.inv.persistence.repository;

import org.egov.common.JdbcRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SupplierBillJdbcRepository extends JdbcRepository {


    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SupplierBillJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


}
