package org.egov.mr.model;

import javax.validation.constraints.NotNull;

import org.egov.mr.model.enums.RelatedTo;
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
public class Witness {
	
	private Long id;
	
	@NotNull
	private String name;
	
	@NotNull
	private Integer witnessNo;

	@NotNull
	private String relationForIdentification;

	@NotNull
	private Long dob;

	@NotNull
	private String address;

	@NotNull 
	private RelatedTo relatedTo;
	
	@NotNull
	private String relationshipWithApplicants;
	
	private String email;
	
	private String mobileNo;

	private String occupation;

	private String aadhaar;
	
	private String photo;
}