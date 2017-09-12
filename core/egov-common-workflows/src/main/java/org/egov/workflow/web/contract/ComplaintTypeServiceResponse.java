package org.egov.workflow.web.contract;

import java.util.List;

import lombok.Getter;

@Getter
public class ComplaintTypeServiceResponse {

	private ResponseInfo responseInfo;

	private List<ComplaintTypeResponse> complaintTypes;
}
