package org.egov.marriagefee.model;

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
public class PriestInfo {
	@NotNull
	private String name;

	@NotNull
	private Long religion;

	@NotNull
	private String address;
	
	private String aadhaar;
	
	private String mobileNo;
	
	private String email;
}
