package org.egov.pgr.read.domain.service;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.pgr.common.entity.ComplaintStatus;
import org.egov.pgr.common.model.Role;
import org.egov.pgr.common.repository.UserRepository;
import org.egov.pgr.common.contract.GetUserByIdResponse;
import org.egov.pgr.read.web.contract.ResponseInfo;
import org.egov.pgr.read.web.contract.User;
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
    private UserRepository userRepository;

    @Test
    @Sql(scripts = {"/sql/clearComplaintStatusMapping.sql", "/sql/InsertComplaintStatusMapping.sql"})
    public void testShouldReturnComplaintStatusListByUserId() {
        when(userRepository.findUserByIdAndTenantId(18L, "ap.public")).thenReturn(getUserResponse());
        List<org.egov.pgr.read.domain.model.ComplaintStatus> complaintStatuses = complaintStatusMappingService.getStatusByRoleAndCurrentStatus(18L,
            "REGISTERED", "ap.public");
        assertFalse(complaintStatuses.isEmpty());
    }

    private GetUserByIdResponse getUserResponse() {
        final User user = User.builder().id(1L).name("manas").userName("manas").roles(getRoles()).build();
        List<User> userList = new ArrayList<User>();
        userList.add(user);
        return GetUserByIdResponse.builder()
            .responseInfo(ResponseInfo.builder().build())
            .user(userList)
            .build();
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
