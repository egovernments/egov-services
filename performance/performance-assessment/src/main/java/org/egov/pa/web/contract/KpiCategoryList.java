package org.egov.pa.web.contract;

import java.util.List;

import org.egov.pa.model.KpiCategory;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KpiCategoryList {
	
		@JsonProperty("KpiCategory")
		private List<KpiCategory> categoryList ;

}
