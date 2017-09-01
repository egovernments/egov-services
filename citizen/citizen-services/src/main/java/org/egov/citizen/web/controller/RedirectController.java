package org.egov.citizen.web.controller;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.citizen.config.ApplicationProperties;
import org.egov.citizen.web.contract.PGPayloadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RedirectController {
	public static final Logger LOGGER = LoggerFactory
			.getLogger(RedirectController.class);
	
	@Autowired
	private ApplicationProperties applicationProperties;

	

    @RequestMapping(value = "/responses", method = RequestMethod.GET)
    public ModelAndView showForm() {
        return new ModelAndView("pgPayLoadView", "pgPayLoad", new PGPayloadResponse());
    }

    @RequestMapping(value = "/pgjsonresponse", method = RequestMethod.POST)
    public String jsonSubmit(HttpServletRequest request) {
    	String body = null;
    	try{
    		if ("POST".equalsIgnoreCase(request.getMethod())) 
    		{
    		   body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    		}
    	}catch(Exception e){
    		
    	}
        LOGGER.info("Body obtained: "+body);
        StringBuilder redirectUrl= new StringBuilder();
        redirectUrl.append(applicationProperties.getRedirectUrl()).append(applicationProperties.getRedirectAppend());
        
        return "redirect:http://www.google.com";
        
      // return "redirect:"+redirectUrl.toString()+body;
    }

    @RequestMapping(value = "/pgresponses", method = RequestMethod.POST)
    public String submit(@Valid @ModelAttribute("pGPayloadResponse")PGPayloadResponse PGPayloadResponse, 
      BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "error";
        }
        
        model.addAttribute("billNumber", PGPayloadResponse.getBillNumber());
        model.addAttribute("billService", PGPayloadResponse.getBillService());
        model.addAttribute("amount", PGPayloadResponse.getAmountPaid());
        model.addAttribute("responseHash", PGPayloadResponse.getResponseHash());
        model.addAttribute("trasnactionId", PGPayloadResponse.getTransactionId());
        model.addAttribute("serviceRequestId", PGPayloadResponse.getServiceRequestId());
        model.addAttribute("uid", PGPayloadResponse.getUid());
        model.addAttribute("paymentMethod", PGPayloadResponse.getPaymentMethod());
        model.addAttribute("consumerCode", PGPayloadResponse.getConsumerCode());
        model.addAttribute("tenantid", PGPayloadResponse.getTenantId());
        model.addAttribute("otherDetails", PGPayloadResponse.getOtherDetails());
        model.addAttribute("status", PGPayloadResponse.getStatus());
        model.addAttribute("status", PGPayloadResponse.getRequestInfo());

        
        LOGGER.info("Model Binding: "+model.toString());
        StringBuilder redirectUrl= new StringBuilder();
        redirectUrl.append(applicationProperties.getRedirectUrl()).append(applicationProperties.getRedirectAppend());
        
       return "redirect:"+redirectUrl.toString()+model.toString();
    }
    
    @RequestMapping(value = "/pgresponse", method = RequestMethod.POST)
    public String jsonSubmit(@RequestParam(value="msg") String request) {
        LOGGER.info("Body obtained: "+request);
        StringBuilder redirectUrl= new StringBuilder();
        redirectUrl.append(applicationProperties.getRedirectUrl()).append(applicationProperties.getRedirectAppend());
              
       return "redirect:"+redirectUrl.toString()+request;
    }
}
