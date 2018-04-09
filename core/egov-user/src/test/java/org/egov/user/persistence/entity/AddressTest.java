package org.egov.user.persistence.entity;

import org.egov.user.domain.model.enums.AddressType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddressTest {

    @Test
    public void test_entity_address_should_build_itself_from_domain_address() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(new UserKey(1L, "tenant"));

        Address entityAddress = Address.builder()
                .id(1L)
                .type("PERMANENT")
                .city("city1")
                .address("post office")
                .pinCode("pincode 1")
                .build();

        org.egov.user.domain.model.Address domainAddress = entityAddress.toDomain();

        assertThat(domainAddress.getType()).isEqualTo(AddressType.PERMANENT);
        assertThat(domainAddress.getPinCode()).isEqualTo("pincode 1");
        assertThat(domainAddress.getAddress()).isEqualTo("post office");
    }
}
