package org.egov.lams.common.web.request;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.common.web.contract.ProcessInstance;
import org.egov.lams.common.web.contract.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ProcessInstanceResponse   {
	
  @JsonProperty("responseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("processInstances")
  private List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();

  @JsonProperty("processInstance")
  private ProcessInstance processInstance = null;

  //@JsonProperty("page")
  //private Page page = null;
}