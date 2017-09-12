package org.egov.citizen.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentResponse {
	
	private String srn;
	private String from;
	private Long timeStamp;
	private String text;
	private Long commentcd;
	
	private String docFrom;
	private Long docTimeStamp;
	private String filePath;
	private Long documentcd;
	private String uploadedbyrole;

	

}
