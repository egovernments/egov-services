package org.egov.pgr.domain.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.egov.pgr.domain.model.ServiceTypeConfiguration;
import org.egov.pgr.domain.service.validator.ServiceTypeConfigurationValidator;
import org.egov.pgr.persistence.repository.ServiceTypeConfigurationMessageQueueRepository;
import org.egov.pgr.persistence.repository.ServiceTypeConfigurationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTypeConfigurationServiceTest {

    @Mock
    private ServiceTypeConfigurationValidator serviceTypeConfigurationValidator;

    @InjectMocks
    private ServiceTypeConfigurationService serviceTypeConfigurationService;
    
    @Mock 
    ServiceTypeConfigurationMessageQueueRepository serviceTypeConfigurationMessageQueueRepository;
    @Mock
    ServiceTypeConfigurationRepository serviceTypeConfigurationRepository;

    @Before
    public void before(){
        when(serviceTypeConfigurationValidator.canValidate(any())).thenReturn(true);
        final List<ServiceTypeConfigurationValidator> validators = Collections.singletonList(serviceTypeConfigurationValidator);
        serviceTypeConfigurationService = new ServiceTypeConfigurationService(validators,serviceTypeConfigurationMessageQueueRepository,serviceTypeConfigurationRepository);
    }

    @Test
    public void test_should_validate_tenantid_and_servicecode(){

        ServiceTypeConfiguration configuration = getServiceTypeConfiguration();

        serviceTypeConfigurationService.create(configuration);
    }

    @Test
    public void test_should_check_for_validation(){

        ServiceTypeConfiguration configuration = getServiceTypeConfigurationWithoutTenantId();

        serviceTypeConfigurationService.create(configuration);

        verify(serviceTypeConfigurationValidator, times(1)).validate(configuration);
    }
    
    @Test
    public void test_should_validate_tenantid_and_servicecode_befor_update(){

        ServiceTypeConfiguration configuration = getServiceTypeConfiguration();

        serviceTypeConfigurationService.update(configuration);
    }

    @Test
    public void test_should_check_for_validation_befor_update(){

        ServiceTypeConfiguration configuration = getServiceTypeConfigurationWithoutTenantId();

        serviceTypeConfigurationService.update(configuration);
        verify(serviceTypeConfigurationValidator, times(1)).validate(configuration);


    }
    
    
    private ServiceTypeConfiguration getServiceTypeConfiguration(){
        return ServiceTypeConfiguration.builder()
                .serviceCode("NOC")
                .tenantId("default")
                .url("abc")
                .glCode("abc")
                .slaEnabled(true)
                .source("abc")
                .build();
    }

    private ServiceTypeConfiguration getServiceTypeConfigurationWithoutTenantId(){
        return ServiceTypeConfiguration.builder()
                .serviceCode("NOC")
                .url("abc")
                .glCode("abc")
                .slaEnabled(true)
                .source("abc")
                .build();
    }

}