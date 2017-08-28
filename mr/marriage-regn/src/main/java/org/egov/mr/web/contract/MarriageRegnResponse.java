package org.egov.mr.web.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.mr.model.MarriageRegn;
import org.egov.mr.model.Page;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class MarriageRegnResponse {
	@JsonProperty("responseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("marriageRegns")
	private List<MarriageRegn> marriageRegns = new ArrayList<MarriageRegn>();

	@JsonProperty("page")
	private Page page = null;
}
