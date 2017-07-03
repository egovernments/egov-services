package org.egov.egf.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@NoArgsConstructor
public class FundSearchCriteria {

	private List<Long> ids = new ArrayList<Long>();
	private String name;
	private String code;
	private Character identifier;
	private Long level;
	private Long parentId;
	private Boolean isParent;
	private Boolean active;
	private Integer fromIndex;
	private Integer pageSize;

	public boolean isPaginationCriteriaPresent() {
		return fromIndex != null && pageSize != null;
	}
}