package org.pgr.batch.repository.contract;

import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@Getter
public class ComplaintTypeServiceResponse {

    private ResponseInfo responseInfo;

	private List<ComplaintTypeResponse> complaintTypes;

}
