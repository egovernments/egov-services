package org.egov.access.persistence.repository;


import org.egov.access.persistence.repository.querybuilder.BaseQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BaseRepository {

    private static final Logger logger = LoggerFactory.getLogger(BaseRepository.class);

    private JdbcTemplate jdbcTemplate;

    public BaseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Object> run(BaseQueryBuilder queryBuilder, RowMapper rowMapper) {
        String query = queryBuilder.build();
        logger.debug("Query : " + query);
        return jdbcTemplate.query(query, rowMapper);
    }
}
