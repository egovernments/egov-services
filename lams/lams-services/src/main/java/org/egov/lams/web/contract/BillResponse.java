package org.egov.lams.web.contract;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.egov.lams.model.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

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
