package org.egov.lcms.workflow.models;

import org.egov.common.contract.request.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * RequestInfo class
 * 
 * @author Pavan Kumar Kamma
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestInfo {

	private String action;

	private String did;

	private String msgId;

	private String requesterId;

	private String authToken;

	private String apiId;

	private String ver;

	private String ts;

	private String key;

	private String tenantId;

	private User userInfo = null;

	private String correlationId = null;
}
