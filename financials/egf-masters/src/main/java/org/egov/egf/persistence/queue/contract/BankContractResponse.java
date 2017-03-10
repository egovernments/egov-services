package org.egov.egf.persistence.queue.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
@JsonInclude(value=Include.NON_NULL)
public @Data class BankContractResponse {
	private ResponseInfo responseInfo = new ResponseInfo();
	private List<BankContract> banks;
	private BankContract bank ;
	private Pagination page ;
}