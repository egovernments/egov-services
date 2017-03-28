package org.egov.lams.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.Task;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TaskRequest   {
	
  @JsonProperty("requestInfo")
  private WorkflowRequestInfo requestInfo = null;

  @JsonProperty("tasks")
  private List<Task> tasks = new ArrayList<Task>();

  @JsonProperty("task")
  private Task task = null;

}