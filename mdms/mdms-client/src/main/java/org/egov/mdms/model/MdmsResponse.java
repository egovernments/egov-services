package org.egov.mdms.model;

import java.util.List;
import java.util.Map;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.mdms.model.MasterDetail.MasterDetailBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.minidev.json.JSONArray;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MdmsResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;
	
	@JsonProperty("MdmsRes")
	private Map<String, List<Map<String, JSONArray>>> mdmsRes;
}
