package org.egov.eis.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.Nominee;
import org.egov.eis.model.User;
import org.egov.eis.repository.NomineeRepository;
import org.egov.eis.service.helper.NomineeNominatingEmployeeMapper;
import org.egov.eis.utils.SeedHelper;
import org.egov.eis.web.contract.EmployeeCriteria;
import org.egov.eis.web.contract.NomineeGetRequest;
import org.egov.eis.web.contract.NomineeRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class NomineeServiceTest {

    private SeedHelper seedHelper = new SeedHelper();

    @Mock
    private ObjectMapper mapper;

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private NomineeRepository nomineeRepository;

    @Mock
    private EmployeeDocumentsService documentsService;

    @Mock
    private PropertiesManager propertiesManager;

    @Mock
    private UserService userService;

    @Mock
    private NomineeNominatingEmployeeMapper employeeMapper;

    @InjectMocks
    private NomineeService nomineeService;

    @Test
    public void testGetNominees() {
        List<Nominee> expectedNominees = seedHelper.getNominees();

        doReturn(expectedNominees).when(nomineeRepository).findForCriteria(any(NomineeGetRequest.class));
        doReturn(Arrays.asList(new User())).when(userService).getUsers(any(EmployeeCriteria.class), any(RequestInfo.class));
        doNothing().when(employeeMapper).mapNominatingEmployeesWithNominees(anyListOf(Nominee.class), anyListOf(User.class));

        List<Nominee> actualNominees = nomineeService.getNominees(new NomineeGetRequest(), new RequestInfo());

        verify(nomineeRepository).findForCriteria(any(NomineeGetRequest.class));
        verify(userService).getUsers(any(EmployeeCriteria.class), any(RequestInfo.class));
        verify(employeeMapper).mapNominatingEmployeesWithNominees(anyListOf(Nominee.class), anyListOf(User.class));

        assertEquals(expectedNominees, actualNominees);
    }

    @Test
    public void testCreateAsync() throws JsonProcessingException {
        List<Nominee> expectedNominees = seedHelper.getNominees();
        doReturn(Arrays.asList(1L)).when(nomineeRepository).generateSequences(anyInt());
        List<Nominee> actualNominees = nomineeService.createAsync(seedHelper.getNomineeRequest());
        verify(nomineeRepository).generateSequences(anyInt());
        verify(nomineeRepository).save(anyListOf(Nominee.class));
        assertEquals(expectedNominees, actualNominees);
    }

    @Test
    public void testCreate() throws JsonProcessingException {
        doNothing().when(nomineeRepository).save(anyListOf(Nominee.class));
        nomineeService.create(seedHelper.getNomineeRequest());
        verify(nomineeRepository).save(anyListOf(Nominee.class));
    }

    @Test
    public void testUpdateAsync() throws JsonProcessingException {
        List<Nominee> expectedNominees = seedHelper.getNominees();
        List<Nominee> actualNominees = nomineeService.updateAsync(seedHelper.getNomineeRequest());
        verify(nomineeRepository).update(anyListOf(Nominee.class));
        assertEquals(expectedNominees, actualNominees);
    }

    @Test
    public void testUpdate() throws JsonProcessingException {
        doNothing().when(nomineeRepository).update(anyListOf(Nominee.class));
        doNothing().when(documentsService).updateDocumentsForNominee(anyListOf(Nominee.class));
        nomineeService.update(seedHelper.getNomineeRequest());
        verify(nomineeRepository).update(anyListOf(Nominee.class));
        verify(documentsService).updateDocumentsForNominee(anyListOf(Nominee.class));
    }
}
