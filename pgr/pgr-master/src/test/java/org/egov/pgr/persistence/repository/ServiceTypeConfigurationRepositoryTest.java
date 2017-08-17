package org.egov.pgr.persistence.repository;

import org.egov.pgr.persistence.dto.ServiceTypeConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ServiceTypeConfigurationRepositoryTest {

    @InjectMocks
    private ServiceTypeConfigurationRepository serviceTypeConfigurationRepository;

    @Test
    public void test_should_persist_servicetype_configuration(){

    }
    
    @Test
    public void test_should_update_servicetype_configuration(){

    }

    private ServiceTypeConfiguration getDto(){
        return ServiceTypeConfiguration.builder()
                .tenantId("default")
                .serviceCode("NOC")
                .applicationFeesEnabled(true)
                .notificationEnabled(true)
                .slaEnabled(true)
                .glCode("123")
                .online(true)
                .source("abc")
                .url("http://egov.org")
                .build();
    }
}