package org.egov.pgr.repository;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.egov.pgr.repository.builder.ServiceTypeQueryBuilder;
import org.egov.pgr.web.contract.ServiceRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTypeRepositoryTest {
	
	@InjectMocks
    private ServiceTypeRepository serviceTypeRepository;
    
	@Mock
	private JdbcTemplate jdbcTemplate;
    
    @Mock
    ServiceTypeQueryBuilder serviceTypeQueryBuilder; 

	@Test(expected = Exception.class)
	public void test_should_persist_service_type() {
		when(serviceTypeQueryBuilder.insertComplaintTypeQuery()).thenReturn("FirstQuery");
		when(jdbcTemplate.update(any(String.class), anyListOf(Object.class))).thenReturn(1);
		when(serviceTypeQueryBuilder.insertServiceTypeQuery()).thenReturn("SecondQuery");
		when(jdbcTemplate.update(any(String.class), anyListOf(Object.class))).thenReturn(1);
		serviceTypeRepository.persistServiceType(any(ServiceRequest.class));
		verify(jdbcTemplate, times(2)).update(any(String.class), anyListOf(Object.class)); 
	}
	
}