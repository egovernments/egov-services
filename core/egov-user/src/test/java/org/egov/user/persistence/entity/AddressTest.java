package org.egov.user.persistence.entity;

import org.egov.user.domain.model.enums.AddressType;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddressTest {

    @Test
    public void test_entity_address_should_build_itself_from_domain_address() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("IST"));
        calendar.set(1990, Calendar.JULY, 23);
        Date date = calendar.getTime();

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);

        Address entityAddress = Address.builder()
                .id(1L)
                .type(AddressType.PERMANENT)
                .houseNoBldgApt("house number 1")
                .areaLocalitySector("area/locality/sector")
                .streetRoadLine("street/road/line")
                .landmark("landmark")
                .cityTownVillage("city/town/village 1")
                .postOffice("post office")
                .subDistrict("sub district")
                .district("district")
                .state("state")
                .country("country")
                .pinCode("pincode 1")
                .build();

        org.egov.user.domain.model.Address domainAddress = entityAddress.toDomain();

        assertThat(domainAddress.getId()).isEqualTo(entityAddress.getId());
        assertThat(domainAddress.getType()).isEqualTo(entityAddress.getType());
        assertThat(domainAddress.getHouseNoBldgApt()).isEqualTo(entityAddress.getHouseNoBldgApt());
        assertThat(domainAddress.getAreaLocalitySector()).isEqualTo(entityAddress.getAreaLocalitySector());
        assertThat(domainAddress.getStreetRoadLine()).isEqualTo(entityAddress.getStreetRoadLine());
        assertThat(domainAddress.getLandmark()).isEqualTo(entityAddress.getLandmark());
        assertThat(domainAddress.getCityTownVillage()).isEqualTo(entityAddress.getCityTownVillage());
        assertThat(domainAddress.getPostOffice()).isEqualTo(entityAddress.getPostOffice());
        assertThat(domainAddress.getSubDistrict()).isEqualTo(entityAddress.getSubDistrict());
        assertThat(domainAddress.getDistrict()).isEqualTo(entityAddress.getDistrict());
        assertThat(domainAddress.getState()).isEqualTo(entityAddress.getState());
        assertThat(domainAddress.getCountry()).isEqualTo(entityAddress.getCountry());
        assertThat(domainAddress.getPinCode()).isEqualTo(entityAddress.getPinCode());
    }
}
