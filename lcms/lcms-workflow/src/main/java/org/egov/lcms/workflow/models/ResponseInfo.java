package org.egov.lcms.workflow.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ResponseInfo class
 * 
 * @author Pavan Kumar Kamma
 *
 */
@Setter
@Getter
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
