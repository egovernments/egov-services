package org.egov.citizen.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Document {

	private String srn;
	private String from;
	private Long timeStamp;
	private String filePath;
	private String name;
	private String remarks;
	private String uploadedbyrole;
	private Boolean isFinal;
}
