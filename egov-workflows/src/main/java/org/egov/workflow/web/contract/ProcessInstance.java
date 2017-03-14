package org.egov.workflow.web.contract;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProcessInstance {

	 
	private String id;

	@NotNull
	@Length(max=128,min=1)
	private String type;

	@Length(max=1024)
	private String description;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private Date createdDate;

	private Date lastupdatedSince;

	@Length(max=1024)
	private String status;
	
	@Length(max=128)
	private String action;
	
	@Length(max=128)
	private String businessKey;

	private Position assignee;

	private String group;

	private String senderName;
	
	private WorkflowEntity entity;

	private Map<String, Attribute> attributes;

	/*@JsonIgnore
	public String getComments() {
		return getValueForKey("approvalComments");
	}

	@JsonIgnore
	public boolean isGrievanceOfficer() {
		return getValueForKey("userRole").equals("Grievance Officer");
	}
*/
	/*public void setStateId(Long stateId) {
		Value value = new Value("stateId", String.valueOf(stateId));
		List<Value> attributeValues = Collections.singletonList(value);
		Attribute attribute = new Attribute(true, "stateId", "String", true, "This is the id of state",
				attributeValues,null);
		attributeValues.put("stateId", attribute);
	}

	// To be used to fetch single value attributes
	public String getValueForKey(String key) {
		if (Objects.nonNull(values.get(key)))
			return values.get(key).getValues().get(0).getName();

		return "";
	}*/
}