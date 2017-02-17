package org.egov.boundary.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.boundary.model.District;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RestController
@RequestMapping("/city")
public class CityController {

    @GetMapping
    public String getCity() {

        ObjectMapper mapper = new ObjectMapper();
        List<District> districts = new ArrayList<District>();
        String jsonInString = "";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {

            districts = (List<District>) mapper.readValue(new ClassPathResource("/json/citiesUrl.json").getFile(),
                    new TypeReference<List<District>>() {
                    });
            jsonInString = objectMapper.writeValueAsString(districts);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonInString;
    }

}