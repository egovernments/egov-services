package org.egov.tenant.domain.service;

import org.egov.tenant.domain.model.Tenant;
import org.egov.tenant.domain.model.TenantSearchCriteria;
import org.egov.tenant.persistence.repository.TenantRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TenantServiceTest {

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private List<Tenant> tenants;

    private TenantService tenantService;

    @Before
    public void setUp() throws Exception {
        tenantService = new TenantService(tenantRepository);
    }

    @Test
    public void test_should_retrieve_tenants() {
        TenantSearchCriteria tenantSearchCriteria = new TenantSearchCriteria(asList("code1", "code2"));
        when(tenantRepository.find(tenantSearchCriteria)).thenReturn(tenants);

        List<Tenant> result = tenantService.find(tenantSearchCriteria);

        assertThat(result).isEqualTo(tenants);
    }

    @Test
    public void test_should_save_tenant() {
        Tenant tenant = mock(Tenant.class);

        when(tenantRepository.save(tenant)).thenReturn(tenant);

        Tenant result = tenantService.createTenant(tenant);

        assertThat(result).isEqualTo(tenant);
    }
}