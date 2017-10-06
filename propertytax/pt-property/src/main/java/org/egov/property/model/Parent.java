package org.egov.property.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Parent {

	private String id;

	private String name;

	private String longitude;

	private String latitude;

	private String boundaryNum;

	private String parent;

	private String tenantId;

	private String boundaryType;

}
