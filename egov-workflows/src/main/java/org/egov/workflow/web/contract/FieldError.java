package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"code","description"})
public  @Data class FieldError {
  private	String code;
  private	String description;

}
