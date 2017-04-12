package org.egov.lams.contract;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TaskResponse   {
  @JsonProperty("responseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("tasks")
  private List<Task> tasks = new ArrayList<Task>();

  @JsonProperty("task")
  private Task task = null;

  //FIXME check if wee need this 
  //@JsonProperty("page")
  //private Page page = null;
}