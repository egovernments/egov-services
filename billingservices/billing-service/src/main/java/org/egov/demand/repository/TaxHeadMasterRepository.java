package org.egov.demand.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.repository.querybuilder.TaxHeadMasterQueryBuilder;
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
		return jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), taxHeadMasterRowMapper);
	}
	
	@Transactional
	public List<TaxHeadMaster> create(TaxHeadMasterRequest taxHeadMasterRequest){
		
		RequestInfo requestInfo = taxHeadMasterRequest.getRequestInfo();
		List<TaxHeadMaster> taxHeadMasters = taxHeadMasterRequest.getTaxHeadMasters();
		log.debug("create requestInfo:"+ requestInfo);
		log.debug("create taxHeadMasters:"+ taxHeadMasters);
		
		jdbcTemplate.batchUpdate(taxHeadMasterQueryBuilder.Insert_Query, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				TaxHeadMaster taxHeadMaster = taxHeadMasters.get(index);

				ps.setString(1, taxHeadMaster.getId());
				ps.setString(2, taxHeadMaster.getTenantId());
				
				if(taxHeadMaster.getCategory().toString()!=null)
					ps.setString(3, taxHeadMaster.getCategory().toString());
				
				ps.setString(4, taxHeadMaster.getService());
				ps.setString(5, taxHeadMaster.getName());
				ps.setString(6, taxHeadMaster.getCode());
				ps.setBoolean(7, taxHeadMaster.getIsDebit());
				ps.setBoolean(8, taxHeadMaster.getIsActualDemand());
				ps.setObject(9, taxHeadMaster.getOrder());
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
	
	@Transactional
	public List<TaxHeadMaster> update(TaxHeadMasterRequest taxHeadMasterRequest) {
		RequestInfo requestInfo = taxHeadMasterRequest.getRequestInfo();
		List<TaxHeadMaster> taxHeadMasters = taxHeadMasterRequest.getTaxHeadMasters();
		log.debug("update requestInfo:"+ requestInfo);
		log.debug("update taxHeadMasters:"+ taxHeadMasters);
		
		jdbcTemplate.batchUpdate(taxHeadMasterQueryBuilder.Update_Query, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				TaxHeadMaster taxHeadMaster = taxHeadMasters.get(index);
				if(taxHeadMaster.getCategory().toString()!=null)
					ps.setString(1, taxHeadMaster.getCategory().toString());
				
				ps.setString(2, taxHeadMaster.getService());
				ps.setString(3, taxHeadMaster.getName());
				ps.setString(4, taxHeadMaster.getCode());
				ps.setBoolean(5, taxHeadMaster.getIsDebit());
				ps.setBoolean(6, taxHeadMaster.getIsActualDemand());
				ps.setObject(7, taxHeadMaster.getOrder());
				ps.setObject(8, taxHeadMaster.getValidFrom());
				ps.setObject(9, taxHeadMaster.getValidTill());
				ps.setString(10, requestInfo.getUserInfo().getId().toString());
				ps.setLong(11, new Date().getTime());
				ps.setString(12, taxHeadMaster.getTenantId());
				ps.setString(13, taxHeadMaster.getId());
			}
			@Override
			public int getBatchSize() {
				return taxHeadMasters.size();
			}
		});
		return taxHeadMasters;
		}
}
