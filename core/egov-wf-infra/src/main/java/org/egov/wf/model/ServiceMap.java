package org.egov.wf.model;

import java.util.List;

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
public class ServiceMap {
	
	private String processInstanceBody;
	
	private String taskBody;
	
	private List<TopicMap> topicMap;

}
