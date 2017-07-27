package org.egov.propertyWorkflow.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.egov.models.UserInfo;

/**
 * RequestInfo class
 * 
 * @author Pavan Kumar Kamma
 *
 */
@Data
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
