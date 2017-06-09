package org.egov.pgrrest.master.web.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.pgrrest.master.model.ReceivingModeType;

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
public class ReceivingModeTypeRes {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("ReceivingModeType")
	private List<ReceivingModeType> centerTypes = new ArrayList<>();
	
	
}
