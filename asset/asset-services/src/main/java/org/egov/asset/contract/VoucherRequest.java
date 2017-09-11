package org.egov.asset.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.model.RequestInfo;
import org.egov.asset.model.Voucher;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class VoucherRequest {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("Vouchers")
	private List<Voucher> vouchers = new ArrayList<>();

}
