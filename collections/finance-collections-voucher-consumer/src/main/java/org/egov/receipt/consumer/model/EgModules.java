package org.egov.receipt.consumer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class EgModules {
	private Integer id;
	private String name;
	private String description;
}
