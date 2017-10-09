package org.egov.infra.persist.web.contract;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TopicMap {

	private Map<String, Mapping> topicMap;
}
