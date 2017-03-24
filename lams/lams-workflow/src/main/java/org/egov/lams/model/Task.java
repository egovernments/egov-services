package org.egov.lams.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Task   {

	private String id;
	private String businessKey;
	private String type;
	private Position assignee;
	private String comments;
	private Date createdDate;
	private Date lastupdatedSince;
	private Position owner;
	private String state;
	private String status;
	private String url;
	private String action;
	private String senderName;
	private String details;
	private String natureOfTask;
	private String entity;
	private String tenantId;
}

