package org.egov.workflow.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@AllArgsConstructor
public class Value   {
  @JsonProperty("key")
  private String key = null;

  @JsonProperty("name")
  private String name = null;
}