package org.egov.tradelicense.common.domain.exception;

import org.egov.tl.commons.web.contract.RequestInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DuplicateTradeLicenseException extends RuntimeException {

	private static final long serialVersionUID = 8861914629969408745L;

	private String customMsg;

	private RequestInfo requestInfo;
}
