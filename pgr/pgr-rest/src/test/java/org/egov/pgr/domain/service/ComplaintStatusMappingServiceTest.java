package org.egov.pgr.domain.service;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.pgr.domain.model.Role;
import org.egov.pgr.persistence.entity.ComplaintStatus;
import org.egov.pgr.persistence.repository.EmployeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ComplaintStatusMappingServiceTest {
    
    @Autowired
    private ComplaintStatusMappingService complaintStatusMappingService;
    
    @MockBean
    private EmployeeRepository employeeRepository;
    
    @Test
    @Sql(scripts = {"/sql/clearComplaintStatusMapping.sql", "/sql/InsertComplaintStatusMapping.sql"})
    public void testShouldReturnComplaintStatusListByUserId() {
        when(employeeRepository.getRolesByUserId(1L, "ap.public")).thenReturn(getRoles());
        List<ComplaintStatus> complaintStatuses = complaintStatusMappingService.getStatusByRoleAndCurrentStatus(1L, "REGISTERED", "ap.public");
        assertFalse(complaintStatuses.isEmpty());
    }
    
    private Set<Role> getRoles() {
        Set<Role> roles = new HashSet<Role>();
        Role role1 = Role.builder().id(1L).name("EMPLOYEE").build();
        Role role2 = Role.builder().id(2L).name("CITIZEN").build();
        roles.add(role1);
        roles.add(role2);
        return roles;
    }
}
