package org.egov.mr.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TaskRequest   {
	
  @JsonProperty("requestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("tasks")
  private List<Task> tasks = new ArrayList<Task>();

  @JsonProperty("task")
  private Task task = null;

}