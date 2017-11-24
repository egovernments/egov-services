package org.egov.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.model.Asset;
import org.egov.model.TransactionHistory;
import org.egov.model.criteria.AssetCriteria;
import org.egov.repository.querybuilder.AssetQueryBuilder;
import org.egov.repository.rowmapper.AssetHistoryRowMapper;
import org.egov.repository.rowmapper.AssetRowMapper;
import org.egov.service.AssetCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AssetRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private AssetRowMapper assetRowMapper;

    @Autowired
	private ObjectMapper mapper;

	@Autowired
	private AssetCommonService assetCommonService;
	
	@Autowired
	private AssetQueryBuilder assetQueryBuilder;
	
	@Autowired
	private AssetHistoryRowMapper assetHistoryRowMapper;
	
	public List<Asset> findForCriteria(final AssetCriteria assetCriteria) {

		final List<Object> preparedStatementValues = new ArrayList<>();
		final String queryStr = assetQueryBuilder.getQuery(assetCriteria, preparedStatementValues);
		List<Asset> assets = new ArrayList<Asset>();
			log.debug("queryStr::" + queryStr + "preparedStatementValues::" + preparedStatementValues.toString());
			assets = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), assetRowMapper);
			log.debug("Assets From Criteria::" + assets);
		return assets;
	}

	public List<Asset> findAssetByCode(final String code) {
		final AssetCriteria assetCriteria = new AssetCriteria();
		assetCriteria.setCode(code);
		return findForCriteria(assetCriteria);
	}

	public Map<Long, List<TransactionHistory>> getTransactionHistory(Set<Long> assetids , String tenantId){
		
		final String queryStr = assetQueryBuilder.getHistoryQuery(assetids, tenantId);
		return jdbcTemplate.query(queryStr, assetHistoryRowMapper);
	}
	

}