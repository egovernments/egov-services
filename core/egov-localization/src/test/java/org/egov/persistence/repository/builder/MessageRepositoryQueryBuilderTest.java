package org.egov.persistence.repository.builder;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.egov.domain.model.Message;
import org.egov.domain.model.Tenant;
import org.egov.web.contract.NewMessagesRequest;
import org.egov.web.contract.RequestInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageRepositoryQueryBuilderTest {
	
	@InjectMocks
    private MessageRepositoryQueryBuilder messageQueryBuilder;
	
	@Test
	public void test_should_return_valid_delete_query() {
		String expectThisString = "DELETE FROM message WHERE locale = ? AND tenantid = ? AND code IN ('codeOne','codeTwo')";
		List<org.egov.web.contract.Message> myMessages = getMyMessages();
		NewMessagesRequest messageRequest = new NewMessagesRequest(new RequestInfo(), "LOCALE", myMessages, "TenantId");
		assertTrue(expectThisString.equals(messageQueryBuilder.getDeleteQuery(getOnlyMessages(messageRequest))));
	}
	
	@Test
	public void test_should_return_valid_batch_insert_query() {
		String expectThisString = "INSERT INTO message (id, locale, code, message, tenantid, module) values (NEXTVAL('SEQ_MESSAGE'),?,?,?,?,?)";
		assertTrue(expectThisString.equals(messageQueryBuilder.getQueryForBatchInsert()));
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

	private List<Message> getOnlyMessages(NewMessagesRequest newMessagesRequest){
    	List<org.egov.web.contract.Message> requestMessages = newMessagesRequest.getMessages();
    	List<Message> persistMessages = new ArrayList<>();
    	if(requestMessages.size() > 0){
    		Iterator<org.egov.web.contract.Message> itr = requestMessages.iterator();
    		while(itr.hasNext()){
    			org.egov.web.contract.Message msg = itr.next();
    			Message myMessage = new Message(msg.getCode(), msg.getMessage(), new Tenant(newMessagesRequest.getTenantId()), newMessagesRequest.getLocale(), msg.getModule());
    			persistMessages.add(myMessage);
    		}
    	}
    	return persistMessages;
    }
}
