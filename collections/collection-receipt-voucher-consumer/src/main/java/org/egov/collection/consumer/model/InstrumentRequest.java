package org.egov.collection.consumer.model;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;

import lombok.Data;

public @Data class InstrumentRequest {
    private RequestInfo requestInfo = new RequestInfo();
    private List<InstrumentContract> instruments = new ArrayList<InstrumentContract>();
}