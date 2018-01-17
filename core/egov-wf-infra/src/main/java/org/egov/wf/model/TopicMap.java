package org.egov.wf.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicMap {

	private String fromTopic;
	private String toTopic;
	private String name;
	private String description;
	private String basePath;
	private Request request;
	private Response response;
	
	
}
