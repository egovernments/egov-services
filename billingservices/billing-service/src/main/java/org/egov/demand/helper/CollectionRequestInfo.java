package org.egov.demand.helper;

import java.util.Date;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionRequestInfo {

	 private String apiId;

	    private String ver;

	    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	    private Date ts;

	    private String action;

	    private String did;

	    private String key;

	    private String msgId;

	    private String authToken;

	    private String correlationId;

	    private User userInfo;
	    
	    public  RequestInfo toRequestInfo(){
	    	return RequestInfo.builder().apiId(apiId).authToken(authToken).action(action)
	    			.correlationId(correlationId).did(did).msgId(msgId).userInfo(userInfo)
	    			.key(key).ver(ver).ts(ts.getTime()).build();
	    }
}
