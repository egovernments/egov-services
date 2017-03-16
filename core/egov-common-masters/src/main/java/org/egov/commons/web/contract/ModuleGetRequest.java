package org.egov.commons.web.contract;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class ModuleGetRequest {
	private List<Long> id;

	@Size(max = 100)
	private String name;

	private Boolean enabled;

	@Size(max = 10)
	private String contextRoot;

	private Long parentModule;

	@Size(max = 50)
	private String displayName;

	private Long orderNumber;

	@NotNull
	private String tenantId;

	private String sortBy;

	private String sortOrder;

	@Min(1)
	@Max(500)
	private Short pageSize;

	private Short pageNumber;
}
