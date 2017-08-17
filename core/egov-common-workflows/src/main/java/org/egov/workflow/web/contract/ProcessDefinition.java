package org.egov.workflow.web.contract;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class ProcessDefinition {
	
	/**
	 * Id of the ProcessDefinition gets created.
	 */

	private String id = null;
	
	private String name = null;
	
	/**
	 * businessKey is the name representing the process flow of the a particular
	 * Item For example For Financial vouchers work flow process may be defined
	 * with a businessKey of "voucher_workflow" . For eGov internal work flow
	 * Implementation it is same as the class name of the java object under
	 * going work flow. example businessKey "Voucher"
	 */
	@Length(max = 128, min = 1)
	@NotNull
	private String businessKey = null;
	
	private int version = 0;
	
	private String description = null;
	
	private List<ActivitiFormProperty> formProperties = new ArrayList<ActivitiFormProperty>();

}
