package org.egov.wcms.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.egov.wcms.model.CategoryType;
import org.egov.wcms.model.CommonDataModel;
import org.egov.wcms.model.Gapcode;
import org.egov.wcms.model.PropertyTypeCategoryType;
import org.egov.wcms.repository.builder.CategoryTypeQueryBuilder;
import org.egov.wcms.repository.builder.GapcodeQueryBuilder;
import org.egov.wcms.repository.builder.PropertyTypeCategoryTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.GapcodeFormulaRowMapper;
import org.egov.wcms.repository.rowmapper.GapcodeRowMapper;
import org.egov.wcms.web.contract.CategoryTypeGetRequest;
import org.egov.wcms.web.contract.GapcodeGetRequest;
import org.egov.wcms.web.contract.GapcodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
@Slf4j
public class GapcodeRepository {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private GapcodeQueryBuilder gapcodeQueryBuilder; 
	
	@Autowired
	private GapcodeRowMapper gapcodeRowMapper;
	
	@Autowired
	private GapcodeFormulaRowMapper gapcodeFormulaRowMapper; 
	
    public GapcodeRequest persist(final GapcodeRequest gapcodeRequest) {
    	
        log.info("GapcodeRequest::" + gapcodeRequest);
        final String insertGapcode = GapcodeQueryBuilder.insertQuery();
        final List<Gapcode> gapcodeList = gapcodeRequest.getGapcode();
        final List<Map<String, Object>> batchValues = new ArrayList<>(gapcodeList.size());
        for (final Gapcode gapcode : gapcodeList){
            batchValues.add(new MapSqlParameterSource("id", gapcode.getId())
            		.addValue("code", gapcode.getId()).addValue("name", gapcode.getName())
                    .addValue("outSideUlb", gapcode.getOutSideUlb())
                    .addValue("noOfLastMonths", gapcode.getNoOfMonths())
                    .addValue("logic", gapcode.getDescription())
                    .addValue("active", gapcode.getActive())
                    .addValue("description", gapcode.getDescription())
                    .addValue("createdBy", Long.valueOf(gapcodeRequest.getRequestInfo().getUserInfo().getId()))
                    .addValue("lastUpdatedBy", Long.valueOf(gapcodeRequest.getRequestInfo().getUserInfo().getId()))
                    .addValue("createdDate", new Date().getTime())
                    .addValue("lastUpdatedDate", new Date().getTime())
                    .addValue("tenantId", gapcode.getTenantId())
                    .getValues());
        }
        namedParameterJdbcTemplate.batchUpdate(insertGapcode, batchValues.toArray(new Map[batchValues.size()]));
        return gapcodeRequest;
    }
    
    public GapcodeRequest persistUpdate(final GapcodeRequest gapcodeRequest){
    	try{
    		log.info("GapcodeRequest::" + gapcodeRequest);
    		final String updateGapcode = GapcodeQueryBuilder.updateQuery();
    		final List<Gapcode> gapcodeList = gapcodeRequest.getGapcode();
    		final List<Map<String, Object>> batchValues = new ArrayList<>(gapcodeList.size());
    		for (final Gapcode gapcode : gapcodeList){
    			batchValues.add(new MapSqlParameterSource("name", gapcode.getName())
    					.addValue("outSideUlb", gapcode.getOutSideUlb())
    					.addValue("noOfLastMonths", gapcode.getDescription())
    					.addValue("logic", gapcode.getDescription())
    					.addValue("description", gapcode.getDescription())
    					.addValue("active", gapcode.getActive())
    					.addValue("lastUpdatedBy", Long.valueOf(gapcodeRequest.getRequestInfo().getUserInfo().getId()))
    					.addValue("lastUpdatedDate", new Date().getTime()).addValue("code", gapcode.getCode()).getValues());
    		}
    		namedParameterJdbcTemplate.batchUpdate(updateGapcode, batchValues.toArray(new Map[gapcodeList.size()]));
    		
    	}
    	catch(Exception exception){
    		log.error("Exception Encountered : " + exception);  		
    	}
    	return gapcodeRequest;
    }
    
    public List<Gapcode> findForCriteria(final GapcodeGetRequest gapcodeGetRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = gapcodeQueryBuilder.getQuery(gapcodeGetRequest, preparedStatementValues);
        log.info(queryStr);
        final List<Gapcode> gapcode = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
        		gapcodeRowMapper);
        return gapcode;
    }
    
    public List<CommonDataModel> getFormulaQuery() {
    	List<CommonDataModel> commonDataModel = jdbcTemplate.query("SELECT code, name FROM egwtr_gapcode",	gapcodeFormulaRowMapper);
        return commonDataModel;
    }


}
