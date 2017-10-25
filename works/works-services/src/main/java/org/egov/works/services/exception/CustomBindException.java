package org.egov.works.services.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.works.services.web.contract.RequestInfo;
import org.springframework.validation.BindingResult;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomBindException extends RuntimeException {

	private static final long serialVersionUID = 8861914629969408745L;

	private BindingResult errors;

	private RequestInfo requestInfo;
}
