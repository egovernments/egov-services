package org.egov.collection.web.contract;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseInfo {

	private String resMsgId;

	private String msgId;

	private String status;

	private String apiId;

	private String ver;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private String ts;

}
