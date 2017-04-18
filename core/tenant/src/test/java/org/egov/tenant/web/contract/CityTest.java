package org.egov.tenant.web.contract;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CityTest {

    @Test
    public void test_should_convert_from_contract_to_domain() throws Exception {
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


        org.egov.tenant.domain.model.City domain = cityContract.toDomain();

        assertThat(domain).isEqualTo(expectedCityModel);
    }
}