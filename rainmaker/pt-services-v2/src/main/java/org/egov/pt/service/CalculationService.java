package org.egov.pt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pt.repository.ServiceRequestRepository;
import org.egov.pt.web.models.*;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CalculationService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

   @Value("${egov.calculation.host}")
   private String calculationHost;

    @Value("${egov.calculation.context.path}")
    private String calculationContextPath;

    @Value("${egov.calculation.endpoint}")
   private String calculationEndpoint;


     public void calculateTax(PropertyRequest request){
         StringBuilder uri = new StringBuilder();
         uri.append(calculationHost).append(calculationContextPath).append(calculationEndpoint);

         CalculationReq calculationReq = createCalculationReq(request);

         Map<String,Calculation> responseMap = ((Map<String, Calculation>)serviceRequestRepository.fetchResult(uri, calculationReq));

         System.out.println("responsemap-> "+responseMap);
         request.getProperties().forEach(property -> {
             property.getPropertyDetails().forEach(propertyDetail -> {
                 if(responseMap.get(propertyDetail.getAssessmentNumber())==null)
                     throw new CustomException("CALCULATION_ERROR","The calculation object is coming null from calculation service");
                 else
                 propertyDetail.setCalculation(responseMap.get(propertyDetail.getAssessmentNumber()));
             });
         });

     }


     private CalculationReq createCalculationReq(PropertyRequest request){
         CalculationReq calculationReq = new CalculationReq();
         calculationReq.setRequestInfo(request.getRequestInfo());

         request.getProperties().forEach(property -> {
             CalculationCriteria calculationCriteria = new CalculationCriteria();
             calculationCriteria.setProperty(property);
             calculationCriteria.setTenantId(property.getTenantId());
             calculationCriteria.setAssessmentYear("2018-19");

             calculationReq.addCalulationCriteriaItem(calculationCriteria);
         });
       return calculationReq;
     }





}
