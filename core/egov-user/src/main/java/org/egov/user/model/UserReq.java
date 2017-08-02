package org.egov.user.model;

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
public class UserReq   {
	
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("Users")
  private List<User> users = new ArrayList<User>();

  
}

