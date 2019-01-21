package org.egov.hrms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.hrms.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class IdGenService {

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public String generate(final String tenantId, final String idGenName, final String idGenFormat, RequestInfo requestInfo)
             {
        String url = propertiesManager.getIdGenServiceHostName() + propertiesManager.getIdGenServiceCreatePath();

        String employeeCode = null;
//        List<IdRequest> idRequests = new ArrayList<>();
//
//        IdRequest idRequest = IdRequest.builder().idName(idGenName).format(idGenFormat).tenantId(tenantId).build();
//        idRequests.add(idRequest);
//        IdGenerationRequest idGenerationRequest = IdGenerationRequest.builder()
//                .idRequests(idRequests).requestInfo(requestInfo).build();
//
//        String idGenerationResponse = null;
//        try {
//            idGenerationResponse = restTemplate.postForObject(url, idGenerationRequest, String.class);
//        } catch (Exception ex) {
//            throw new IdGenerationException(null, requestInfo);
//        }
//
//        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
//        IdGenerationResponse idResponse = gson.fromJson(idGenerationResponse, IdGenerationResponse.class);
//        IdGenerationErrorResponse idGenerationErrorResponse = gson.fromJson(idGenerationResponse, IdGenerationErrorResponse.class);
//
//        if (!idGenerationErrorResponse.getErrors().isEmpty()) {
//            throw new IdGenerationException(idGenerationErrorResponse, requestInfo);
//        } else if (idResponse.getResponseInfo() != null) {
//            if (idResponse.getResponseInfo().getStatus().equalsIgnoreCase("SUCCESSFUL")) {
//                if (idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0)
//                    employeeCode = idResponse.getIdResponses().get(0).getId();
//            }
//        }
        return employeeCode;
    }
}
