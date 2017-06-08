package org.egov.propertyUser.userConsumer;

import java.util.HashMap;
import java.util.Map;
import org.egov.propertyUser.model.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.models.User;
import org.egov.models.UserResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/**
 * Consumer class will use for listing  property object from kafka server. 
 * Authenticate the user
 * Search the user
 * Create the user
 * If user exist update the user id 
 * otherwise create the user
 * 
 * @author: S Anilkumar
 */

@RestController
@SuppressWarnings("unused")
public class Consumer {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	Environment environment;

	@Autowired
	private Producer producer;


	/*
	 * This method for creating rest template
	 */
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}



	/**
	 * This method for getting consumer configuration bean
	 */
	@Bean
	public Map<String,Object> consumerConfig(){
		Map<String,Object> consumerProperties=new HashMap<String,Object>();
		consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, environment.getProperty("auto.offset.reset.config"));
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("bootstrap.servers"));
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "user");
		return consumerProperties;
	}

	/**
	 * This method will return the consumer factory bean based on consumer configuration
	 */
	@Bean
	public ConsumerFactory<String, PropertyRequest> consumerFactory(){
		return new DefaultKafkaConsumerFactory<>(consumerConfig(),new StringDeserializer(),
				new JsonDeserializer<>(PropertyRequest.class));

	}

	/**
	 * This bean will return kafka listner object based on consumer factory
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, PropertyRequest> kafkaListenerContainerFactory(){
		ConcurrentKafkaListenerContainerFactory<String, PropertyRequest> factory=new ConcurrentKafkaListenerContainerFactory<String,PropertyRequest>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}


	/**
	 * This method will listen property object from producer and check user authentication
	 *  Updating auth token in UserAuthResponseInfo
	 *  Search user
	 *  Create user
	 */
	@KafkaListener(topics ="#{environment.getProperty('validate.user')}")
	public void receive(PropertyRequest propertyRequest) {
		StringBuffer createUrl=new StringBuffer();
		createUrl.append(environment.getProperty("egov.services.allottee_service.hostname"));
		createUrl.append(environment.getProperty("egov.services.allottee_service.basepath"));
		createUrl.append(environment.getProperty("egov.services.allottee_service.createpath"));

		StringBuffer searchUrl=new StringBuffer();
		searchUrl.append(environment.getProperty("egov.services.allottee_service.hostname"));
		searchUrl.append(environment.getProperty("egov.services.allottee_service.basepath"));
		searchUrl.append(environment.getProperty("egov.services.allottee_service.searchpath"));


		for(Property property:propertyRequest.getProperties()){
			for(User user: property.getOwners()){
				user.setUserName(user.getMobileNumber());
				if(user.getId() !=null){

					Map<String,Object> userSearchRequestInfo=new HashMap<String,Object >();
					userSearchRequestInfo.put("username",user.getUserName());
					userSearchRequestInfo.put("tenantId",user.getTenantId());
					userSearchRequestInfo.put("RequestInfo",propertyRequest.getRequestInfo());
					//search user
					UserResponseInfo userResponse= restTemplate.postForObject(searchUrl.toString(), userSearchRequestInfo, UserResponseInfo.class);

					if(userResponse.getResponseInfo()!=null){
						if(userResponse.getUser()==null){
							UserRequestInfo userRequestInfo=new UserRequestInfo();
							userRequestInfo.setRequestInfo(propertyRequest.getRequestInfo());
							userRequestInfo.setUser(user);
							UserResponseInfo userCreateResponse = restTemplate.postForObject(createUrl.toString(),
									userRequestInfo, UserResponseInfo.class);	
						}
						else{
							user.setId(userResponse.getUser().get(0).getId());
						}
					}

				}else{
					UserRequestInfo userRequestInfo=new UserRequestInfo();
					userRequestInfo.setRequestInfo(propertyRequest.getRequestInfo());
					userRequestInfo.setUser(user);

					//create user
					UserResponseInfo userCreateResponse = restTemplate.postForObject(createUrl.toString(),
							userRequestInfo, UserResponseInfo.class);
					user.setId(userCreateResponse.getUser().get(0).getId());
				}
				producer.kafkaTemplate.send(environment.getProperty("update.user"), propertyRequest);
			}
		}

	}

}
