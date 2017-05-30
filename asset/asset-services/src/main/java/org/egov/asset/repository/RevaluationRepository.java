package org.egov.asset.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.repository.builder.RevaluationQueryBuilder;
import org.egov.asset.repository.rowmapper.RevaluationRowMapper;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RevaluationRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private RevaluationQueryBuilder revaluationQueryBuilder;
	
	@Autowired
	private RevaluationRowMapper revaluationRowMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(RevaluationRepository.class);

	public void create(RevaluationRequest revaluationRequest){
		
		logger.info("RevaluationRepository create revaluationRequest:"+revaluationRequest);
		Revaluation revaluation = revaluationRequest.getRevaluation();
		RequestInfo requestInfo = revaluationRequest.getRequestInfo();
		
		String sql = RevaluationQueryBuilder.INSERT_QUERY;
		
		String status = null;
		if(revaluation.getStatus()!=null)
		status = revaluation.getStatus().toString();
		
		Object [] obj = new Object[]{revaluation.getId(),revaluation.getTenantId(),revaluation.getAssetId(),
								revaluation.getCurrentCapitalizedValue(),revaluation.getTypeOfChange().toString(),revaluation.getRevaluationAmount(),
								revaluation.getValueAfterRevaluation(),revaluation.getRevaluationDate(),
								requestInfo.getUserInfo().getId().toString(),revaluation.getReasonForRevaluation(),
								revaluation.getFixedAssetsWrittenOffAccount(),revaluation.getFunction(),revaluation.getFund(),
								revaluation.getScheme(),revaluation.getSubScheme(),revaluation.getComments(),status,
								requestInfo.getUserInfo().getId(),new Date().getTime(),requestInfo.getUserInfo().getId(),
								new Date().getTime()};
 		try{
 			jdbcTemplate.update(sql, obj);
 		} catch (Exception ex){
 			ex.printStackTrace();
 			logger.info("RevaluationRepository create:"+ex.getMessage());
 		}
	}
	
	public List<Revaluation> search(RevaluationCriteria revaluationCriteria) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String queryStr = revaluationQueryBuilder.getQuery(revaluationCriteria, preparedStatementValues);
		List<Revaluation> revaluations = null;
		try {
			logger.info("queryStr::" + queryStr + "preparedStatementValues::" + preparedStatementValues.toString());
			revaluations = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), revaluationRowMapper);
			logger.info("RevaluationRepository::" + revaluations);
		} catch (Exception ex) {
			logger.info("the exception from findforcriteria : " + ex);
		}
		return revaluations;
	}
	
	public Integer getNextRevaluationId(){
		
		String query = "SELECT nextval('seq_egasset_revaluation')";
		Integer result = null;
		try{
			 result = jdbcTemplate.queryForObject(query, Integer.class);
		} catch (Exception ex){
			ex.printStackTrace();
			logger.info("getNextRevaluationId::"+ex.getMessage());
			throw new RuntimeException("Can not fatch next RevaluationId");
			
		}
		logger.info("result:" + result);
		
		return result;
	}

}
