package org.egov.asset.repository;


import java.util.ArrayList;
import java.util.List;

import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.DisposalCriteria;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.repository.builder.DisposalQueryBuilder;
import org.egov.asset.repository.rowmapper.DisposalRowMapper;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DisposalRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(DisposalRepository.class);
	
	@Autowired
	private DisposalQueryBuilder disposalQueryBuilder;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DisposalRowMapper disposalRowMapper;
	
	public List<Disposal> search(DisposalCriteria disposalCriteria) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String queryStr = disposalQueryBuilder.getQuery(disposalCriteria, preparedStatementValues);
		List<Disposal> disposals = null;
		try {
			LOGGER.info("queryStr::" + queryStr + "preparedStatementValues::" + preparedStatementValues.toString());
			disposals = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), disposalRowMapper);
			LOGGER.info("DisposalRepository::" + disposals);
		} catch (Exception ex) {
			LOGGER.info("the exception from findforcriteria : " + ex);
		}
		return disposals;
	}
	
	public void create(DisposalRequest disposalRequest){
		
		RequestInfo requestInfo = disposalRequest.getRequestInfo();
		Disposal disposal = disposalRequest.getDisposal();
		
		Object[] values = {disposal.getId(),disposal.getTenantId(),disposal.getAssetId(),disposal.getBuyerName(),
				disposal.getBuyerAddress(),disposal.getDisposalReason(),disposal.getDisposalDate(),
				disposal.getPanCardNumber(),disposal.getAadharCardNumber(),disposal.getAssetCurrentValue(),
				disposal.getSaleValue(),disposal.getTransactionType().toString(),
				disposal.getAssetSaleAccount(),requestInfo.getUserInfo().getId(),
				System.currentTimeMillis(),requestInfo.getUserInfo().getId(),System.currentTimeMillis()};
		
		try{
			jdbcTemplate.update(DisposalQueryBuilder.INSERT_QUERY, values);
		} catch (Exception ex) {
			LOGGER.info("DisposalRepository:",ex);
			throw new RuntimeException(ex);
		}
	}
	
	public Integer getNextDisposalId(){
		
		String query = "SELECT nextval('seq_egasset_disposal')";
		Integer result = null;
		try{
			 result = jdbcTemplate.queryForObject(query, Integer.class);
		} catch (Exception ex){
			ex.printStackTrace();
			LOGGER.info("getNextRevaluationId::"+ex.getMessage());
			throw new RuntimeException("Can not fatch next RevaluationId");
		}
		LOGGER.info("result:" + result);
		
		return result;
	}
	
	
}
