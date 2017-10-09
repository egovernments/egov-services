package org.egov.infra.mdms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;


@SpringBootApplication
@Configuration
@PropertySource("classpath:application.properties")
public class MasterDataMgmtSvcApplication
{
	    
	public static final Logger logger = LoggerFactory.getLogger(MasterDataMgmtSvcApplication.class);

	
    @Autowired
    private static Environment env;
    
    
    public void setEnvironment(final Environment env) {
    	MasterDataMgmtSvcApplication.env = env;
    }
	
	public static void main(String[] args) {
		SpringApplication.run(MasterDataMgmtSvcApplication.class, args);
	}    

	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	private static ObjectMapper getMapperConfig() {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper;
	}
}
