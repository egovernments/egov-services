package org.egov.asset.model;

import javax.validation.constraints.NotNull;

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
public class AccountDetailType {

	private Long id;

	@NotNull
	private String name;

	@NotNull
	private String description;

	private String tableName;

	@NotNull
	private Boolean active;

	private String fullyQualifiedName;
}
