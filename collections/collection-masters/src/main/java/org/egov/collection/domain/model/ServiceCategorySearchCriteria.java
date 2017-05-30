package org.egov.collection.domain.model;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
@EqualsAndHashCode
public class ServiceCategorySearchCriteria {

	private String tenantId;
	private String businessCategoryName;
	private Boolean isactive;
	private List<Long> ids;
}
