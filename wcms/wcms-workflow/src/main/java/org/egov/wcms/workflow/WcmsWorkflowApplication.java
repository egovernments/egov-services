package org.egov.wcms.workflow;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Import({ TracerConfiguration.class })
@SpringBootApplication
public class WcmsWorkflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(WcmsWorkflowApplication.class, args);
	}
	@Value("${app.timezone}")
        private String timeZone;

        @PostConstruct
        public void initialize() {
                TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
        }
        
         @Bean
            public MappingJackson2HttpMessageConverter jacksonConverter() {
                MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
                ObjectMapper mapper = new ObjectMapper();
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
                mapper.setTimeZone(TimeZone.getTimeZone(timeZone));
                // mapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH));
                converter.setObjectMapper(mapper);
                return converter;
            }
}
