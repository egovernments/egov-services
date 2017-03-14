package org.egov.asset.model;

import java.util.List;

import org.egov.asset.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AssetCriteria {

	private List<Long> id;
	private String name;
	private String code;
	private Long assetCategory;
	private Long department;
	private Status status;
	private Long locality;
	private Long zone;
	private Long revenueWard;
	private Long block;
	private Long street;
	private Long electionWard;
	private Long doorNo;
	private Long pinCode;
	
	private String tenantId;
	private Long size;
	private Long offset;
}
