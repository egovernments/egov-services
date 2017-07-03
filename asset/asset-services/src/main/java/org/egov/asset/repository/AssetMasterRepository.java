package org.egov.asset.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.model.AssetStatus;
import org.egov.asset.model.AssetStatusCriteria;
import org.egov.asset.repository.builder.AssetStatusQueryBuilder;
import org.egov.asset.repository.rowmapper.AssetStatusRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AssetMasterRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private AssetStatusQueryBuilder assetStatusQueryBuilder;

	@Autowired
	private AssetStatusRowMapper assetStatusRowMapper;

	private static final Logger logger = LoggerFactory.getLogger(RevaluationRepository.class);

	public List<AssetStatus> search(final AssetStatusCriteria assetStatusCriteria) {

		final List<Object> preparedStatementValues = new ArrayList<>();
		final String queryStr = assetStatusQueryBuilder.getQuery(assetStatusCriteria, preparedStatementValues);
		List<AssetStatus> assetStatus = null;
		try {
			logger.info("queryStr::" + queryStr + "preparedStatementValues::" + preparedStatementValues.toString());
			assetStatus = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), assetStatusRowMapper);
			logger.info("AssetStatusRepository::" + assetStatus);
		} catch (final Exception ex) {
			logger.info("the exception from findforcriteria : " + ex);
		}
		return assetStatus;
	}

}
