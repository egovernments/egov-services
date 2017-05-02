package org.egov.workflow.web.contract;


import lombok.Getter;

import java.util.List;

@Getter
public class ComplaintTypeServiceResponse {

    private ResponseInfo responseInfo;

	private List<ComplaintTypeResponse> complaintTypes;

}
