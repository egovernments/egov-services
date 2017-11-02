package org.egov.lcms.repository;

import java.util.ArrayList;
import java.util.List;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.Register;
import org.egov.lcms.models.RegisterSearchCriteria;
import org.egov.lcms.repository.builder.RegisterBuilder;
import org.egov.lcms.repository.rowmapper.RegisterRowMapper;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class RegisterRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	RegisterRowMapper registerRowMapper;

	public List<Register> search(RegisterSearchCriteria registerSearchCriteria) {

		if (registerSearchCriteria.getPageNumber() == null || registerSearchCriteria.getPageNumber() == 0)
			registerSearchCriteria.setPageNumber(Integer.valueOf(propertiesManager.getDefaultPageNumber().trim()));

		if (registerSearchCriteria.getPageSize() == null)
			registerSearchCriteria.setPageSize(Integer.valueOf(propertiesManager.getDefaultPageSize().trim()));

		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String selectQuery = RegisterBuilder.getSearchQuery(registerSearchCriteria, preparedStatementValues);

		List<Register> registers = new ArrayList<Register>();
		try {
			registers = jdbcTemplate.query(selectQuery, preparedStatementValues.toArray(), registerRowMapper);
		} catch (final Exception exception) {
			log.info("the exception in register search :" + exception);
			throw new CustomException(propertiesManager.getRegisterErrorCode(), propertiesManager.getRegisterErrorMsg());
		}

		return registers;
	}
}
