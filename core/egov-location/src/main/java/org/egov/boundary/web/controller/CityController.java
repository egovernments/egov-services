package org.egov.boundary.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.boundary.domain.model.CityModel;
import org.egov.boundary.domain.model.District;
import org.egov.boundary.domain.service.CityService;
import org.egov.boundary.persistence.entity.City;
import org.egov.boundary.web.contract.CityRequest;
import org.egov.boundary.web.contract.CityResponse;
import org.egov.boundary.web.contract.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RestController
@RequestMapping("/city")
public class CityController {
	
	@Autowired
	private CityService cityService;

    @GetMapping
    public String getCity(@RequestParam(value = "code", required = false) String code) {

        ObjectMapper mapper = new ObjectMapper();
        List<District> districts;
        List<District> result = new ArrayList<>();
        String jsonInString = "";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {

            districts = (List<District>) mapper.readValue(new ClassPathResource("/json/citiesUrl.json").getFile(),
                    new TypeReference<List<District>>() {
                    });
            if (code != null && !code.isEmpty())
                for (District d : districts) {
                    List<CityModel> cities = d.getCities().stream()
                            .filter(c -> c.getCityCode().compareTo(Integer.valueOf(code)) == 0).collect(Collectors.toList());

                    jsonInString = objectMapper.writeValueAsString(!cities.isEmpty() ? cities.get(0) : cities);
                }
            else
                jsonInString = objectMapper.writeValueAsString(districts);
        } catch (IOException e) { 
            e.printStackTrace();
        }

        return jsonInString;
    }
    
    
    @GetMapping("/getCitybyCityRequest")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute CityRequest cityRequest) {
    	CityResponse cityResponse = new CityResponse();
    	City city= cityService.getCityByCityReq(cityRequest);
		ResponseInfo responseInfo = new ResponseInfo();
		cityResponse.setCity(city);
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		cityResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<CityResponse>(cityResponse, HttpStatus.OK);
	}

}