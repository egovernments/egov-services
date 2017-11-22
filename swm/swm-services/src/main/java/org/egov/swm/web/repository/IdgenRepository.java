package org.egov.swm.web.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.web.contract.IdGenerationRequest;
import org.egov.swm.web.contract.IdGenerationResponse;
import org.egov.swm.web.contract.IdRequest;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.Error;
import org.egov.tracer.model.ErrorRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Repository
public class IdgenRepository {

    private static final Logger logger = LoggerFactory.getLogger(IdgenRepository.class);

    @Value("${egov.services.egov_idgen.hostname}")
    private String host;

    @Value("${egov.services.egov_idgen.createpath}")
    private String createPath;

    @Autowired
    private LogAwareRestTemplate restTemplate;

    public String getIdGeneration(final String tenantId, final RequestInfo requestInfo, final String name) {

        final StringBuffer idGenerationUrl = new StringBuffer();
        idGenerationUrl.append(host);
        idGenerationUrl.append(createPath);

        final List<IdRequest> idRequests = new ArrayList<>();
        final IdRequest idrequest = new IdRequest();
        idrequest.setFormat(null);
        idrequest.setIdName(name);
        idrequest.setTenantId(tenantId);
        final IdGenerationRequest idGeneration = new IdGenerationRequest();
        idRequests.add(idrequest);
        idGeneration.setIdRequests(idRequests);
        idGeneration.setRequestInfo(requestInfo);
        String response = null;

        try {
            logger.info("UpicNoGeneration calling id generation service url :" + idGenerationUrl.toString()
                    + " request is " + requestInfo);
            response = restTemplate.postForObject(idGenerationUrl.toString(), idGeneration, String.class);
        } catch (final Exception ex) {
            throw new CustomException("InValidUrl", idGenerationUrl.toString());
        }

        String responseNumber = null;
        final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        final ErrorRes errorResponse = gson.fromJson(response, ErrorRes.class);
        final IdGenerationResponse idResponse = gson.fromJson(response, IdGenerationResponse.class);

        if (errorResponse.getErrors() != null && errorResponse.getErrors().size() > 0) {
            final Error error = errorResponse.getErrors().get(0);
            throw new CustomException(error.getMessage(), error.getDescription());
        } else if (idResponse.getResponseInfo() != null)
            if (idResponse.getResponseInfo().getStatus().toString().equalsIgnoreCase("SUCCESSFUL"))
                if (idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0)
                    responseNumber = idResponse.getIdResponses().get(0).getId();
        return responseNumber;
    }

}
