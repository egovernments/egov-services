package org.egov.infra.mdms.model;

import java.util.List;
import java.util.Map;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.minidev.json.JSONArray;

@Setter
@Getter
@ToString
public class MdmsResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;
	
	@JsonProperty("MdmsRes")
	private Map<String, List<Map<String, JSONArray>>> mdmsRes;
}
