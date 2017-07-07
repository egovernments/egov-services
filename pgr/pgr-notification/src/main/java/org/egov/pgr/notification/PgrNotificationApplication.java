package org.egov.pgr.notification;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.egov.pgr.common.repository.DesignationRepository;
import org.egov.pgr.common.repository.EmployeeRepository;
import org.egov.pgr.common.repository.LocationRepository;
import org.egov.pgr.common.repository.PositionRepository;
import org.egov.pgr.notification.domain.service.emailstrategy.*;
import org.egov.pgr.notification.domain.service.smsstrategy.*;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.trimou.engine.MustacheEngine;
import org.trimou.engine.MustacheEngineBuilder;
import org.trimou.engine.locator.ClassPathTemplateLocator;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
@Import(TracerConfiguration.class)
public class PgrNotificationApplication {

    private static final String TEMPLATE_TYPE = "txt";
    private static final String TEMPLATE_FOLDER = "templates";
    private static final int PRIORITY = 1;
    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

    @Value("${app.timezone}")
    private String timeZone;

    @PostConstruct
    public void initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    public static void main(String[] args) {
        SpringApplication.run(PgrNotificationApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy hh:mm a"));
        return mapper;
    }

    @Bean
    public MustacheEngine getMustacheEngine() {
        ClassPathTemplateLocator classPathTemplateLocator = new ClassPathTemplateLocator(PRIORITY, TEMPLATE_FOLDER,
            TEMPLATE_TYPE);
        return MustacheEngineBuilder.newBuilder().addTemplateLocator(classPathTemplateLocator).build();
    }

    @Bean
    public MappingJackson2HttpMessageConverter jacksonConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH));
        mapper.setTimeZone(TimeZone.getTimeZone(timeZone));
        converter.setObjectMapper(mapper);
        return converter;
    }

    @Bean
    public EmployeeRepository employeeRepository(RestTemplate restTemplate,
                                                 @Value("${hremployee.host}") String employeeServiceHostName) {
        return new EmployeeRepository(restTemplate, employeeServiceHostName);
    }

    @Bean
    public PositionRepository positionRepository(RestTemplate restTemplate,
                                                 @Value("${hrmaster.host}") String hrMasterServiceHostName) {
        return new PositionRepository(restTemplate, hrMasterServiceHostName);
    }

    @Bean
    public DesignationRepository designationRepository(RestTemplate restTemplate,
                                                       @Value("${hrmaster.host}") String hrMasterServiceHostName) {
        return new DesignationRepository(restTemplate, hrMasterServiceHostName);
    }

    @Bean
    public LocationRepository locationRepository(RestTemplate restTemplate,
                                          @Value("${location.host}") String locationServiceHostName) {
        return new LocationRepository(restTemplate, locationServiceHostName);
    }

    @Bean("smsMessageStrategies")
    public List<SMSMessageStrategy> getSMSMessageStrategies() {
        return Arrays.asList(new ComplaintCitizenSMSMessageStrategy(), new NewDeliverableCitizenSMSMessageStrategy(),
            new UpdateDeliverableStatusCitizenSMSMessageStrategy(),
            new ProcessingFeeEnteredCitizenSMSMessageStrategy(),
            new ComplaintRegisteredEmployeeSMSMessageStrategy(),
            new ComplaintEscalatedFromEmployeeSMSMessageStrategy(),
            new ComplaintEscalatedToEmployeeSMSMessageStrategy(),
            new RejectDeliverableCitizenSMSMessageStrategy(),
            new ReSubmitDeliverableCitizenSMSMessageStrategy());
    }

    @Bean("emailMessageStrategies")
    public List<EmailMessageStrategy> getEmailMessageStrategies() {
        return Arrays.asList(new ComplaintCitizenEmailMessageStrategy(),
            new NewDeliverableCitizenEmailMessageStrategy(),
            new UpdateDeliverableStatusCitizenEmailMessageStrategy(),
            new ProcessingFeeEnteredCitizenEmailMessageStrategy(),
            new ComplaintRegisteredEmployeeEmailMessageStrategy(),
            new ComplaintEscalatedFromEmployeeEmailMessageStrategy(),
            new ComplaintEscalatedToEmployeeEmailMessageStrategy(),
            new RejectDeliverableCitizenEmailMessageStrategy(),
            new ReSubmitDeliverableCitizenEmailMessageStrategy());
    }
}
