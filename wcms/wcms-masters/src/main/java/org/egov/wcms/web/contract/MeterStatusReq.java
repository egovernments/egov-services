package org.egov.wcms.web.contract;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.model.MeterStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class MeterStatusReq {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("MeterStatus")
	private List<MeterStatus> meterStatus;
}
