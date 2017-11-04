package org.egov.works.masters.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import net.minidev.json.JSONArray;
import org.egov.common.contract.response.ResponseInfo;

import java.util.Map;

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
	private Map<String, Map<String, JSONArray>> mdmsRes;
}
