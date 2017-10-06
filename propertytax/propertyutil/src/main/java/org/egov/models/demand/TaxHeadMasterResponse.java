package org.egov.models.demand;

import java.util.ArrayList;
import java.util.List;

import org.egov.models.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxHeadMasterResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("TaxHeadMasters")
	private List<TaxHeadMaster> taxHeadMasters = new ArrayList();
}
