package org.egov.demand.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.repository.builder.TaxHeadMasterQueryBuilder;
import org.egov.demand.repository.rowmapper.TaxHeadMasterRowMapper;
import org.egov.demand.web.contract.TaxHeadMasterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class TaxHeadMasterRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private TaxHeadMasterQueryBuilder taxHeadMasterQueryBuilder;
	
	@Autowired
	private TaxHeadMasterRowMapper taxHeadMasterRowMapper;
	
	public List<TaxHeadMaster> findForCriteria(TaxHeadMasterCriteria taxHeadMasterCriteria) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String queryStr = taxHeadMasterQueryBuilder.getQuery(taxHeadMasterCriteria, preparedStatementValues);
		List<TaxHeadMaster> taxHeadMaster = null;
		try {
			log.debug("queryStr::" + queryStr + "preparedStatementValues::" + preparedStatementValues.toString());
			taxHeadMaster = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), taxHeadMasterRowMapper);
			log.debug("TaxHeadRepository::" + taxHeadMaster);
		} catch (Exception ex) {
			log.debug("the exception from findforcriteria : " + ex);
		}
		return taxHeadMaster;
	}
	
	public Integer getNextTaxHeadMasterId() {
		String query = "SELECT nextval('seq_egbs_taxHeadMaster')";
		Integer result = jdbcTemplate.queryForObject(query, Integer.class);
		return result;
	}
	
	public String getTaxHeadMasterCode() {
		String query = "SELECT nextval('seq_egbs_taxHeadMastercode')";
		Integer result = jdbcTemplate.queryForObject(query, Integer.class);
		log.debug("result:" + result);
		StringBuilder code = null;
		try {
			code = new StringBuilder(String.format("%06d", result));
		} catch (Exception ex) {
			log.debug("the exception from seq number gen for code : " + ex);
		}
		return code.toString();
	}

	@Transactional
	public List<TaxHeadMaster> create(TaxHeadMasterRequest taxHeadMasterRequest){
		
		RequestInfo requestInfo = taxHeadMasterRequest.getRequestInfo();
		List<TaxHeadMaster> taxHeadMasters = taxHeadMasterRequest.getTaxHeadMasters();
		
		log.debug("create requestInfo:"+ requestInfo);
		log.debug("create taxHeadMasters:"+ taxHeadMasters);
		
		
		jdbcTemplate.batchUpdate(taxHeadMasterQueryBuilder.getInsertQuery(), new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				TaxHeadMaster taxHeadMaster = taxHeadMasters.get(index);


				ps.setString(1, taxHeadMaster.getId());
				ps.setString(2, taxHeadMaster.getTenantId());
				ps.setString(3, taxHeadMaster.getCategory().toString());
				ps.setString(4, taxHeadMaster.getService());
				ps.setString(5, taxHeadMaster.getName());
				ps.setString(6, taxHeadMaster.getCode());
				ps.setBoolean(7, taxHeadMaster.getIsDebit());
				ps.setBoolean(8, taxHeadMaster.getIsActualDemand());
				ps.setInt(9, taxHeadMaster.getOrder());
				ps.setObject(10, taxHeadMaster.getValidFrom());
				ps.setObject(11, taxHeadMaster.getValidTill());
				ps.setString(12, requestInfo.getUserInfo().getId().toString());
				ps.setLong(13, new Date().getTime());
				ps.setString(14, requestInfo.getUserInfo().getId().toString());
				ps.setLong(15, new Date().getTime());
			}
			
			@Override
			public int getBatchSize() {
				return taxHeadMasters.size();
			}
		});
		return taxHeadMasters;
	}
	
	
	
	public List<TaxHeadMaster> update(TaxHeadMasterRequest taxHeadMasterRequest) {
		List<TaxHeadMaster> taxHeadMasters = taxHeadMasterRequest.getTaxHeadMasters();

		/*if(taxHeadMasters!=null){
			String query = taxHeadMasterQueryBuilder.getUpdateQuery();
			List<Object[]> taxHeadsBatchArgs = new ArrayList<>();
			int taxHeadsCount=taxHeadMasters.size();
			RequestInfo requestInfo = taxHeadMasterRequest.getRequestInfo();
			for (TaxHeadMaster master:taxHeadMasters) {
				Object[] demandRecord = { master.getCategory().toString(),
						master.getService(),
						master.getName(),
						master.getCode(),
						master.getGlCode(),
						master.getIsDebit(),
						master.getIsActualDemand(),
						requestInfo.getUserInfo().getId(),
//						master.getTaxPeriod().getId() ,
						new java.util.Date().getTime(),
						master.getTenantId()};
				taxHeadsBatchArgs.add(demandRecord);
			}

			try {
				jdbcTemplate.batchUpdate(query, taxHeadsBatchArgs);
			} catch (DataAccessException ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex.getMessage());
			}

		}*/
		
		return taxHeadMasters;
	}
}
