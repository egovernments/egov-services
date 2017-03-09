package org.egov.lams.exception;


import org.egov.lams.model.ResponseInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponse {

	 private ResponseInfo responseInfo;
	 private Error error;	 
}
