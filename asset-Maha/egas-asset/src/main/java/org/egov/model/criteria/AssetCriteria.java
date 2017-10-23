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
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AssetCriteria {

	@NotNull
	private String tenantId;
	private List<Long> id;
	private String name;
	private String code;
	private Long assetCategory;
	private Long department;
	private Long locality;
	private Long zone;
	private Long revenueWard;
	private Long block;
	private Long street;
	private Long electionWard;
	private String doorNo;
	private String status;
	
	
	private Long size;
	private Long offset;
}
