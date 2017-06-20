package org.egov.persistence.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.egov.domain.model.Tenant;
import org.egov.persistence.entity.Message;
import org.egov.persistence.repository.builder.MessageRepositoryQueryBuilder;
import org.egov.web.contract.NewMessagesRequest;
import org.egov.web.contract.RequestInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;


@RunWith(MockitoJUnitRunner.class)
public class MessageRepositoryTest {

    private static final String TENANT_ID = "tenant_123";
    private static final String MR_IN = "MR_IN";

    @Mock
    private MessageJpaRepository messageJpaRepository;
    
    @Mock
	private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private MessageRepository messageRepository;
    
    @Mock
    private MessageRepositoryQueryBuilder messageQueryBuilder;

    @Test
    public void test_should_save_messages() {
        List<org.egov.domain.model.Message> domainMessages = getDomainMessages();

        messageRepository.save(domainMessages);

        verify(messageJpaRepository).save(anyListOf(Message.class));
    }

    List<org.egov.domain.model.Message> getDomainMessages() {
        org.egov.domain.model.Message message1 = org.egov.domain.model.Message.builder()
            .message("OTP यशस्वीपणे प्रमाणित")
            .code("core.msg.OTPvalidated")
            .locale(MR_IN)
            .tenant(new Tenant(TENANT_ID))
            .build();
        org.egov.domain.model.Message message2 = org.egov.domain.model.Message.builder()
            .message("प्रतिमा यशस्वीरित्या अपलोड")
            .code("core.lbl.imageupload")
            .locale(MR_IN)
            .tenant(new Tenant(TENANT_ID))
            .build();
        return Arrays.asList(message1, message2);
    }
    
    @Test(expected = Exception.class)
    public void test_should_create_new_messages() {
    	List<org.egov.web.contract.Message> myMessages = getMyMessages();
    	NewMessagesRequest newMessagesRequest = new NewMessagesRequest(new RequestInfo(), "LOCALE", myMessages, "TENANTID");
    	int[] values = {1,1};
    	when(messageQueryBuilder.getQueryForBatchInsert()).thenReturn("query");
    	when(jdbcTemplate.batchUpdate(any(String.class), any(BatchPreparedStatementSetter.class))).thenReturn(values);
    	assertTrue(messageRepository.createMessage(newMessagesRequest.getLocale(), newMessagesRequest.getTenantId(),getOnlyMessages(newMessagesRequest) ));
    }
    
    @Test(expected = Exception.class)
    public void test_should_delete_existing_messages() { 
    	List<org.egov.web.contract.Message> myMessages = getMyMessages();
    	NewMessagesRequest newMessagesRequest = new NewMessagesRequest(new RequestInfo(), "LOCALE", myMessages, "TENANTID");
    	when(messageQueryBuilder.getDeleteQuery(getOnlyMessages(newMessagesRequest) )).thenReturn("query");
    	when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
    	assertTrue(messageRepository.deleteMessages(newMessagesRequest.getLocale(), newMessagesRequest.getTenantId(),getOnlyMessages(newMessagesRequest) ));
    }
    
    private List<org.egov.web.contract.Message> getMyMessages(){
    	org.egov.web.contract.Message msg1 = org.egov.web.contract.Message.builder()
    			.code("codeOne")
    			.message("messageOne")
    			.module("moduleOne")
    			.tenantId("tenantOne")
    			.build();
    	org.egov.web.contract.Message msg2 = org.egov.web.contract.Message.builder()
    			.code("codeTwo")
    			.message("messageTwo")
    			.module("moduleTwo")
    			.tenantId("tenantTwo")
    			.build();
    	return (Arrays.asList(msg1,msg2));
    }
    
    private List<org.egov.domain.model.Message> getOnlyMessages(NewMessagesRequest newMessagesRequest){
    	List<org.egov.web.contract.Message> requestMessages = newMessagesRequest.getMessages();
    	List<org.egov.domain.model.Message> persistMessages = new ArrayList<>();
    	if(requestMessages.size() > 0){
    		Iterator<org.egov.web.contract.Message> itr = requestMessages.iterator();
    		while(itr.hasNext()){
    			org.egov.web.contract.Message msg = itr.next();
    			org.egov.domain.model.Message myMessage = new org.egov.domain.model.Message(msg.getCode(), msg.getMessage(), new Tenant(newMessagesRequest.getTenantId()), newMessagesRequest.getLocale(), msg.getModule());
    			persistMessages.add(myMessage);
    		}
    	}
    	return persistMessages;
    }
}