package org.egov.propertyWorkflow.models;

import org.egov.models.UserInfo;

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

	private UserInfo userInfo = null;

	private String correlationId = null;
}
