package org.egov.lcms.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Veswanth
 *	This object holds information about the Department Request
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DepartmentResponseInfo {
	

   private String apiId;
	private String ver;
	private String ts;
	private String resMsgId;
	private String msgId;
	private String status;

}
