package org.egov.citizen.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Comment {

	private String srn;
	private String from;
	private Long timeStamp;
	private String text;
}
