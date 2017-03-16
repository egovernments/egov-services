package org.egov.demand.web.contract;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Module {
	private Long id;

	private String name;

	private Boolean enabled;

	private String contextRoot;

	private Long parentModule;

	private String displayName;

	private Long orderNumber;
}
