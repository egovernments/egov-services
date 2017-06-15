package org.egov.persistence.repository;


import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.domain.model.Message;
import org.egov.domain.model.Tenant;
import org.egov.persistence.repository.builder.MessageRepositoryQueryBuilder;
import org.egov.web.contract.CreateMessagesRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageRepository {
	
    private MessageJpaRepository messageJpaRepository;
    
    private MessageCacheRepository messageCacheRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(MessageRepository.class);
    
    @Autowired
	private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private MessageRepositoryQueryBuilder messageQueryBuilder;
    
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
    
    public boolean deleteMessages(CreateMessagesRequest createMessagesRequest){
    	String deleteQuery = messageQueryBuilder.getDeleteQuery(createMessagesRequest);
    	final Object[] obj = new Object[] { createMessagesRequest.getLocale(), createMessagesRequest.getTenantId() };
		int deleteStatus = jdbcTemplate.update(deleteQuery, obj);
		logger.info("Status of Delete is : " + deleteStatus);
		if(deleteStatus > 0){
			return true;
		}
		return false; 
    }
    
    public boolean createMessage(CreateMessagesRequest createMessagesRequest){
    	List<org.egov.web.contract.Message> messageList = createMessagesRequest.getMessages();
    	int[] values = null;
		try {
			values = jdbcTemplate.batchUpdate(messageQueryBuilder.getQueryForBatchInsert(),
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(java.sql.PreparedStatement statement, int i) throws SQLException {
							org.egov.web.contract.Message eachMessage = messageList.get(i);
							statement.setString(1, createMessagesRequest.getLocale());
							statement.setString(2, eachMessage.getCode());
							statement.setString(3, eachMessage.getMessage());
							statement.setString(4, createMessagesRequest.getTenantId());
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
			Tenant tenant = new Tenant(createMessagesRequest.getTenantId());
			// messageCacheRepository.bustCacheEntry(createMessagesRequest.getLocale(), tenant);
			return true;
		}
    	return false;
    }
}
