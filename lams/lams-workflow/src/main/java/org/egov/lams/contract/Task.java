package org.egov.lams.contract;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date createdDate;
	@JsonFormat(pattern = "dd/MM/yyyy")
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
	private Map<String, Attribute> attributes = new HashMap<>();
	private String tenantId;
}

