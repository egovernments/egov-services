package org.egov.wcms.transanction.web.contract;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Object holds the basic data for a property
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PropertyInfo {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId = null;

	@JsonProperty("upicNumber")
	@Size(min = 6, max = 128)
	private String upicNumber = null;

	

}
