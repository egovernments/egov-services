package org.egov.pgrrest.master.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.egov.pgrrest.master.model.ReceivingCenterType;
import org.egov.pgrrest.master.model.ReceivingModeType;
import org.egov.pgrrest.master.repository.builder.ReceivingCenterTypeQueryBuilder;
import org.egov.pgrrest.master.repository.builder.ReceivingModeTypeQueryBuilder;
import org.egov.pgrrest.master.repository.rowmapper.ReceivingModeTypeRowMapper;
import org.egov.pgrrest.master.web.contract.ReceivingModeTypeGetReq;
import org.egov.pgrrest.master.web.contract.ReceivingModeTypeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReceivingModeTypeRepository {
	
	
	 public static final Logger LOGGER = LoggerFactory.getLogger(ReceivingModeTypeRepository.class);

	    @Autowired
	    private JdbcTemplate jdbcTemplate;
	    
	    @Autowired
	    private ReceivingModeTypeQueryBuilder receivingModeTypeQueryBuilder;
	    
	    
	    @Autowired
	    private ReceivingModeTypeRowMapper receivingModeRowMapper;

	    
	    public ReceivingModeTypeReq persistReceivingModeType(final ReceivingModeTypeReq modeTypeRequest) {
	        LOGGER.info("ReceivingModeType Create Request::" + modeTypeRequest);
	        final String receivingModeTypeInsert = ReceivingModeTypeQueryBuilder.insertReceivingModeTypeQuery();
	        final ReceivingModeType modeType = modeTypeRequest.getModeType();
	        final Object[] obj = new Object[] {modeType.getCode(), modeType.getName(),
	        		modeType.getDescription(), modeType.getActive(),modeType.getVisible(),
	                Long.valueOf(modeTypeRequest.getRequestInfo().getUserInfo().getId()),
	                Long.valueOf(modeTypeRequest.getRequestInfo().getUserInfo().getId()),
	                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
	                modeType.getTenantId() };
	        jdbcTemplate.update(receivingModeTypeInsert, obj);
	        return modeTypeRequest;
	    }

	    public ReceivingModeTypeReq persistModifyReceivingModeType(final ReceivingModeTypeReq modeTypeRequest) {
	        LOGGER.info("ReceivingModeType Update Request::" + modeTypeRequest);
	        final String receivingCenterTypeUpdate = ReceivingModeTypeQueryBuilder.updateReceivingModeTypeQuery();
	        final ReceivingModeType modeType = modeTypeRequest.getModeType();
	        final Object[] obj = new Object[] { modeType.getName(), modeType.getDescription(), modeType.getActive(),
	                Long.valueOf(modeTypeRequest.getRequestInfo().getUserInfo().getId()),
	                new Date(new java.util.Date().getTime()), modeType.getCode() };
	        jdbcTemplate.update(receivingCenterTypeUpdate, obj);
	        return modeTypeRequest;

	    }
	    
	    
	    public List<ReceivingModeType> getAllReceivingTypes(final ReceivingModeTypeGetReq modeTypeGetRequest) {
	    	 LOGGER.info("ReceivingModeType search Request::" + modeTypeGetRequest);
	        final List<Object> preparedStatementValues = new ArrayList<>();
	        final String queryStr = receivingModeTypeQueryBuilder.getQuery(modeTypeGetRequest, preparedStatementValues);
	        final List<ReceivingModeType> receivingModeTypes = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
	        		receivingModeRowMapper);
	        return receivingModeTypes;
	    }
	    
	    
}
