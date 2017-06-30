package org.egov;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Configuration;

@RestController
public class ReportController {

	private static final String UTF_8 = "UTF-8";
	// inject via application.properties
	@Value("${welcome.message:test}")
	private String message = "Hello World";

	@RequestMapping("/test")
	public String welcome(Map<String, Object> model) {
		System.out.println("testing the merged application controller");
		model.put("message", this.message);
		return "welcome";
	}
    
	@RequestMapping("/report/getUserInformation/{id}")
	public String getUser(@PathVariable("id") int id) {
		
        
		String userDetails = getUserDetail(id);
		//String homenumber = JsonPath.read(document, "$.phoneNumbers[:2].type").toString();
		
		return userDetails;
	}
	public String getUserDetail(int userID) {
        
		RestTemplate restTemplate = new RestTemplate();
		StringBuffer baseURI = new StringBuffer("http://localhost:8080/SpringBootRestApi/api/user/");
		baseURI.append(userID);
        String user = restTemplate.getForObject(baseURI.toString(), String.class);
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(user);
		String username = JsonPath.read(document, "$.name");
		String homenumber = JsonPath.read(document, "$.salary").toString();
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