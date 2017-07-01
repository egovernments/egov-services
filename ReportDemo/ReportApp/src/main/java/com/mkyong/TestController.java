package com.mkyong;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Configuration;

@Controller
public class TestController {

	private static final String UTF_8 = "UTF-8";
	// inject via application.properties
	@Value("${welcome.message:test}")
	private String message = "Hello World";

	
	@RequestMapping("/testController")
	public String getUserTest() {
		System.out.println("readin the json file");
		String user = getFileContents("user.json");
		
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(user);

		String username = JsonPath.read(document, "$.firstName");
		String homenumber = JsonPath.read(document, "$.phoneNumbers[:2].type").toString();
		System.out.println("User Information is" +username);
		String userDetails = username + " " + homenumber;
		return userDetails;
	}
	public String getFileContents(final String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader()
                .getResourceAsStream(fileName), UTF_8).replace("\n", "");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}