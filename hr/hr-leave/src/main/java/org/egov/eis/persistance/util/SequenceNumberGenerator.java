package org.egov.eis.persistance.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class SequenceNumberGenerator {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Serializable getNextSequence(final String sequenceName) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        String query = "SELECT nextval (?) as nextval";
        preparedStatementValues.add(sequenceName);
        return (Serializable) jdbcTemplate.queryForObject(query, preparedStatementValues.toArray(), Integer.class);
    }
}
