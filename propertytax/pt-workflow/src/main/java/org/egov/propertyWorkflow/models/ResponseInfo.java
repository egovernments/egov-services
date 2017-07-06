package org.egov.propertyWorkflow.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ResponseInfo class
 * 
 * @author Pavan Kumar Kamma
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseInfo {

	private String resMsgId = null;

	private String status = null;

	private String apiId = null;

	private String ver = null;

	private String ts = null;

	private String key = null;

	private String tenantId = null;
}
