package org.egov.pgr;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@Import({ TracerConfiguration.class })
public class PGRApp
{
	
	public static void main(String[] args) {
		SpringApplication.run(PGRApp.class, args);
	}    
	
	private static Map<String, String> actionStatusMap = new HashMap<>();
	
	private static Map<String, List<String>> actionCurrentStatusMap = new HashMap<>();
	
	@PostConstruct
	private void prepareStatusMap(){
		
		Map<String, String> map = new HashMap<>();
		map.put("open", "open");
		map.put("assign", "assigned");
		map.put("close", "closed");
		map.put("reject","rejected");
		map.put("resolve", "resolved");
		map.put("reopen","open");
		actionStatusMap = map;
	}
	
	@PostConstruct
	private void prepareActionCurrentStatusMap(){
		
		Map<String, List<String>> map = new HashMap<>();
		map.put("assign", Arrays.asList("open"));
		map.put("close", Arrays.asList("rejected","resolved"));
		map.put("reject",Arrays.asList("assigned","open"));
		map.put("resolve",Arrays.asList("assigned"));
		map.put("reopen",Arrays.asList("rejected","resolved"));
		actionCurrentStatusMap = map;
	}
	
	public static Map<String, String> getActionStatusMap(){
		return actionStatusMap;
	}
	
	public static Map<String, List<String>> getActionCurrentStatusMap(){
		return actionCurrentStatusMap;
	}
}
