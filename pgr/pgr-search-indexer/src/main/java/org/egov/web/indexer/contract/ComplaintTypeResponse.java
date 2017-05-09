package org.egov.web.indexer.contract;

import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@Getter
public class ComplaintTypeResponse {

    private ResponseInfo responseInfo;

    private List<ComplaintType> complaintTypes;

}