package org.egov.asset.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.model.Pagination;
import org.egov.asset.model.Voucher;
import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class VoucherResponse {

	@JsonProperty("responseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("vouchers")
	private List<Voucher> vouchers = new ArrayList<Voucher>();
	
	@JsonProperty("page")
	private Pagination page = null;
	
}
