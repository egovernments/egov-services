package org.egov.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {

	@RequestMapping(value = "/rest/test/testing", method = RequestMethod.GET)
	public String testingOauth2() {
		return "testing";
	}

}