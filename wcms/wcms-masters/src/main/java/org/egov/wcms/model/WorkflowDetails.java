package org.egov.wcms.model;
import javax.validation.constraints.NotNull;


import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class WorkflowDetails {
	
	@NotNull
	private long department;
	
	@NotNull
	private long designation;
	
	@NotNull
	private long approver;
	
	@NotNull
	private String comments;
	
	@NotNull
	private String status;
	
	

}
