package org.egov.infra.persist.service;

import java.util.List;
import java.util.Map;

import org.egov.infra.persist.repository.PersistRepository;
import org.egov.infra.persist.web.contract.JsonMap;
import org.egov.infra.persist.web.contract.Mapping;
import org.egov.infra.persist.web.contract.QueryMap;
import org.egov.infra.persist.web.contract.TopicMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersistService {

	@Autowired
	private TopicMap topicMap;

	@Autowired
	private PersistRepository persistRepository;

	@Transactional
	public void persist(String topic, String json) {

		Map<String, Mapping> map = topicMap.getTopicMap();
		Mapping mapping = map.get(topic);
		log.info("PersistService mapping:" + mapping);

		List<QueryMap> queryMaps = mapping.getQueryMaps();
		for (QueryMap queryMap : queryMaps) {
			String query = queryMap.getQuery();
			List<JsonMap> jsonMaps = queryMap.getJsonMaps();
			String basePath = queryMap.getBasePath();
			persistRepository.persist(query, jsonMaps, json, basePath);

		}

		/*
		 * for(Mapping mapping : mappings){
		 * log.info("mapping.getFromTopic():"+mapping.getFromTopic()+","+"topic:"+topic)
		 * ; if(mapping.getFromTopic().equals(topic)){ List<QueryMap> queryMaps =
		 * mapping.getQueryMaps(); for(QueryMap queryMap : queryMaps){ String query =
		 * queryMap.getQuery(); List<JsonMap> jsonMaps = queryMap.getJsonMaps(); String
		 * basePath = queryMap.getBasePath(); persistRepository.persist(query, jsonMaps,
		 * json, basePath);
		 * 
		 * } } }
		 */

	}

}
