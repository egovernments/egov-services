package org.egov.wcms.transaction.web.contract;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.transaction.model.ConnectionDocument;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectionDocumentRes {
	
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;
	
	@JsonProperty("ConnectionDocument")
	private List<ConnectionDocument> connectionDocuments;

}
