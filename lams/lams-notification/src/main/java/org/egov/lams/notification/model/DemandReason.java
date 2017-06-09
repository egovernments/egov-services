package org.egov.lams.notification.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@EqualsAndHashCode
public class DemandReason {
	private String name;
	private String category;
	private String taxPeriod;
	private String glCode;
}
