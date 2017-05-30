package org.egov.collection.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Created by mansibansal on 5/10/17.
 */
@Builder
@Getter
@EqualsAndHashCode
public class ServiceCategory {

	private Long id;
	private String name;
	private String code;
	private Boolean isactive;
	private String tenantId;
}
