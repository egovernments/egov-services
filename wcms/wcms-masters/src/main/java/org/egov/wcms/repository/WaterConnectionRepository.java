package org.egov.wcms.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.wcms.model.DocumentOwner;
import org.egov.wcms.model.MeterReading;
import org.egov.wcms.repository.builder.WaterConnectionQueryBuilder;
import org.egov.wcms.web.contract.WaterConnectionReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WaterConnectionRepository {
	
    public static final Logger LOGGER = LoggerFactory.getLogger(WaterConnectionRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private WaterConnectionQueryBuilder waterConnectionQueryBuilder;
    
    
    public WaterConnectionReq persistConnection(WaterConnectionReq waterConnectionRequest){
    	
    	String insertQuery="";
    	if(waterConnectionRequest.getConnection().getLegacyConsumerNumber()!=null){
    		
    		insertQuery = waterConnectionQueryBuilder.insertLegacyConnectionQuery();
    	} else if(waterConnectionRequest.getConnection().getParentConnectionId() != 0){
        	
    		insertQuery = waterConnectionQueryBuilder.insertAdditionalConnectionQuery();
    		
    	} else {
    		
    		insertQuery = waterConnectionQueryBuilder.insertConnectionQuery();
    	}
    	
    	 final String query= insertQuery;
    	
    	long connectionId = 0L;
    	try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					String[] returnValColumn = new String[] { "id" };
					PreparedStatement statement = connection.prepareStatement(query,
							returnValColumn);
					statement.setString(1,waterConnectionRequest.getConnection().getTenantId());
					statement.setString(2,waterConnectionRequest.getConnection().getConnectionType());
					statement.setString(3,waterConnectionRequest.getConnection().getBillingType());
					statement.setLong(4, 3L); // waterConnectionRequest.getConnection().getCategoryType());
					statement.setLong(5, 1L); // waterConnectionRequest.getConnection().getHscPipeSizeType());
					statement.setString(6,waterConnectionRequest.getConnection().getSupplyType());
					statement.setLong(7,1L);  // waterConnectionRequest.getConnection().getSourceType());
					statement.setString(8,waterConnectionRequest.getConnection().getConnectionStatus());
					statement.setDouble(9,waterConnectionRequest.getConnection().getSumpCapacity());
					statement.setInt(10,waterConnectionRequest.getConnection().getNumberOfTaps());
					statement.setInt(11,waterConnectionRequest.getConnection().getNumberOfPersons());
					statement.setString(12,waterConnectionRequest.getConnection().getAcknowledgementNumber());
					statement.setLong(13,waterConnectionRequest.getRequestInfo().getUserInfo().getId());
					statement.setLong(14,waterConnectionRequest.getRequestInfo().getUserInfo().getId());
					statement.setDate(15,new Date(new java.util.Date().getTime()));
					statement.setDate(16,new Date(new java.util.Date().getTime()));
					statement.setLong(17,waterConnectionRequest.getConnection().getProperty().getId());
					statement.setString(18,waterConnectionRequest.getConnection().getProperty().getUsageType());
					statement.setString(19,waterConnectionRequest.getConnection().getProperty().getPropertyType());
                     statement.setString(20,"AddressTest"); // waterConnectionRequest.getConnection().getProperty().getAddress());
                     statement.setString(21, waterConnectionRequest.getConnection().getDonationCharge());
                     
					if(waterConnectionRequest.getConnection().getLegacyConsumerNumber()!=null || waterConnectionRequest.getConnection().getParentConnectionId() != 0){
			    	
						statement.setString(22,waterConnectionRequest.getConnection().getLegacyConsumerNumber());
						statement.setString(23,waterConnectionRequest.getConnection().getConsumerNumber());
			    	} 
			
					if(waterConnectionRequest.getConnection().getParentConnectionId() != 0){
						
						statement.setLong(24,waterConnectionRequest.getConnection().getParentConnectionId());
					}
					
					//Please verify if there's proper validation on all these fields to avoid NPE.
					
					return statement;
				}
			}, keyHolder);
			
			connectionId = keyHolder.getKey().longValue();
		} catch (Exception e) {
    		LOGGER.error("Inserting Connection Object failed!", e);
		}

    	if(connectionId > 0 && waterConnectionRequest.getConnection().getLegacyConsumerNumber() == null ){
    	List<Object[]> values = new ArrayList<>();
    	for(DocumentOwner document: waterConnectionRequest.getConnection().getDocuments()){
    		Object[] obj = {document.getDocument().getId(),
    		            	document.getName(),
    		            	document.getFileStoreId(),
    		            	waterConnectionRequest.getConnection().getId(),
    		            	waterConnectionRequest.getConnection().getTenantId()};
    		
    		values.add(obj);
    	}
    	String insertDocsQuery = WaterConnectionQueryBuilder.insertDocumentQuery();
    	try{
    		jdbcTemplate.batchUpdate(insertDocsQuery, values);
    	}catch(Exception e){
    		LOGGER.error("Inserting documents failed!", e);
    	}
    } else if(connectionId > 0){
    	
    	String insertMeterQuery = waterConnectionQueryBuilder.insertMeterQuery();
    	try{
    	 Object[] obj = new Object[] {waterConnectionRequest.getConnection().getMeter().getMeterMake(),connectionId,waterConnectionRequest.getConnection().getTenantId(),
    			 waterConnectionRequest.getRequestInfo().getUserInfo().getId(),new Date(new java.util.Date().getTime())};

         jdbcTemplate.update(insertMeterQuery, obj);
    	}catch(Exception e){
     		LOGGER.error("Inserting Meter failed!", e);
    	}
    	String insertMeterReadingQuery = waterConnectionQueryBuilder.insertMeterReadingQuery();
    	List<Object[]> values = new ArrayList<>();
    	for(MeterReading meterReading: waterConnectionRequest.getConnection().getMeterReadings()){
    	 
    		Object[] obj = {connectionId,
    				meterReading.getReading(),waterConnectionRequest.getConnection().getTenantId(),waterConnectionRequest.getRequestInfo().getUserInfo().getId(),
    				meterReading.getAuditDetails().getCreatedDate(),waterConnectionRequest.getRequestInfo().getUserInfo().getId(),meterReading.getAuditDetails().getCreatedDate()};
    		
    		values.add(obj);
    	}
    	try{
    		jdbcTemplate.batchUpdate(insertMeterReadingQuery, values);
    	}catch(Exception e){
    		LOGGER.error("Inserting documents failed!", e);
    	}
    	
    }
    	
    	
    	
    	
	LOGGER.info("Insertion to document owner table left unattempted upon failure of connection object insertion.");
    return waterConnectionRequest;
    
   }
    
    

}
