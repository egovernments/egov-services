package org.egov.tenant.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tenant.domain.exception.DuplicateTenantCodeException;
import org.egov.tenant.domain.exception.InvalidTenantDetailsException;
import org.egov.tenant.domain.model.Tenant;
import org.egov.tenant.persistence.repository.MdmsRepository;
import org.egov.tenant.persistence.repository.TenantRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import net.minidev.json.JSONArray;

@RunWith(MockitoJUnitRunner.class)
public class TenantServiceTest {

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private MdmsRepository mdmsRepository;
    
    @Mock
    private List<Tenant> tenants;

    private TenantService tenantService;

    @Before
    public void setUp() throws Exception {
        tenantService = new TenantService(tenantRepository);
    }

    @Test
    public void test_should_save_tenant() {
        Tenant tenant = mock(Tenant.class);
        when(tenant.getCode()).thenReturn("code");
        when(tenantRepository.isTenantPresent("code")).thenReturn(0L);
        when(tenantRepository.save(tenant)).thenReturn(tenant);

        Tenant result = tenantService.createTenant(tenant);

        assertThat(result).isEqualTo(tenant);
    }

    @Test(expected = InvalidTenantDetailsException.class)
    public void test_should_throw_exception_when_tenant_is_invalid() {
        Tenant tenant = Tenant.builder().build();

        tenantService.createTenant(tenant);

        verify(tenantRepository, never()).save(any(Tenant.class));
    }

    @Test(expected = DuplicateTenantCodeException.class)
    public void test_should_throw_exception_when_duplicate_tenant_code_exists() {
        Tenant tenant = mock(Tenant.class);
        when(tenant.getCode()).thenReturn("code");
        when(tenantRepository.isTenantPresent("code")).thenReturn(1L);

        tenantService.createTenant(tenant);

        verify(tenantRepository, never()).save(any(Tenant.class));
    }
    
    @Test
    public void test_should_update_tenant() {
        Tenant tenant = mock(Tenant.class);
        when(tenant.getCode()).thenReturn("code");
        when(tenantRepository.isTenantPresent("code")).thenReturn(1L);
        when(tenantRepository.update(tenant)).thenReturn(tenant);

        Tenant result = tenantService.updateTenant(tenant);

        assertThat(result).isEqualTo(tenant);
    }
    
    @Test(expected = InvalidTenantDetailsException.class)
    public void test_should_throw_exception_when_tenant_isinvalid_inupdate() {
        Tenant tenant = Tenant.builder().build();

        tenantService.updateTenant(tenant);

        verify(tenantRepository, never()).update(any(Tenant.class));
    }
}