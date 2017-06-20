package org.egov.persistence.repository.builder;

import java.util.ArrayList;
import java.util.List;

import org.egov.domain.model.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageRepositoryQueryBuilder {
	public String getDeleteQuery(List<Message> messageList){
		String deleteQuery = "DELETE FROM message WHERE locale = ? AND tenantid = ? AND code IN ";
		StringBuilder builder = new StringBuilder(deleteQuery);
		for(int i=0; i<messageList.size();i++){
			if(i==0 && messageList.size()-1 == 0){
				builder.append("('");
				builder.append(messageList.get(i).getCode()+"')");
			} else if(i==0) {
				builder.append("('");
				builder.append(messageList.get(i).getCode()+"','");
			} else if(i==messageList.size()-1){
				builder.append(messageList.get(i).getCode()+"')");
			} else {
				builder.append(messageList.get(i).getCode()+"','");
			}
		}
		List<String> moduleList = new ArrayList<>();
		for(int i=0; i<messageList.size();i++){
			if(!moduleList.contains(messageList.get(i).getModule())){
				moduleList.add(messageList.get(i).getModule());
			}
		}
		if(moduleList.size()==1){
			builder.append("AND module = '"+moduleList.get(0)+"'");
		} else if(moduleList.size() > 1) {
			builder.append("AND module IN (");
			for(int i=0; i< moduleList.size(); i++){
				builder.append("'"+moduleList.get(i)+"'");
				if(i != moduleList.size()-1){
					builder.append(",");
				}
			}
			builder.append(")");
		}
		return builder.toString();
	}
	
	public String getQueryForBatchInsert(){
		return "INSERT INTO message (id, locale, code, message, tenantid, module) values (NEXTVAL('SEQ_MESSAGE'),?,?,?,?,?)";
	}

}
