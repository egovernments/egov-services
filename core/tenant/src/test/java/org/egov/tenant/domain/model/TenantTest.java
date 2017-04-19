package org.egov.tenant.domain.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.egov.tenant.domain.exception.InvalidTenantDetailsException;
import org.junit.Before;
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

    @Before
    public void setUp() {
        when(city.isValid()).thenReturn(true);
    }

    @Test(expected = InvalidTenantDetailsException.class)
    public void test_should_throw_exception_when_city_is_not_valid() {
        when(city.isValid()).thenReturn(false);

        Tenant tenant = Tenant.builder()
            .code("tenantcode")
            .description("description")
            .logoId("logoId")
            .imageId("imageId")
            .domainUrl("domainUrl")
            .type("CITY")
            .city(city)
            .build();
        assertThat(tenant.isCityAbsent()).isFalse();
        tenant.validate();
    }

    @Test(expected = InvalidTenantDetailsException.class)
    public void test_should_throw_exception_when_city_is_not_present() {
        Tenant tenant = Tenant.builder()
            .code("tenantcode")
            .description("description")
            .logoId("logoId")
            .imageId("imageId")
            .domainUrl("domainUrl")
            .type("CITY")
            .city(null)
            .build();

        assertThat(tenant.isCityAbsent()).isTrue();
        tenant.validate();
    }

    @Test(expected = InvalidTenantDetailsException.class)
    public void test_should_throw_exception_when_code_is_not_present() {
        Tenant tenant = Tenant.builder()
            .code(null)
            .description("description")
            .logoId("logoId")
            .imageId("imageId")
            .domainUrl("domainUrl")
            .type("CITY")
            .city(city)
            .build();

        assertThat(tenant.isCodeAbsent()).isTrue();
        tenant.validate();
    }

    @Test(expected = InvalidTenantDetailsException.class)
    public void test_should_throw_exception_when_code_is_empty() {
        Tenant tenant = Tenant.builder()
            .code("")
            .description("description")
            .logoId("logoId")
            .imageId("imageId")
            .domainUrl("domainUrl")
            .type("CITY")
            .city(city)
            .build();

        assertThat(tenant.isCodeAbsent()).isTrue();
        tenant.validate();
    }

    @Test(expected = InvalidTenantDetailsException.class)
    public void test_should_throw_exception_when_code_is_more_than_256_characters() {
        String code = RandomStringUtils.random(257);

        Tenant tenant = Tenant.builder()
            .code(code)
            .description("description")
            .logoId("logoId")
            .imageId("imageId")
            .domainUrl("domainUrl")
            .type("CITY")
            .city(city)
            .build();

        assertThat(tenant.isCodeOfInvalidLength()).isTrue();
        tenant.validate();
    }

    @Test(expected = InvalidTenantDetailsException.class)
    public void test_should_throw_exception_when_logoId_is_not_present() {
        Tenant tenant = Tenant.builder()
            .code("tenantcode")
            .description("description")
            .logoId(null)
            .imageId("imageId")
            .domainUrl("domainUrl")
            .type("CITY")
            .city(city)
            .build();

        assertThat(tenant.isLogoIdAbsent()).isTrue();
        tenant.validate();
    }

    @Test(expected = InvalidTenantDetailsException.class)
    public void test_should_throw_exception_when_logoId_is_empty() {
        Tenant tenant = Tenant.builder()
            .code("tenantcode")
            .description("description")
            .logoId("")
            .imageId("imageId")
            .domainUrl("domainUrl")
            .type("CITY")
            .city(city)
            .build();

        assertThat(tenant.isLogoIdAbsent()).isTrue();
        tenant.validate();
    }

    @Test(expected = InvalidTenantDetailsException.class)
    public void test_should_throw_exception_when_imageId_is_not_present() {
        Tenant tenant = Tenant.builder()
            .code("tenantcode")
            .description("description")
            .logoId("logoid")
            .imageId(null)
            .domainUrl("domainUrl")
            .type("CITY")
            .city(city)
            .build();

        assertThat(tenant.isImageIdAbsent()).isTrue();
        tenant.validate();
    }

    @Test(expected = InvalidTenantDetailsException.class)
    public void test_should_throw_exception_when_imageId_is_empty() {
        Tenant tenant = Tenant.builder()
            .code("tenantcode")
            .description("description")
            .logoId("logoid")
            .imageId("")
            .domainUrl("domainUrl")
            .type("CITY")
            .city(city)
            .build();

        assertThat(tenant.isImageIdAbsent()).isTrue();
        tenant.validate();
    }

    @Test(expected = InvalidTenantDetailsException.class)
    public void test_should_throw_exception_when_type_is_not_present() {
        Tenant tenant = Tenant.builder()
            .code("tenantcode")
            .description("description")
            .logoId("logoid")
            .imageId("imageid")
            .domainUrl("domainUrl")
            .type(null)
            .city(city)
            .build();

        assertThat(tenant.isTypeAbsent()).isTrue();
        tenant.validate();
    }

    @Test(expected = InvalidTenantDetailsException.class)
    public void test_should_throw_exception_when_type_is_invalid() {
        Tenant tenant = Tenant.builder()
            .code("tenantcode")
            .description("description")
            .logoId("logoid")
            .imageId("imageid")
            .domainUrl("domainUrl")
            .type("INVALID")
            .city(city)
            .build();

        assertThat(tenant.isTypeInvalid()).isTrue();
        tenant.validate();
    }
}