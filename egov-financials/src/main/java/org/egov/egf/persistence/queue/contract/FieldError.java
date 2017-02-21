package org.egov.egf.persistence.queue.contract;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonPropertyOrder({"code","description"})
public  @Data class FieldError {
  private	String code;
  private	String description;

}
