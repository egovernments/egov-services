package org.egov.tenant.web.contract;

import org.egov.tenant.domain.model.TenantType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TenantTest {

    @Mock
    City city;

    @Test
    public void test_should_convert_contract_to_model() {
        City cityContract = City.builder()
                .name("Bengaluru")
                .localName("localname")
                .districtCode("districtcode")
                .districtName("districtname")
                .regionName("regionname")
                .longitude(35.456)
                .latitude(75.443)
                .build();




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
                .type(TenantType.CITY)
                .city(expectedCityModel)
                .build();

        when(city.toDomain()).thenReturn(expectedCityModel);

        Tenant tenantContract = Tenant.builder()
                .code("AP.KURNOOL")
                .description("description")
                .logoId("logoId")
                .imageId("imageId")
                .domainUrl("domainUrl")
                .type("CITY")
                .city(city)
                .build();


        org.egov.tenant.domain.model.Tenant actualTenantModel = tenantContract.toDomain();

        assertThat(expectedTenantModel).isEqualTo(actualTenantModel);

    }
}