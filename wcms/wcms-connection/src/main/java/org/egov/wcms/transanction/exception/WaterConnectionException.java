package org.egov.wcms.transanction.exception;

import org.egov.common.contract.request.RequestInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Description : IdGeneration custom exception class
 * 
 * @author Narendra
 *
 */

@AllArgsConstructor
@Getter
@Setter
public class WaterConnectionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String customMsg;

	private String msgDetails;

	private RequestInfo requestInfo;

}
