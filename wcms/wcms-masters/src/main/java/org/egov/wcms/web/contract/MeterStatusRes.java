package org.egov.wcms.web.contract;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.model.MeterStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeterStatusRes {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("MeterStatus")
	private List<MeterStatus> meterStatus;

}
