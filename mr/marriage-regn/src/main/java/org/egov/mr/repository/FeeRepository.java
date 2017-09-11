package org.egov.mr.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.egov.mr.config.PropertiesManager;
import org.egov.mr.model.Fee;
import org.egov.mr.repository.querybuilder.FeeQueryBuilder;
import org.egov.mr.repository.rowmapper.FeeRowMapper;
import org.egov.mr.service.ServiceConfigurationService;
import org.egov.mr.web.contract.FeeCriteria;
import org.egov.mr.web.contract.FeeRequest;
import org.egov.mr.web.contract.ServiceConfigurationSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FeeRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private FeeQueryBuilder feeQueryBuilder;
	
	@Autowired
	private FeeRowMapper feeRowMapper;
	
	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
	private ServiceConfigurationService serviceConfigurationService;
	
//	private String batchS = propertiesManager.getBatchSize();
	
	//TODO value of batch size to be fetched from serviceConfig
	private int batchSize=500;
	
	public List<Fee> findForCriteria(FeeCriteria feeCriteria) {

		return jdbcTemplate.query(feeQueryBuilder.getQuery(feeCriteria), feeRowMapper);
	}
	
	public void createFee(FeeRequest feeRequest) {
		List<Fee> fees = feeRequest.getFees();

		for (int j = 0; j < fees.size(); j += batchSize) {

			final List<Fee> batchList = fees.subList(j, j + batchSize > fees.size() ? fees.size() : j + batchSize);

			jdbcTemplate.batchUpdate(feeQueryBuilder.CREATE_QUERY, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Fee fee = fees.get(i);

					ps.setString(1, fee.getId());
					ps.setString(2, fee.getTenantId());
					ps.setString(3, fee.getFeeCriteria());
					ps.setBigDecimal(4, fee.getFee());
					ps.setLong(5, fee.getFromDate());
					ps.setLong(6, fee.getToDate());
					ps.setLong(7, new Date().getTime());
					ps.setLong(8, new Date().getTime());
					ps.setString(9, feeRequest.getRequestInfo().getDid());
					ps.setString(10, feeRequest.getRequestInfo().getDid());
				}

				@Override
				public int getBatchSize() {
					return batchList.size();
				}
			});
		}
	}
	
	public void updateFee(FeeRequest feeRequest) {
		List<Fee> fees = feeRequest.getFees();

		for (int j = 0; j < fees.size(); j += batchSize) {

			final List<Fee> batchList = fees.subList(j, j + batchSize > fees.size() ? fees.size() : j + batchSize);

			jdbcTemplate.batchUpdate(feeQueryBuilder.UPDATE_QUERY, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Fee fee = fees.get(i);

					ps.setString(1, fee.getFeeCriteria());
					ps.setBigDecimal(2, fee.getFee());
					ps.setLong(3, fee.getFromDate());
					ps.setLong(4, fee.getToDate());
					ps.setLong(5, new Date().getTime());
					ps.setString(6, feeRequest.getRequestInfo().getDid());
					ps.setString(7, fee.getId());
					ps.setString(8, fee.getTenantId());
				}

				@Override
				public int getBatchSize() {
					return fees.size();
				}
			});
		}
	}
}
