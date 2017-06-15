package org.egov.persistence.repository.builder;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.egov.domain.model.AuditDetails;
import org.egov.web.contract.CreateMessagesRequest;
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
		CreateMessagesRequest messageRequest = new CreateMessagesRequest(new RequestInfo(), "LOCALE", myMessages, "TenantId");
		assertTrue(expectThisString.equals(messageQueryBuilder.getDeleteQuery(messageRequest)));
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
    			.auditDetails(new AuditDetails()).build();
    	org.egov.web.contract.Message msg2 = org.egov.web.contract.Message.builder()
    			.code("codeTwo")
    			.message("messageTwo")
    			.module("moduleTwo")
    			.tenantId("tenantTwo")
    			.auditDetails(new AuditDetails()).build();
    	return (Arrays.asList(msg1,msg2));
    }

}
