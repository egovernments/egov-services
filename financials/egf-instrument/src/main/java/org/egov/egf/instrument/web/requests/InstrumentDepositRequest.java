package org.egov.egf.instrument.web.requests;

import lombok.Data;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Auditable;

public @Data class InstrumentDepositRequest extends Auditable{
	private RequestInfo requestInfo = new RequestInfo();
	private String instrumentDepositId;
}