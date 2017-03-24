package org.egov.lams.web.contract;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.egov.lams.model.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class BillRequest {
	@JsonProperty("RequestInfo")
	RequestInfo requestInfo;

	@JsonProperty("BillInfos")
	List<BillInfo> billInfos;

}
