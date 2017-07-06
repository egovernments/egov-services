package org.egov.propertyWorkflow.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.models.SourceEnum;
import org.egov.models.StatusEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * ProcessInstance class
 * 
 * @author Yosadhara
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProcessInstance {

	@JsonProperty("id")
	private String id;

	@NotNull
	@Size(min = 1, max = 128)
	@JsonProperty("businessKey")
	private String businessKey;

	@Size(min = 1, max = 128)
	@JsonProperty("type")
	private String type;

	@JsonProperty("assignee")
	private Position assignee;

	@Size(min = 1, max = 1024)
	@JsonProperty("comments")
	private String comments;

	@JsonProperty("createdDate")
	private String createdDate;

	@JsonProperty("lastupdatedSince")
	private String lastupdatedSince;

	@JsonProperty("owner")
	private Position owner;

	@Size(min = 1, max = 128)
	@JsonProperty("state")
	private SourceEnum state;

	@Size(min = 1, max = 128)
	@JsonProperty("status")
	private StatusEnum status;

	@Size(min = 1, max = 128)
	@JsonProperty("senderName")
	private String senderName;

	@JsonProperty("tenantId")
	private String tenantId;
}
