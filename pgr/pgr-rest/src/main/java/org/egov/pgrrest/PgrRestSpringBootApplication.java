package org.egov.pgrrest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.egov.pgr.common.repository.ComplaintConfigurationRepository;
import org.egov.pgr.common.repository.EmployeeRepository;
import org.egov.pgr.common.repository.OtpRepository;
import org.egov.pgr.common.repository.OtpSMSRepository;
import org.egov.pgrrest.common.persistence.repository.UserRepository;
import org.egov.pgrrest.read.domain.service.validator.AttributeValueValidator;
import org.egov.pgrrest.read.domain.service.validator.DateAttributeValidator;
import org.egov.pgrrest.read.domain.service.validator.DoubleAttributeValidator;
import org.egov.pgrrest.read.domain.service.validator.GroupConstraintAttributeValidator;
import org.egov.pgrrest.read.domain.service.validator.IntegerAttributeValidator;
import org.egov.pgrrest.read.domain.service.validator.LongAttributeValidator;
import org.egov.pgrrest.read.domain.service.validator.MandatoryAttributeValidator;
import org.egov.pgrrest.read.domain.service.validator.SingleValueAttributeValidator;
import org.egov.pgrrest.read.domain.service.validator.TextareaAttributeValidator;
import org.egov.tracer.config.TracerConfiguration;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@Import({TracerConfiguration.class})
public class PgrRestSpringBootApplication {

    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    private static final String CLUSTER_NAME = "cluster.name";

    @Value("${app.timezone}")
    private String timeZone;

    @Value("${user.service.url}")
    private String userServiceHost;

    @Value("${egov.services.user.get_user_details}")
    private String getUserDetailsUrl;

    @Value("${egov.services.user.get_user_by_username}")
    private String getUserByUserNameUrl;

    @Value("${es.host}")
    private String elasticSearchHost;

    @Value("${es.transport.port}")
    private Integer elasticSearchTransportPort;

    @Value("${es.cluster.name}")
    private String elasticSearchClusterName;

    private TransportClient client;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

    @PostConstruct
    public void init() throws UnknownHostException {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
        DateTimeZone.setDefault(DateTimeZone.forID(timeZone));
        Settings settings = Settings.builder().put(CLUSTER_NAME, elasticSearchClusterName).build();
        final InetAddress esAddress = InetAddress.getByName(elasticSearchHost);
        final InetSocketTransportAddress transportAddress = new InetSocketTransportAddress(esAddress,
            elasticSearchTransportPort);
        client = new PreBuiltTransportClient(settings).addTransportAddress(transportAddress);
    }

    @Bean
    public UserRepository userRepository(RestTemplate restTemplate) {
        return new UserRepository(restTemplate, userServiceHost, getUserDetailsUrl, getUserByUserNameUrl);
    }

    @Bean
    public OtpRepository otpRepository(RestTemplate restTemplate, @Value("${otp.host}") String otpHost) {
        return new OtpRepository(restTemplate, otpHost);
    }

    @Bean
    public OtpSMSRepository otpSMSRepository(@Value("${sms.topic}") String smsTopic) {
        return new OtpSMSRepository(logAwareKafkaTemplate, smsTopic);
    }

    @Bean
    public EmployeeRepository employeeRepository(RestTemplate restTemplate,
                                                 @Value("${hremployee.host}") String employeeHostName) {
        return new EmployeeRepository(restTemplate, employeeHostName);
    }

    @Bean
    public ComplaintConfigurationRepository complaintConfigurationRepository(
        RestTemplate restTemplate, @Value("${pgrmaster.host}") String pgrMasterHost) {
        return new ComplaintConfigurationRepository(restTemplate, pgrMasterHost);
    }

    @Bean
    public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH));
        objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));
        return objectMapper;
    }

    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
        return new WebMvcConfigurerAdapter() {

            @Override
            public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
                configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
            }

        };
    }

    @Bean
    public TransportClient getTransportClient() {
        return client;
    }

    @Bean
    public List<AttributeValueValidator> getAttributeValueValidators() {
        return Arrays.asList(
            new MandatoryAttributeValidator(),
            new GroupConstraintAttributeValidator(),
            new SingleValueAttributeValidator(),
            new DateAttributeValidator(),
            new IntegerAttributeValidator(),
            new DoubleAttributeValidator(),
            new LongAttributeValidator(),
            new TextareaAttributeValidator()
        );
    }

    public static void main(String[] args) {
        SpringApplication.run(PgrRestSpringBootApplication.class, args);
    }
}
