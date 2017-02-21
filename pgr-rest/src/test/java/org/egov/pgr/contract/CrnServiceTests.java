package org.egov.pgr.contract;

import org.egov.pgr.domain.model.ComplaintRegistrationNumber;
import org.egov.pgr.persistence.CrnRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureStubRunner(
        repositoryRoot = "http://repo.egovernments.org/nexus/content/repositories/snapshots/",
        ids = {"org.egov:pgr-crn-generation:+:stubs:8088"}
)
public class CrnServiceTests {

    @Test
    public void shouldGenerateCrn() throws Exception {
        CrnRepository crnRepository = new CrnRepository(new RestTemplate(), "http://localhost:8088/crn");
        ComplaintRegistrationNumber complaintRegistrationNumber = crnRepository.getCrn();
        assertThat(complaintRegistrationNumber.getValue()).isNotNull();
    }
}
