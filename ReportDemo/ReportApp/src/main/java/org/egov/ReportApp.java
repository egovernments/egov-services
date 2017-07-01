package org.egov;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.io.File;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.egov.controller.ReportController;
import org.egov.domain.model.ReportMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;



@SpringBootApplication
public class ReportApp {
	
	
	@Autowired
	public ReportMetaData reportMetaData;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ReportApp.class, args);
		 
	        
	}
	
	@Bean
    public MappingJackson2HttpMessageConverter jacksonConverter() {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        final ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // mapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH));
        converter.setObjectMapper(mapper);
        return converter;
    }
	 
	  @Bean("reportMetaData")
	  public static ReportMetaData loadYaml() {
	ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    try {
    	ReportMetaData reportMetaData = mapper.readValue(new File("/ws/ReportDemo/ReportApp/src/main/resources/application.yml"), ReportMetaData.class);
        System.out.println(ReflectionToStringBuilder.toString(reportMetaData,ToStringStyle.MULTI_LINE_STYLE));
        //new ReportController(reportMetaData);
        
        return reportMetaData;
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return null;
    }
}