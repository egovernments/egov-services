package org.egov.pgrrest.master.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.egov.pgrrest.master.model.ReceivingCenterType;
import org.egov.pgrrest.master.repository.builder.ReceivingCenterTypeQueryBuilder;
import org.egov.pgrrest.master.repository.rowmapper.ReceivingCenterTypeRowMapper;
import org.egov.pgrrest.master.web.contract.ReceivingCenterTypeGetReq;
import org.egov.pgrrest.master.web.contract.ReceivingCenterTypeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReceivingCenterTypeRepository {
	
    public static final Logger LOGGER = LoggerFactory.getLogger(ReceivingCenterTypeRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private ReceivingCenterTypeQueryBuilder receivingCenterQueryBuilder;
    
    @Autowired
    private ReceivingCenterTypeRowMapper receivingCenterRowMapper;
    
    
    public ReceivingCenterTypeReq persistReceivingCenterType(final ReceivingCenterTypeReq centerTypeRequest) {
        LOGGER.info("ReceivingCenterType Create Request::" + centerTypeRequest);
        final String receivingCenterTypeInsert = ReceivingCenterTypeQueryBuilder.insertReceivingCenterTypeQuery();
        final ReceivingCenterType centerType = centerTypeRequest.getCenterType();
        final Object[] obj = new Object[] {centerType.getCode(), centerType.getName(),
        		centerType.getDescription(), centerType.getActive(),centerType.getVisible(),
                Long.valueOf(centerTypeRequest.getRequestInfo().getUserInfo().getId()),
                Long.valueOf(centerTypeRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
                centerType.getTenantId() };
        jdbcTemplate.update(receivingCenterTypeInsert, obj);
        return centerTypeRequest;
    }

    public ReceivingCenterTypeReq persistModifyReceivingCenterType(final ReceivingCenterTypeReq centerTypeRequest) {
        LOGGER.info("ReceivingCenterType Update Request::" + centerTypeRequest);
        final String receivingCenterTypeUpdate = ReceivingCenterTypeQueryBuilder.updateReceivingCenterTypeQuery();
        final ReceivingCenterType centerType = centerTypeRequest.getCenterType();
        final Object[] obj = new Object[] { centerType.getName(), centerType.getDescription(), centerType.getActive(),
                Long.valueOf(centerTypeRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), centerType.getCode() };
        jdbcTemplate.update(receivingCenterTypeUpdate, obj);
        return centerTypeRequest;

    }
    
    
    public List<ReceivingCenterType> getAllReceivingTypes(final ReceivingCenterTypeGetReq centerTypeGetRequest) {
    	 LOGGER.info("ReceivingCenterType search Request::" + centerTypeGetRequest);
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = receivingCenterQueryBuilder.getQuery(centerTypeGetRequest, preparedStatementValues);
        final List<ReceivingCenterType> receivingCenterTypes = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
        		receivingCenterRowMapper);
        return receivingCenterTypes;
    }
    
    
}
