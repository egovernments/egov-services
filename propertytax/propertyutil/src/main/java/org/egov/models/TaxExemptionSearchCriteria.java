package org.egov.models;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Anil
 *
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaxExemptionSearchCriteria {

	@NotNull
	@NotEmpty
	private String tenantId;

	private Integer pageSize;

	private Integer pageNumber;

	private String[] sort;

	private String upicNo;

	private String oldUpicNo;

	private String applicationNo;

	private Long taxExemptionId;

}