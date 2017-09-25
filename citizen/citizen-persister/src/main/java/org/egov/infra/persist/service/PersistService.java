package org.egov.infra.persist.service;

import java.util.List;

import org.egov.infra.persist.repository.PersistRepository;
import org.egov.infra.persist.web.contract.JsonMap;
import org.egov.infra.persist.web.contract.Mapping;
import org.egov.infra.persist.web.contract.QueryMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersistService {

	@Autowired
	private org.egov.infra.persist.web.contract.Service service;
	
	@Autowired
	private PersistRepository persistRepository;
	
	@Transactional
	public void persist(String topic, String json){
		
		log.debug("PersistService persist:"+service);
		System.out.println("PersistService persist:");
		List<Mapping> mappings = service.getServiceMaps().getMappings();
		System.out.println("PersistService persist:"+mappings);
		for(Mapping mapping : mappings){
			System.out.println("mapping.getFromTopic():"+mapping.getFromTopic()+","+"topic:"+topic);
			if(mapping.getFromTopic().equals(topic)){
				List<QueryMap> queryMaps = mapping.getQueryMaps();
				for(QueryMap queryMap : queryMaps){
					String query = queryMap.getQuery();
					List<JsonMap> jsonMaps = queryMap.getJsonMaps();
					String rootObject=queryMap.getRootObject();
					System.out.println(":::::rootObject::::::"+rootObject);
					/*JsonMap jsonMap = jsonMaps.get(0);
					jsonMap.getJsonPath().contains("[");*/
					System.out.println("PersistService persist befor repository");
					persistRepository.persist(query, jsonMaps, json,rootObject);
					
				}
			}
		}
		
	}
	
}
