package org.egov.propertyUser;

import org.egov.models.PropertyRequest;
import org.egov.propertyUser.userConsumer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@Autowired
	Producer producer;
	@Autowired
	Environment env;
	
	@RequestMapping(method = RequestMethod.POST, path = "/send")
	public void Test(@RequestBody PropertyRequest propertyRequest) {
		producer.send(env.getProperty("validate.user"), propertyRequest);

	}
}
