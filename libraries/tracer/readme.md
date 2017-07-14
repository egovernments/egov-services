# Tracer
### Distributed tracing library
Correlation id and logging support addons to Spring Web and Spring Kafka. 

#### Features supported
###### Logging of the below mentioned scenarios -
- Incoming http request URI, query strings and payload.
- Message payload and topic name when pushing message to Kafka.
- Success & failure response from Kafka when pushing of message fails.
- Message payload and topic name for messages received by Kafka consumer using the KafkaListener annotation.
- Outgoing http request URI and payload along with response code and response body when using RestTemplate.

###### Toggle logging detail -
The logging of the http request/response body and Kakfa message body can be toggled on/off using 
"tracer.detailed.tracing.enabled" application property.
  
###### Correlation id retrieval and forwarding -
The library takes care of retrieving the correlation id from -
- Incoming http request body or header
- Kafka message payload

For an outgoing http request the correlation id is sent as a custom request header "x-correlation-id".

###### Setting the correlation id in the MDC -
Given the library takes care of placing the correlation id into the MDC, any custom logging done in the 
application would seamlessly include the correlation id in the log message.

Note - See the "logging.pattern" mentioned in the "Tracer integration" section.
  
#### Steps to integrate Tracer to your Spring application -
- In the pom.xml add the below repository section
 ```
     <repositories>
         <repository>
             <id>repo.egovernments.org</id>
             <name>eGov ERP Releases Repository</name>
             <url>http://repo.egovernments.org/nexus/content/repositories/releases/</url>
         </repository>
     </repositories>
 ```
 - In the pom.xml add the below dependency and replace version accordingly.
 ```
         <dependency>
             <groupId>org.egov.services</groupId>
             <artifactId>tracer</artifactId>
             <version>X.Y.Z</version>
         </dependency>
 ```
 - Add the below entry to application.properties
 ```
 logging.pattern.console=%clr(%X{CORRELATION_ID:-}) %clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}
 ```
 - In the main Spring application class file add the below annotation to the class.
 ```
 @Import({TracerConfiguration.class})
```
 - To make http requests, in your class component autowire Spring's RestTemplate.
 - To push messages to Kafka, in your class component autowire LogAwareKafkaTemplate.
 - To receive messages via Kafka consumer annotate your bean method with KafkaListener annotation. Add the
 payload annotation to the message payload parameter (any data type is supported) and topic header to the topic String
  parameter. 
 
 
#### Tracer implementation details -
- A Spring filter is used to retrieve the correlation id from the incoming http request.
- If the incoming http request is a POST and the content type is compatible with application/json then the library
makes an attempt to retrieve the correlation id from the request body. 
The json path searched for the correlation id are RequestInfo.correlationId and requestInfo.correlationId.
- If the correlation id is not present in the request body or the http verb is not POST or the content type is not json
 compatible then an attempt is made to retrieve the correlation id from the http request header "x-correlation-id".
- If the correlation id is not present in the request body or header then a new correlation id (UUID v4) is generated.
- For the RestTemplate an interceptor is used for adding the correlation id as a custom header 
to the outgoing request, logging the corresponding request and response sent and received.
- A subclass of Spring's RestTemplate is registered as a Spring bean to perform the correlation id forwarding and logging.
- The LogAwareKafkaTemplate is a wrapper Spring bean class for Spring Kafka's KafkaTemplate that performs the logging 
of messages sent to Kafka.
- For a Kafka consumer implemented using Spring Kafka's KafkaListener annotation an AspectJ's aspect is used to log and
 retrieve the correlation id from the received payload.
- The correlation id retrieved via the filter or aspect is then stored in a thread local variable to 
forward as necessary. 
 
#### Change log -
1.0.0
- For Kafka Listener the @Payload annotation is used to identify the payload parameter.
- For Kafka Listener @Header(KafkaHeaders.RECEIVED_TOPIC) is used to identify the topic name.

0.18.1
- Added IST time zone json serializer - org.egov.tracer.kafka.serializer.ISTTimeZoneJsonSerializer
- Added UTC hash map deserializer - org.egov.tracer.kafka.deserializer.HashMapDeserializer
- Added IST time zone hash map deserializer - org.egov.tracer.kafka.deserializer.ISTTimeZoneHashMapDeserializer
- tracer.detailed.tracing.enabled can be set to "true" or "false" to toggle request body logging. Default value is "true".

Versions older than 0.18.0
- For Kafka Listener the first method parameter that is not of type 
String or org.springframework.kafka.support.Acknowledgment is identified as the payload parameter.



