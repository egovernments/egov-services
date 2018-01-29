package org.egov.model.criteria;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.egov.model.enums.AssetCategoryType;
import org.egov.model.enums.TransactionType;

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
	private List<AssetCategoryType> assetCategoryType;
	private Set<Long> assetSubCategory;
	private Set<Long> assetCategory;
	private String department;
	private String name;
	private String code;
	private List<String> codes;//added for SWM integration
	private Set<Long> id;
	private BigDecimal originalValueFrom;
	private BigDecimal originalValueTo;
	private Long assetCreatedFrom;
	private Long assetCreatedTo;
	private Long toDate; // exclusive for depreciation search

	private Long locality;
	private Long zone;
	private Long revenueWard;
	private Long block;
	private Long street;
	private Long electionWard;
	private String doorNo;
	private String status;
	private List<String> sort=null;
	private TransactionType transaction;
	
	private  Boolean isTransactionHistoryRequired;
	//added for SWM integration
	private String categoryName;
	
	private Long size;
	private Long offset;
}
