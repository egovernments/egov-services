package org.egov;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.egov.domain.model.ReportDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;


@SpringBootApplication
public class ReportApp{

	public static final Logger LOGGER = LoggerFactory.getLogger(ReportApp.class);

    @Autowired
    public static ResourceLoader resourceLoader;
   
    @Autowired
    private static Environment env;
    @Autowired
    private static ReportDefinitions reportDefinitions;
    
    public ReportApp(ResourceLoader resourceLoader) {
    	this.resourceLoader = resourceLoader;
    }
    
    @Bean
    public RestTemplate restTemplate(){
    	return new RestTemplate();
    }
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ReportApp.class, args);
		
	}
	@PostConstruct
	@Bean("reportDefinitions")
	public static ReportDefinitions loadYaml() {
		
	ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	try {

	/*Resource resource = resourceLoader.getResource("file:/ws/egov-services/pgr/pgr-master/src/main/resources/application.yml");
	File file = resource.getFile();
	 reportDefinitions = mapper.readValue(file, ReportDefinitions.class);*/
	// Resource resource =
	// resourceLoader.getResource("file:"+env.getproperty("report.yaml.path"));
    //Resource resource = resourceLoader.getResource(env.getProperty("report.yaml.path"));
	
	 //URL oracle = new URL(env.getProperty("report.yaml.path"));
	URL oracle = new URL("https://raw.githubusercontent.com/egovernments/egov-services/master/pgr/pgr-master/src/main/resources/application.yml");
	       
	 reportDefinitions = mapper.readValue(new InputStreamReader(oracle.openStream()), ReportDefinitions.class);
	 
	System.out.println("Report Defintion PGR: "+reportDefinitions.toString());
	return reportDefinitions;
	} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	}
	return null;
	}
	
	public static ReportDefinitions getReportDefs() {
		return reportDefinitions;
	}
	
	
}