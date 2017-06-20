package org.egov.commons.web.contract;

import java.util.List;




import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BusinessCategoryResponse {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("BusinessCategoryInfo")
	private List<BusinessCategory> businessCategoryInfo;

}
