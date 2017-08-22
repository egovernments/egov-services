package org.egov;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.egov.domain.model.ReportDefinitions;
import org.egov.swagger.model.ReportDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ReportApp implements EnvironmentAware {

	public static final Logger LOGGER = LoggerFactory.getLogger(ReportApp.class);

    @Autowired
    public static ResourceLoader resourceLoader;
   
    @Autowired
    private static Environment env;
    
    @Override
    public void setEnvironment(final Environment env) {
        ReportApp.env = env;
    }
    
    @Autowired
    private static ReportDefinitions reportDefinitions;
    
    public void setReportDefinitions(ReportDefinitions reportDefinitions) {
		ReportApp.reportDefinitions = reportDefinitions;
	}

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
	
	
	@Bean("reportDefinitions")
	public static ReportDefinitions loadYaml() {
    
	ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
	mapper.setSerializationInclusion(Include.NON_NULL);
	List<ReportDefinition> localrd = new ArrayList<ReportDefinition>();
	ReportDefinitions rd = new ReportDefinitions();
	ReportDefinitions localReportDefinitions = new ReportDefinitions();
	ReportDefinition validateRD = new ReportDefinition();
	
	BufferedReader br = null;
	FileReader fr = null;
	try {
    //Local Testing
	/*Resource resource = resourceLoader.getResource("file:/ws/reportFileLocations.txt");
	File file = resource.getFile();
	fr = new FileReader(file);
	br = new BufferedReader(fr);*/
	
	//Dev Testing
	 URL url = new URL("https://raw.githubusercontent.com/egovernments/egov-services/master/docs/reportinfra/report/reportFileLocations.txt");
	 URLConnection urlConnection = url.openConnection();
	 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	 
	try {

		String yamlLocation;
		//while ((yamlLocation = br.readLine()) != null) {
			while ((yamlLocation = bufferedReader.readLine()) != null) {
			
			if(yamlLocation.startsWith("https")) {
				LOGGER.info("The Yaml Location is : "+yamlLocation);
				URL oracle = new URL(yamlLocation);
				try{
				rd = mapper.readValue(new InputStreamReader(oracle.openStream()), ReportDefinitions.class);
				} catch(Exception e) {
					LOGGER.info("Skipping the report definition "+yamlLocation);
				}
				localrd.addAll(rd.getReportDefinitions());
				
				} else if(yamlLocation.startsWith("file://")){
					LOGGER.info("The Yaml Location is : "+yamlLocation);
					Resource resource = resourceLoader.getResource(yamlLocation.toString());
					File file = resource.getFile();
					try{
					rd = mapper.readValue(file, ReportDefinitions.class);
					 } catch(Exception e) {
						LOGGER.info("Skipping the report definition "+yamlLocation);
					}
					localrd.addAll(rd.getReportDefinitions());
					
				} else {
					LOGGER.info("The Yaml Location is : "+yamlLocation);
					URL oracle = new URL(yamlLocation);
					rd = mapper.readValue(new InputStreamReader(oracle.openStream()), ReportDefinitions.class);
					localrd.addAll(rd.getReportDefinitions());
				}
			
		}

	} catch (IOException e) {
		e.printStackTrace();

	} 
		localReportDefinitions.setReportDefinitions(localrd);
		reportDefinitions = localReportDefinitions;
	
	
     /*LOGGER.info("Duplicate Report Definitions are "+reportDefinitions.getDuplicateReportDefinition());*/
     //Dev Server
	 /*URL oracle = new URL(ReportApp.env.getProperty("report.yaml.path"));
	 reportDefinitions = mapper.readValue(new InputStreamReader(oracle.openStream()), ReportDefinitions.class);*/
	 
	LOGGER.info("Report Defintion : "+reportDefinitions.toString());
	return reportDefinitions;
	}catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	}
	return reportDefinitions;
	}
	
	

	public static ReportDefinitions getReportDefs() {
		return reportDefinitions;
	}
	
	
}