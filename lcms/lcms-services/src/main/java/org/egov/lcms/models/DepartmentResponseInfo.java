package org.egov.lcms.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
