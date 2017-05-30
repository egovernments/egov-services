package org.egov.propertyUser.userConsumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.models.User;
import org.egov.models.UserAuthResponseInfo;
import org.egov.propertyUser.model.UserResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@SuppressWarnings("unused")
public class Consumer {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	Environment env;

	@Autowired
	private KafkaTemplate<String, PropertyRequest> kafkaTemplate;


	@Bean
	public Map<String,Object> consumerConfig(){
		Map<String,Object> consumerProperties=new HashMap<String,Object>();
		consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, env.getProperty("autoOffsetResetConfig"));
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("bootstrap-servers"));
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "user");
		return consumerProperties;
	}

	@Bean
	public ConsumerFactory<String, PropertyRequest> consumerFactory(){
		return new DefaultKafkaConsumerFactory<>(consumerConfig(),new StringDeserializer(),
				new JsonDeserializer<>(PropertyRequest.class));

	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, PropertyRequest> kafkaListenerContainerFactory(){
		ConcurrentKafkaListenerContainerFactory<String, PropertyRequest> factory=new ConcurrentKafkaListenerContainerFactory<String,PropertyRequest>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}


	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}



	@KafkaListener(topics ="${validate.user}")
	public void receive(PropertyRequest propertyRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization",env.getProperty("authkey") );

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("username", env.getProperty("oauth.username"));
		map.add("password", env.getProperty("password"));
		map.add("grant_type", env.getProperty("grant_type"));
		map.add("scope", env.getProperty("scope"));
		map.add("tenantId",propertyRequest.getProperties().get(0).getOwners().get(0).getTenantId());

		HttpEntity<MultiValueMap<String, String>> requestEntity= 
				new HttpEntity<MultiValueMap<String, String>>(map, headers);


		UserAuthResponseInfo userAuthResponseInfo=	restTemplate.postForObject(env.getProperty("user.auth"), requestEntity, UserAuthResponseInfo.class);

		propertyRequest.getRequestInfo().setAuthToken(userAuthResponseInfo.getAccess_token());

		for(Property property:propertyRequest.getProperties()){
			for(User user: property.getOwners()){
				if(user.getId() !=null){

					Map<String,Object> userSearchRequestInfo=new HashMap<String,Object >();
					userSearchRequestInfo.put("username",propertyRequest.getProperties().get(0).getOwners().get(0).getUsername());
					userSearchRequestInfo.put("tenantId",propertyRequest.getProperties().get(0).getOwners().get(0).getTenantId());
					userSearchRequestInfo.put("RequestInfo",propertyRequest.getRequestInfo());

					UserResponseInfo userResponse= restTemplate.postForObject(env.getProperty("user.searchUrl"), userSearchRequestInfo, UserResponseInfo.class);

					if(userResponse.getResponseInfo().getStatus().equalsIgnoreCase(env.getProperty("statusCode"))){
						if(userResponse.getUsers()==null){
							Map<String,Object> userCreateRequestInfo=new HashMap<String,Object >();
							userCreateRequestInfo.put("User",propertyRequest.getProperties().get(0).getOwners());
							userCreateRequestInfo.put("RequestInfo",propertyRequest.getRequestInfo());


							UserResponseInfo userCreateResponse = restTemplate.postForObject(env.getProperty("user.createUrl"),
									userCreateRequestInfo, UserResponseInfo.class);	
						}
						else{
							propertyRequest.getProperties().get(0).getOwners().get(0).setId(userResponse.getUsers().get(0).getId());
							kafkaTemplate.send(env.getProperty("update.user"), propertyRequest);
						}
					}

				}else{
					Map<String,Object> userCreateRequestInfo=new HashMap<String,Object >();
					userCreateRequestInfo.put("User",propertyRequest.getProperties().get(0).getOwners());
					userCreateRequestInfo.put("RequestInfo",propertyRequest.getRequestInfo());

					UserResponseInfo userCreateResponse = restTemplate.postForObject(env.getProperty("user.createUrl"),
							userCreateRequestInfo, UserResponseInfo.class);
					kafkaTemplate.send(env.getProperty("update.user"), propertyRequest);

				}
			}
		}

	}

}
