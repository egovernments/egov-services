package org.egov.persistence.repository;


import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.domain.model.Message;
import org.egov.domain.model.Tenant;
import org.egov.persistence.repository.builder.MessageRepositoryQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageRepository {
	
    private MessageJpaRepository messageJpaRepository;
    
    // private MessageCacheRepository messageCacheRepository;
    
    @Autowired
    private MessageRepositoryQueryBuilder messageQueryBuilder;
    
    private static final Logger logger = LoggerFactory.getLogger(MessageRepository.class);
    
    @Autowired
	private JdbcTemplate jdbcTemplate;
    
    public MessageRepository(MessageJpaRepository messageJpaRepository) {
        this.messageJpaRepository = messageJpaRepository;
    }

    public List<Message> findByTenantIdAndLocale(Tenant tenant, String locale) {
        return messageJpaRepository.findByTenantIdAndLocale(tenant.getTenantId(), locale).stream()
            .map(org.egov.persistence.entity.Message::toDomain).collect(Collectors.toList());
    }

    public void save(List<Message> messages) {
        final List<org.egov.persistence.entity.Message> entityMessages = messages.stream()
            .map(org.egov.persistence.entity.Message::new)
            .collect(Collectors.toList());
        messageJpaRepository.save(entityMessages);
    }
    
    public boolean deleteMessages(String locale, String tenantId, List<Message> messageList){
    	String deleteQuery = messageQueryBuilder.getDeleteQuery(messageList);
    	final Object[] obj = new Object[] { locale, tenantId };
		int deleteStatus = jdbcTemplate.update(deleteQuery, obj);
		logger.info("Status of Delete is : " + deleteStatus);
		if(deleteStatus > 0){
			return true;
		}
		return false; 
    }
    
    public boolean createMessage(String locale, String tenantId, List<Message> messageList){
    	String batchInsertQuery = messageQueryBuilder.getQueryForBatchInsert();
    	int[] values = {};
		try {
			values = jdbcTemplate.batchUpdate(batchInsertQuery,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(java.sql.PreparedStatement statement, int i) throws SQLException {
							Message eachMessage = messageList.get(i);
							statement.setString(1, locale);
							statement.setString(2, eachMessage.getCode());
							statement.setString(3, eachMessage.getMessage());
							statement.setString(4, tenantId);
							statement.setString(5, eachMessage.getModule());
						}
						@Override
						public int getBatchSize() {
							return messageList.size();
						}
					});
		} catch (Exception e) {
			logger.error("Encountered an Exception : " + e);
		}
		logger.info("Status of Insert is : " + values.length);
		if(values.length > 0){
			// Tenant tenant = new Tenant(tenantId);
			// messageCacheRepository.bustCacheEntry(createMessagesRequest.getLocale(), tenant);
			return true;
		}
    	return false;
    }
}
