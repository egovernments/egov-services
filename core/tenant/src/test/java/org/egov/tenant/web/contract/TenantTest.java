package org.egov.tenant.web.contract;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TenantTest {

    @Mock
    private City cityContract;

    @Test
    public void test_should_convert_contract_to_model() {

        org.egov.tenant.domain.model.City expectedCityModel = org.egov.tenant.domain.model.City.builder()
                .name("Bengaluru")
                .localName("localname")
                .districtCode("districtcode")
                .districtName("districtname")
                .regionName("regionname")
                .longitude(35.456)
                .latitude(75.443)
                .build();

        org.egov.tenant.domain.model.Tenant expectedTenantModel = org.egov.tenant.domain.model.Tenant.builder()
                .code("AP.KURNOOL")
                .description("description")
                .logoId("logoId")
                .imageId("imageId")
                .domainUrl("domainUrl")
                .type("CITY")
                .city(expectedCityModel)
                .build();

        when(cityContract.toDomain()).thenReturn(expectedCityModel);

        Tenant tenantContract = Tenant.builder()
                .code("AP.KURNOOL")
                .description("description")
                .logoId("logoId")
                .imageId("imageId")
                .domainUrl("domainUrl")
                .type("CITY")
                .city(cityContract)
                .build();


        org.egov.tenant.domain.model.Tenant actualTenantModel = tenantContract.toDomain();

        assertThat(actualTenantModel).isEqualTo(expectedTenantModel);

    }
}