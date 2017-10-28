package org.egov.lcms.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.AdvocatePayment;
import org.egov.lcms.models.AdvocatePaymentSearchCriteria;
import org.egov.lcms.repository.builder.AdvocatePaymentQueryBuilder;
import org.egov.lcms.repository.rowmapper.AdvocatePaymentRowMapper;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Shubham Pratap
 *
 */

@Repository
@Slf4j
public class AdvocatePaymentRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private AdvocatePaymentRowMapper advocatePaymentRowMapper;

	@Autowired
	private AdvocatePaymentQueryBuilder advocatePaymentQueryBuilder;
	
	@Autowired
	PropertiesManager propertiesManager;

	public List<AdvocatePayment> search(final AdvocatePaymentSearchCriteria advocatePaymentSearchCriteria) {

		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String queryStr = advocatePaymentQueryBuilder.getQuery(advocatePaymentSearchCriteria,
				preparedStatementValues);

		List<AdvocatePayment> advocatePayment = new ArrayList<AdvocatePayment>();
		try {
			advocatePayment = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), advocatePaymentRowMapper);
		} catch (final Exception exception) {
			throw new CustomException(propertiesManager.getPaymentSearchErrorCode(), exception.getMessage());
		}

		return advocatePayment;
	}
}