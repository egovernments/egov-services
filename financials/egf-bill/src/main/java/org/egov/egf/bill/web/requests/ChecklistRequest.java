package org.egov.egf.bill.web.requests;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.web.contract.ChecklistContract;

public @Data class ChecklistRequest {
    private RequestInfo requestInfo = new RequestInfo();
    private List<ChecklistContract> checklists = new ArrayList<ChecklistContract>();
}