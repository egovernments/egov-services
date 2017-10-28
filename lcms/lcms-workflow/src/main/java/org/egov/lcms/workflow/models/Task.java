package org.egov.lcms.workflow.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Task class
 * 
 * @author Yosadhara
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Task {

	@NotNull
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

	@JsonProperty("state")
	private StateEnum state;

	@Size(min = 1, max = 128)
	@JsonProperty("status")
	private String status;

	@Size(min = 1, max = 256)
	@JsonProperty("url")
	private String url;

	@NotNull
	@Size(min = 1, max = 128)
	@JsonProperty("action")
	private String action;

	@Size(min = 1, max = 128)
	@JsonProperty("senderName")
	private String senderName;

	@Size(min = 1, max = 128)
	@JsonProperty("extraInfo")
	private String extraInfo;

	@Size(min = 1, max = 128)
	@JsonProperty("natureOfTask")
	private String natureOfTask;

	@JsonProperty("entity")
	private String entity;

	private String tenantId;
}