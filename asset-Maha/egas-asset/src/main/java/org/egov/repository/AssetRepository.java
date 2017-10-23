package org.egov.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.contract.AssetRequest;
import org.egov.model.Asset;
import org.egov.model.criteria.AssetCriteria;
import org.egov.repository.queryuilder.AssetQueryBuilder;
import org.egov.repository.rowmapper.AssetRowMapper;
import org.egov.service.AssetCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.bind.v2.runtime.Location;

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

	
	


}