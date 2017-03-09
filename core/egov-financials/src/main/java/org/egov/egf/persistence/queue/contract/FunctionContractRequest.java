package org.egov.egf.persistence.queue.contract;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public @Data class FunctionContractRequest {
	private RequestInfo requestInfo = new RequestInfo();
	private List<FunctionContract> functions = new ArrayList<FunctionContract>();
	private FunctionContract function = new FunctionContract();
	private Pagination page = new Pagination();
}