package org.egov.pgr.read.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ComplaintTypeCategoryResponse {
	@JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("ComplaintTypeCategory")
    private  List<ComplaintTypeCategory> complaintTypeCategories;

}
