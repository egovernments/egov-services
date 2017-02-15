package org.egov.egf.web.contract;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BankContractResponse {

	private ResponseInfo responseInfo = null;

	private List<BankContract> banks = new ArrayList<BankContract>();

	private Page page = new Page();

}
