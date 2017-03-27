package org.egov.lams.web.contract;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class BillResponse {
	@JsonProperty("ResponseInfo")
	ResponseInfo responseInfo;

	@JsonProperty("BillInfos")
	private List<BillInfo> billInfos = new ArrayList<>();

	@JsonProperty("BillXmls")
	private List<String> billXmls = new ArrayList<>();

}
