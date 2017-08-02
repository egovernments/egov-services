package org.egov.mr.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class ReissueApplicantInfo {
	@NotNull
	private String name;

	@NotNull
	private String address;

	@NotNull
	private String mobileNo;

	private String email;

	@NotNull
	private BigDecimal fee;

	private String aadhaar;

}