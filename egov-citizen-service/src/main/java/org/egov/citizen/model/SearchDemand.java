package org.egov.citizen.model;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDemand {
	
    private String url;
   	private RequestInfo requestInfo;
	private long applicationFee;
	private List<String> queryAppend;
	private List<String> result;
	private GenerateId generateId;
	private CreateDemandRequest createDemandRequest;

}
