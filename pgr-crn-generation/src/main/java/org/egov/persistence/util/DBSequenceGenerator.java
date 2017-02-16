package org.egov.persistence.util;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.sql.SQLException;

@Service
public class DBSequenceGenerator {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = SQLGrammarException.class)
    public Serializable createAndGetNextSequence(final String sequenceName) throws SQLException {
        Query query = entityManager.unwrap(Session.class).createSQLQuery("create sequence " + sequenceName);
        query.executeUpdate();

        String NEXT_SEQ_SQL_QUERY = "SELECT nextval (:sequenceName) as nextval";
        query = entityManager.unwrap(Session.class).createSQLQuery(NEXT_SEQ_SQL_QUERY);
        query.setParameter("sequenceName", sequenceName);
        return (Serializable) query.uniqueResult();
    }
}
