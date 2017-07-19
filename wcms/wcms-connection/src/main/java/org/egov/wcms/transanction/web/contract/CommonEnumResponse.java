package org.egov.wcms.transanction.web.contract;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.transanction.model.CommonDataModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommonEnumResponse {
	
	    @JsonProperty("responseInfo")
	    private ResponseInfo responseInfo;
	    @JsonProperty("DataModelList")
	    private List<CommonDataModel> dataModelList;

}
