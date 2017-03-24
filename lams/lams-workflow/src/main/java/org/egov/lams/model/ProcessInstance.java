package org.egov.lams.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProcessInstance   {
	
	private String id = null;
	private String businessKey = null;
	private String type = null;
	private Position assignee = null;
	private String comments = null;
	private Date createdDate = null;
	private Date lastupdatedSince = null;
	private Position owner = null;
	private String state = null;
	private String status = null;
	private String senderName;
	private String details;
	List<Task> tasks = new ArrayList<Task>();
	private String tenantId;
  }
