package org.egov.persistence.repository.builder;

import java.util.List;

import org.egov.web.contract.CreateMessagesRequest;
import org.springframework.stereotype.Component;

@Component
public class MessageRepositoryQueryBuilder {
	
	
	public String getDeleteQuery(CreateMessagesRequest createMessagesRequest){
		List<org.egov.web.contract.Message> messageList = createMessagesRequest.getMessages();
		String deleteQuery = "DELETE FROM message WHERE locale = ? AND tenantid = ? AND code IN  ";
		StringBuilder builder = new StringBuilder(deleteQuery);
		for(int i=0; i<messageList.size();i++){
			if(i==0){
				builder.append("('");
				builder.append(messageList.get(i).getCode()+"','");
			} else if(i==messageList.size()-1){
				builder.append(messageList.get(i).getCode()+"')");
			} else {
				builder.append(messageList.get(i).getCode()+"','");
			}
		}
    	final Object[] obj = new Object[] {  };
		return builder.toString();
	}
	
	public String getQueryForBatchInsert(){
		return "INSERT INTO message (id, locale, code, message, tenantid, module) values (NEXTVAL('SEQ_MESSAGE'),?,?,?,?,?)";
	}

}
