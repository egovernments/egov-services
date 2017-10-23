package org.egov.model.criteria;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevaluationCriteria {
        
    @NotNull
	private String tenantId;
	private List<Long> id;
	private List<Long> assetId;
	private String status;
	private Long size;
	private Long offset;
	private Long fromDate;
	private Long toDate;

}
