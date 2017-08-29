package org.egov.citizen.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Comment {

	private String from;
	private String to;
	private Long timeStamp;
	private String text;
}
