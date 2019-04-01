package org.egov.win.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CronQueryBuilder {
	
	public String getQuery(String module) {
		String query = null;
		switch(module) {
		    case "STATEWIDE":
				query = CronQueries.GET_STATEWIDE_DATA;
				break;
			case "PGR":
				query = CronQueries.GET_PGR_DATA;
				break;
			case "PGR_CW":
				query = CronQueries.GET_PGR_CHANNELWISE_DATA;
				break;
			case "PT":
				query = CronQueries.GET_PT__DATA;
				break;
			case "TL":
				query = CronQueries.GET_TL__DATA;
				break;
			default:
				log.info("No Queries found!");
				break;

		}
		return query;
	}
	
	public Map<String, Object> getParameterMap(Long time){
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put(":time", time);
		
		return parameterMap;
		
	}
	

}
