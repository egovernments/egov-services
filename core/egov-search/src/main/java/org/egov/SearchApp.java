package org.egov;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.egov.domain.model.SearchDefinitions;
import org.egov.swagger.model.SearchDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;



@Configuration
@PropertySource("classpath:application.properties")
@SpringBootApplication
public class SearchApp implements EnvironmentAware {

	public static final Logger LOGGER = LoggerFactory.getLogger(SearchApp.class);

    @Autowired
    public static ResourceLoader resourceLoader;
   
    @Autowired
    private static Environment env;
    
    @Override
    public void setEnvironment(final Environment env) {
        SearchApp.env = env;
    }
    
	public SearchApp(ResourceLoader resourceLoader) {
    	this.resourceLoader = resourceLoader;
    }
    
    @Autowired
    private static SearchDefinitions searchDefinitions;
    
    
    @Bean
    public RestTemplate restTemplate(){
    	return new RestTemplate();
    }
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SearchApp.class, args);
	}
	
	
	@Bean("searchDefinitions")
	@Value("common")
	public static SearchDefinitions loadYaml(String moduleName) throws Exception {
    
	ObjectMapper mapper = getMapperConfig();
	List<SearchDefinition> localrd = new ArrayList<SearchDefinition>();
	SearchDefinitions rd = new SearchDefinitions();
	SearchDefinitions localSearchDefinitions = new SearchDefinitions();

	if(!moduleName.equals("common")){
		localrd.addAll(searchDefinitions.getReportDefinitions());
	}
	loadSearchDefinitions(moduleName, mapper, localrd, rd); 
	
	localSearchDefinitions.setReportDefinitions(localrd);

	searchDefinitions = localSearchDefinitions;
     
	LOGGER.info("ModuleName : "+moduleName);
	
	return searchDefinitions;
	

	}

	private static void loadSearchDefinitions(String moduleName, ObjectMapper mapper, List<SearchDefinition> localrd,
			SearchDefinitions rd) throws Exception {
		BufferedReader br;
		FileReader fr;
		String yamllist = env.getProperty("search.yaml.path");
		try {
		List<String> ymlUrlS = Arrays.asList(yamllist.split(","));
		if(0 == ymlUrlS.size()){
			ymlUrlS.add(yamllist);
		}
		       for(String yamlLocation : ymlUrlS){ 

				if(moduleName.equals("common")){
				if(yamlLocation.startsWith("https")) {
					LOGGER.info("The Yaml Location is : "+yamlLocation);
					URL oracle = new URL(yamlLocation);
					try{
					rd = mapper.readValue(new InputStreamReader(oracle.openStream()), SearchDefinitions.class);
					} catch(Exception e) {
						
						LOGGER.info("Skipping the report definition "+yamlLocation);
						e.printStackTrace();
						
					}
					localrd.addAll(rd.getReportDefinitions());
					
					} else if(yamlLocation.startsWith("file://")){
						LOGGER.info("The Yaml Location is : "+yamlLocation);
						 Resource yamlResource = resourceLoader.getResource(yamlLocation.toString());
						 File yamlFile = yamlResource.getFile();
						try{
						rd = mapper.readValue(yamlFile, SearchDefinitions.class);
						 } catch(Exception e) {
							LOGGER.info("Skipping the report definition "+yamlLocation);
							e.printStackTrace();
						}
						localrd.addAll(rd.getReportDefinitions());
						
					} 
				
			} 
			}

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	private static ObjectMapper getMapperConfig() {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper;
	}
	
	

	public static SearchDefinitions getSearchDefs() {
		return searchDefinitions;
	}
	
	
}