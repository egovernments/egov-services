package org.egov.pa.web.contract;

import java.util.List;

import org.egov.pa.model.KpiCategory;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KpiCategoryList {
	
		@JsonProperty("KpiCategory")
		private List<KpiCategory> categoryList ;

}
