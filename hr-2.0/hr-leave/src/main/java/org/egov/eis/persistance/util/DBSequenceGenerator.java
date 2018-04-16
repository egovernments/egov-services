package org.egov.eis.persistance.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DBSequenceGenerator {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Serializable createAndGetNextSequence(final String sequenceName) throws SQLException {
        final List<Object> preparedStatementValues = new ArrayList<>();
        jdbcTemplate.execute("create sequence " + sequenceName);

        String query = "SELECT nextval (?) as nextval";
        preparedStatementValues.add(sequenceName);
        return (Serializable) jdbcTemplate.queryForObject(query, preparedStatementValues.toArray(), Integer.class);
    }
}
