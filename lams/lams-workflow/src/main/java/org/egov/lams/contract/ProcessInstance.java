package org.egov.lams.contract;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.lams.contract.Attribute;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date createdDate = null;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date lastupdatedSince = null;
	private Position owner = null;
	private String state = null;
	private String status = null;
	private String senderName;
	private String details;
	List<Task> tasks = new ArrayList<Task>();
	private String tenantId;
	private Long initiatorPosition;
	private Map<String, Attribute> attributes = new HashMap<String, Attribute>();
  }
