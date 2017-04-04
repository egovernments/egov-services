package org.egov.user.domain.service;

import org.egov.user.domain.exception.InvalidAccessTokenException;
import org.egov.user.persistence.repository.ActionRestRepository;
import org.egov.user.web.contract.ActionResponse;
import org.egov.user.web.contract.auth.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private TokenStore tokenStore;

    @Mock
    private ActionRestRepository actionRestRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testToGetUser() {
       CustomUserDetails expectedCustomUserDetails=getCustomUserDetails();
        OAuth2Authentication oAuth2Authentication = mock(OAuth2Authentication.class);
        when(tokenStore.readAuthentication("c80e0ade-f48d-4077-b0d2-4e58526a6bfd")).thenReturn(oAuth2Authentication);
        SecureUser secureUser = new SecureUser(getUser());
        when(oAuth2Authentication.getPrincipal()).thenReturn(secureUser);
        when(actionRestRepository.getActionByRoleId(getRoleIds())).thenReturn(getActionResponse());
        CustomUserDetails customUserDetails=tokenService.getUser("c80e0ade-f48d-4077-b0d2-4e58526a6bfd");

        assertEquals(expectedCustomUserDetails.getId(),customUserDetails.getId());
        assertEquals(expectedCustomUserDetails.getUserName(),customUserDetails.getUserName());
        assertEquals(expectedCustomUserDetails.getName(),customUserDetails.getName());
        assertEquals(expectedCustomUserDetails.getMobileNumber(),customUserDetails.getMobileNumber());
        assertEquals(expectedCustomUserDetails.getEmailId(),customUserDetails.getEmailId());
        assertEquals(expectedCustomUserDetails.getLocale(),customUserDetails.getLocale());
        assertEquals(expectedCustomUserDetails.getType(),customUserDetails.getType());
        assertEquals(expectedCustomUserDetails.getRoles().get(0).getId(),customUserDetails.getRoles().get(0).getId());
        assertEquals(expectedCustomUserDetails.getRoles().get(0).getName(),customUserDetails.getRoles().get(0).getName());
        assertEquals(expectedCustomUserDetails.getActions().get(0).getDisplayName(),customUserDetails.getActions().get(0).getDisplayName());
        assertEquals(expectedCustomUserDetails.getActions().get(0).getOrderNumber(),customUserDetails.getActions().get(0).getOrderNumber());
        assertEquals(expectedCustomUserDetails.getActions().get(0).getParentModule(),customUserDetails.getActions().get(0).getParentModule());
        assertEquals(expectedCustomUserDetails.getActions().get(0).getQueryParams(),customUserDetails.getActions().get(0).getQueryParams());
        assertEquals(expectedCustomUserDetails.getActions().get(0).getServiceCode(),customUserDetails.getActions().get(0).getServiceCode());
        assertEquals(expectedCustomUserDetails.getActions().get(0).getUrl(),customUserDetails.getActions().get(0).getUrl());
    }

    @Test
    public void testToGetUserWithoutAccessToken() {
            thrown.expect(InvalidAccessTokenException.class);
            tokenService.getUser("");

      }
    public User getUser(){
        User user=User.builder().id(18L).userName("narasappa").name("narasappa")
                .mobileNumber("123456789").emailId("abc@gmail.com").locale("en_IN").type("EMPLOYEE").active(Boolean.TRUE)
                .roles(getRoles()).build();
        return user;
    }

    public List<Role>  getRoles(){
        List<Role> roles=new ArrayList<Role>();
        org.egov.user.domain.model.Role roleModel=new org.egov.user.domain.model.Role();
        roleModel.setId(15L);
        roleModel.setName("Employee");

        Role role=new Role(roleModel);

        roles.add(role);

        return roles;
    }
    public CustomUserDetails getCustomUserDetails(){
        SecureUser secureUser=new SecureUser(getUser());
        return new CustomUserDetails(secureUser,getActions());
    }

    public List<Action> getActions(){
        List<Action> actions = new ArrayList<Action>();
        Action action =Action.builder().url("/pgr/receivingmode").name("Get all ReceivingMode").displayName("Get all ReceivingMode").orderNumber(0).queryParams("tenantId=").parentModule("1").serviceCode("PGR").build();
        actions.add(action);
        return actions;
    }

    public List<Long> getRoleIds(){
       return  getRoles().stream().map(role ->role.getId()) .collect(Collectors.toList());
    }

    public ActionResponse getActionResponse(){
       return ActionResponse.builder().actions(getActions()).build();
    }
}