package org.egov.tenant.domain.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CityTest {

    @Test
    public void test_should_return_false_when_name_is_not_present() {
        City city = City.builder()
            .name(null)
            .localName("localname")
            .districtCode("districtcode")
            .districtName("districtname")
            .regionName("regionname")
            .tenantCode("tenantcode")
            .longitude(35.456)
            .latitude(75.443)
            .build();

        assertThat(city.isValid()).isFalse();
        assertThat(city.isNameAbsent()).isTrue();
    }

    @Test
    public void test_should_return_true_when_required_fields_are_present() throws Exception {
        City city = City.builder()
            .name("Bengaluru")
            .build();

        assertThat(city.isValid()).isTrue();
        assertThat(city.isNameAbsent()).isFalse();
    }
}