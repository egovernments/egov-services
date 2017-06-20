package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * department
 * 
 * @author narendra
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Department {

	private Long id;

	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId;

	@Size(min = 4, max = 16)
	@NotNull
	private String category;

	@NotNull
	@Size(min = 4, max = 128)
	private String name;

	@NotNull
	@Size(min = 4, max = 16)
	private String code;

	@Size(min = 8, max = 256)
	private String nameLocal;

	@Size(min = 8, max = 512)
	private String description;

	@JsonProperty("data")
	@JsonIgnore
	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	private AuditDetails auditDetails;

}
