package org.egov.wcms.transanction.exception;

import org.egov.common.contract.request.RequestInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class IdGenerationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String customMsg;

	private String msgDetails;

	private RequestInfo requestInfo;

}
