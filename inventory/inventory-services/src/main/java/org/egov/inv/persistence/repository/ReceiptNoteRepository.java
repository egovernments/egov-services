package org.egov.inv.persistence.repository;

import org.egov.common.JdbcRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReceiptNoteRepository extends JdbcRepository {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ReceiptNoteRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

}
