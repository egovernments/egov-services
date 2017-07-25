package org.egov.egf.master.web.requests;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.master.web.contract.AccountCodePurposeContract;

import lombok.Data;

public @Data class AccountCodePurposeRequest {
	private RequestInfo requestInfo = new RequestInfo();
	private List<AccountCodePurposeContract> accountCodePurposes = new ArrayList<AccountCodePurposeContract>();
}