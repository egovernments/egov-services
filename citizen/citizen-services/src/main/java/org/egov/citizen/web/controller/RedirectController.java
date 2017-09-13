package org.egov.citizen.web.controller;


import javax.servlet.http.HttpServletRequest;

import org.egov.citizen.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class RedirectController {
	public static final Logger LOGGER = LoggerFactory
			.getLogger(RedirectController.class);
	
	@Autowired
	private ApplicationProperties applicationProperties;

    @RequestMapping(value = "/pgresponse", method = RequestMethod.GET)
    public String jsonSubmit(HttpServletRequest request) {
    	String body = null;
    	String host = request.getHeader("host");
    	String queryString = request.getQueryString();
    	if(null != queryString){
	    	String[] array = queryString.split("=");
	        LOGGER.info("String array: "+array.length);
	    	if(1 != array.length)
	    		body = array[1];
    	}
        LOGGER.info("Host obtained: "+host);
        LOGGER.info("Body obtained: "+body);
        StringBuilder redirectUrl= new StringBuilder();
        redirectUrl.append(applicationProperties.getRedirectHostName())
                   .append(applicationProperties.getRedirectUrl())
                   .append(applicationProperties.getRedirectAppend());
     //   redirectUrl.append("http://"+host).append(applicationProperties.getRedirectUrl()).append(applicationProperties.getRedirectAppend());

        LOGGER.info("Redirect URL: "+redirectUrl.toString()+body);
        return "redirect:"+redirectUrl.toString()+body;        
    }
    
}
