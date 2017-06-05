package org.egov.web.indexer.contract;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.web.indexer.repository.Resources;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class SevaRequestTest {

    @Test
    public void test_deserialize_seva_request() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        SevaRequest sevaRequest = objectMapper.readValue(
                new Resources().getFileContents("sevaRequestFromKafka.json"),
                SevaRequest.class
        );

        RequestInfo requestInfo = sevaRequest.getRequestInfo();
        ServiceRequest serviceRequest = sevaRequest.getServiceRequest();

        assertThat(requestInfo).isNotNull();
        assertThat(serviceRequest).isNotNull();
        assertThat(serviceRequest.getDynamicSingleValue("assignmentId")).isEqualTo("2");
    }
}