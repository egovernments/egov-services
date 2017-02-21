package org.egov.egf.persistence.queue.contract;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonPropertyOrder({"code","message","description","filelds"})
public @Data class Error {

	private Integer code = null;

	private String message = null;

	private String description = null;

	private List<FieldError> filelds = new ArrayList<FieldError>();  

	

}
