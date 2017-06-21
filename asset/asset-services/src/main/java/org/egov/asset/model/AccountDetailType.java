package org.egov.asset.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

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

	private List<Long> ids = new ArrayList<Long>();

	@NotNull
	@Length(max = 50, min = 1)
	private String name;

	@NotNull
	@Length(max = 50, min = 1)
	private String description;

	private String tableName;

	@NotNull
	private Boolean active;
	
	@Length(max = 250, min = 1)
	private String fullyQualifiedName;
}
