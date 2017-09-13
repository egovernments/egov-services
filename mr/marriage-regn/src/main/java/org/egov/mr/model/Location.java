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
public class Location {

	private Long locality;

	@NotNull
	private Long zone;

	private Long revenueWard;

	private Long block;

	private Long street;

	private Long electionWard;

	private String doorNo;

	private Integer pinCode;

}
