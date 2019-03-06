package org.egov.encryption.accesscontrol;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.Role;
import org.egov.encryption.models.Attribute;
import org.egov.encryption.models.AttributeAccess;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class AbacFilterTest {

    @Mock
    private AbacFilter abacFilter;

    private Role role1, role2;
    private Attribute attribute;

    @Before
    public void init() {
//        role1 = Role.builder().id(1L).code("GRO").name("GRO").build();
//        attribute = Attribute.builder().id(1L).name("mobile")
//                .jsonPath("$.user.mobileNumber").maskingTechnique("mobile").build();
//
//        RoleAttributeAccess roleAttribute = RoleAttributeAccess.builder().role(role1)
//                .attributes(Collections.singletonList(attribute)).accessType(AccessType.MASK).build();
//
//
//        role2 = Role.builder().id(2L).code("LME").name("LME").build();
//
//        RoleAttributeAccess roleAttribute2 = RoleAttributeAccess.builder().role(role2)
//                .attributes(Collections.singletonList(attribute)).accessType(AccessType.PLAIN).build();
//
//
//        abacFilter = new AbacFilter(Arrays.asList(roleAttribute, roleAttribute2));
    }

    @Test
    public void test() {
        Map<Role, List<AttributeAccess>> roleAttributeMap = new HashMap<>();


    }

}