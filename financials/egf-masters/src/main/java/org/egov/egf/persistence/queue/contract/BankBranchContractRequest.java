package org.egov.egf.persistence.queue.contract;

import java.util.ArrayList;

import java.util.List;

import lombok.Data;

public @Data class BankBranchContractRequest {
	private RequestInfo requestInfo = new RequestInfo();
	private List<BankBranchContract> bankBranches = new ArrayList<BankBranchContract>();
	private BankBranchContract bankBranch = new BankBranchContract();
	private Pagination page = new Pagination();
}