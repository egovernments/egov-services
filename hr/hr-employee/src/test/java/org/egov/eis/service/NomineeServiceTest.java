package org.egov.eis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.eis.broker.EmployeeProducer;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.NominatingEmployee;
import org.egov.eis.model.Nominee;
import org.egov.eis.model.User;
import org.egov.eis.model.enums.Gender;
import org.egov.eis.model.enums.MaritalStatus;
import org.egov.eis.model.enums.Relationship;
import org.egov.eis.repository.NomineeRepository;
import org.egov.eis.service.helper.NomineeNominatingEmployeeMapper;
import org.egov.eis.utils.SeedHelper;
import org.egov.eis.web.contract.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NomineeServiceTest {

    private SeedHelper seedHelper = new SeedHelper();

    @Mock
    private ObjectMapper mapper;

    @Mock
    private EmployeeProducer employeeProducer;

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
        doReturn(new String()).when(mapper).writeValueAsString(any(NomineeRequest.class));
        doNothing().when(employeeProducer).sendMessage(anyString(), anyString(), anyString());

        List<Nominee> actualNominees = nomineeService.createAsync(seedHelper.getNomineeRequest());

        verify(nomineeRepository).generateSequences(anyInt());
        verify(mapper).writeValueAsString(any(NomineeRequest.class));
        verify(employeeProducer).sendMessage(anyString(), anyString(), anyString());

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

        doReturn(new String()).when(mapper).writeValueAsString(any(NomineeRequest.class));
        doNothing().when(employeeProducer).sendMessage(anyString(), anyString(), anyString());

        List<Nominee> actualNominees = nomineeService.updateAsync(seedHelper.getNomineeRequest());

        verify(mapper).writeValueAsString(any(NomineeRequest.class));
        verify(employeeProducer).sendMessage(anyString(), anyString(), anyString());

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
