package org.egov.calculator.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.calculator.repository.builder.TransferFeeRateBuilder;
import org.egov.calculator.repository.rowmpper.TransferFeeRateRowMapper;
import org.egov.calculator.utility.TimeStampUtil;
import org.egov.models.TransferFeeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class TransferFeeRateRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	TransferFeeRateRowMapper transferFeeRateRowMapper;

	public Long saveTransferFeeRate(TransferFeeRate transferFeeRate, String tenantId) {

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection
						.prepareStatement(TransferFeeRateBuilder.INSERT_TRANSFERFEERATES_QUERY, new String[] { "id" });
				ps.setString(1, transferFeeRate.getTenantId());
				ps.setString(2, transferFeeRate.getFeeFactor().toString());
				ps.setTimestamp(3, TimeStampUtil.getTimeStamp(transferFeeRate.getFromDate()));
				ps.setTimestamp(4, TimeStampUtil.getTimeStamp(transferFeeRate.getToDate()));
				ps.setDouble(5, transferFeeRate.getFromValue());
				ps.setDouble(6, transferFeeRate.getToValue());
				ps.setDouble(7, transferFeeRate.getFeePercentage());
				ps.setDouble(8, transferFeeRate.getFlatValue());
				ps.setString(9, transferFeeRate.getAuditDetails().getCreatedBy());
				ps.setString(10, transferFeeRate.getAuditDetails().getLastModifiedBy());
				ps.setLong(11, transferFeeRate.getAuditDetails().getCreatedTime());
				ps.setLong(12, transferFeeRate.getAuditDetails().getLastModifiedTime());
				return ps;
			}
		};
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);
		return Long.valueOf(holder.getKey().intValue());
	}

	public void updateTransferFeeRate(TransferFeeRate transferFeeRate, String tenantId) {

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection
						.prepareStatement(TransferFeeRateBuilder.UPDATE_TRANSFERFEERATES_QUERY, new String[] { "id" });
				ps.setString(1, transferFeeRate.getTenantId());
				ps.setString(2, transferFeeRate.getFeeFactor().toString());
				ps.setTimestamp(3, TimeStampUtil.getTimeStamp(transferFeeRate.getFromDate()));
				ps.setTimestamp(4, TimeStampUtil.getTimeStamp(transferFeeRate.getToDate()));
				ps.setDouble(5, transferFeeRate.getFromValue());
				ps.setDouble(6, transferFeeRate.getToValue());
				ps.setDouble(7, transferFeeRate.getFeePercentage());
				ps.setDouble(8, transferFeeRate.getFlatValue());
				ps.setString(9, transferFeeRate.getAuditDetails().getLastModifiedBy());
				ps.setLong(10, transferFeeRate.getAuditDetails().getLastModifiedTime());
				ps.setLong(11, transferFeeRate.getId());
				return ps;
			}
		};
		jdbcTemplate.update(psc);
	}

	public List<TransferFeeRate> getTransferFeeRates(String tenantId, String feeFactor, String validDate,
			Double validValue) {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String transferFeeRateSelect = TransferFeeRateBuilder.getTransferFeeRates(tenantId, feeFactor, validDate,
				validValue, preparedStatementValues);
		List<TransferFeeRate> transferFeeRates = new ArrayList<>();
		transferFeeRates = getTransferFeeRates(transferFeeRateSelect, preparedStatementValues);
		return transferFeeRates;
	}

	public List<TransferFeeRate> getTransferFeeRates(String query, List<Object> preparedStatementValues) {
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());
		return transferFeeRateRowMapper.getMappedTransferFeeRates(rows);
	}

	public boolean checkWhetherSlabRecordExits(String tenantId, String feeFactor, Double fromValue, Double toValue,
			String fromDate, String toDate, String tableName, Long id) {
		Boolean isExists = Boolean.TRUE;
		String query = TransferFeeRateBuilder.getUniqueAndOverlappingSlabQuery(tenantId, feeFactor, fromValue, toValue,
				fromDate, toDate, tableName, id);
		int count = 0;
		count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);
		if (count == 0)
			isExists = Boolean.FALSE;
		return isExists;
	}
}
