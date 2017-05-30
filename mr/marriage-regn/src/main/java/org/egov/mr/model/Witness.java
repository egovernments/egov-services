package org.egov.mr.model;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class Witness {
	@NotNull
	private String name;

	@NotNull
	private String relationForIdentification;

	@NotNull
	private Integer age;

	@NotNull
	private String address;

	@NotNull
	private String relationship;

	private String occupation;

	private String aadhaar;
}
