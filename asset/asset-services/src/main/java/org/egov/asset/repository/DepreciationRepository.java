package org.egov.asset.repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.domain.CalculationAssetDetails;
import org.egov.asset.domain.CalculationCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Depreciation;
import org.egov.asset.model.DepreciationCriteria;
import org.egov.asset.model.DepreciationDetail;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.repository.builder.DepreciationQueryBuilder;
import org.egov.asset.repository.rowmapper.CalculationAssetDetailsRowMapper;
import org.egov.asset.repository.rowmapper.CalculationCurrentValueRowMapper;
import org.egov.asset.repository.rowmapper.DepreciationDetailRowMapper;
import org.egov.asset.repository.rowmapper.DepreciationSumRowMapper;
import org.egov.asset.service.AssetConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DepreciationRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private CalculationAssetDetailsRowMapper calculationAssetDetailsRowMapper;

	@Autowired
	private CalculationCurrentValueRowMapper calculationCurrentValueRowMapper;

	@Autowired
	private DepreciationQueryBuilder depreciationQueryBuilder;
	
	@Autowired
	private DepreciationDetailRowMapper depreciationDetailRowMapper;
	
	@Autowired
	private DepreciationSumRowMapper depreciationSumRowMapper;
	
	@Autowired
	private AssetConfigurationService assetConfigurationService;
	
	public List<DepreciationDetail> getDepreciationdetails(DepreciationCriteria depreciationCriteria){

		List<Object> preparedStatementValues = new ArrayList<>();
		String sql = depreciationQueryBuilder.getDepreciationSearchQuery(depreciationCriteria,preparedStatementValues);
		return jdbcTemplate.query(sql,preparedStatementValues.toArray(),depreciationDetailRowMapper);
	}
	public void saveDepreciation(Depreciation depreciation){
		
		String sql = depreciationQueryBuilder.getInsertQuery();
		List<DepreciationDetail> assetDepreciationvalues = depreciation.getDepreciationDetails();
		AuditDetails auditDetails = depreciation.getAuditDetails();
		final int batchSize = Integer.parseInt(applicationProperties.getBatchSize());

		for (int j = 0; j < assetDepreciationvalues.size(); j += batchSize) {

			final List<DepreciationDetail> batchList = assetDepreciationvalues.subList(j,
					j + batchSize > assetDepreciationvalues.size() ? assetDepreciationvalues.size() : j + batchSize);

			 jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int rowNum) throws SQLException {

					DepreciationDetail depreciationDetail = assetDepreciationvalues.get(rowNum);
					String reasonForFailure = null;
					if (depreciationDetail.getReasonForFailure() != null)
						reasonForFailure = depreciationDetail.getReasonForFailure().toString();
					
					ps.setLong(1, depreciationDetail.getId());
					ps.setLong(2, depreciationDetail.getAssetId());
					ps.setString(3, depreciation.getFinancialYear());
					ps.setLong(4, depreciation.getFromDate());
					ps.setLong(5, depreciation.getToDate());
					ps.setObject(6, depreciationDetail.getVoucherReference());
					ps.setString(7, depreciation.getTenantId());
					ps.setString(8, depreciationDetail.getStatus().toString());
					ps.setObject(9, depreciationDetail.getDepreciationRate());
					ps.setBigDecimal(10, depreciationDetail.getValueBeforeDepreciation());
					ps.setBigDecimal(11, depreciationDetail.getDepreciationValue());
					ps.setBigDecimal(12, depreciationDetail.getValueAfterDepreciation());
					ps.setString(13, auditDetails.getCreatedBy());
					ps.setLong(14, auditDetails.getCreatedDate());
					ps.setString(15, auditDetails.getLastModifiedBy());
					ps.setLong(16, auditDetails.getLastModifiedDate());
					ps.setString(17, reasonForFailure);
				}

				@Override
				public int getBatchSize() {
					return batchList.size();
				}
			});
		}
	}
	
	public Map<Long, BigDecimal> getdepreciationSum(String tenantId) {

		String sql = depreciationQueryBuilder.getDepreciationSumQuery(tenantId);
		return jdbcTemplate.query(sql,depreciationSumRowMapper);
	}

	public List<CalculationAssetDetails> getCalculationAssetDetails(DepreciationCriteria depreciationCriteria) {

		String sql = depreciationQueryBuilder.getCalculationAssetDetailsQuery(depreciationCriteria);
		log.debug("the CalculationAssetDetails query-- "+sql);
		return jdbcTemplate.query(sql, calculationAssetDetailsRowMapper);
	}

	public List<CalculationCurrentValue> getCalculationCurrentvalue(DepreciationCriteria depreciationCriteria) {
				
		String tenantId = depreciationCriteria.getTenantId();
		Integer year = Integer.parseInt(depreciationCriteria.getFinancialYear().substring(0,4));
		String[] sepDate = assetConfigurationService
				.getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DEPRECIATIONSEPARATIONDATE, tenantId)
				.split("/");
		Long seprationDate = Date
				.from(LocalDateTime.of(year, Integer.parseInt(sepDate[0]), Integer.parseInt(sepDate[1]),
						Integer.parseInt(sepDate[2]), Integer.parseInt(sepDate[3]), Integer.parseInt(sepDate[4]))
						.toInstant(ZoneOffset.UTC)).getTime();
		String sql = depreciationQueryBuilder.getCalculationCurrentvalueQuery(depreciationCriteria.getAssetIds());
		List<Object> pSValues = new ArrayList<>();
		pSValues.add(seprationDate);
		pSValues.add(tenantId);
		pSValues.add(seprationDate);
		pSValues.add(tenantId);
		log.info("the calculation currentvalue query --" + sql + "\n --preparedstatementvalues---" + pSValues);

		return jdbcTemplate.query(sql, pSValues.toArray(), calculationCurrentValueRowMapper);
	}
}
