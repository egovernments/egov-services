package org.egov.encryption.accesscontrol;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.Role;
import org.egov.encryption.models.AccessType;
import org.egov.encryption.models.Attribute;
import org.egov.encryption.models.RoleAttribute;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@Slf4j
public class AbacFilterTest {

    @Mock
    private AbacFilter abacFilter;

    private Role role1, role2;
    private Attribute attribute;

    @Before
    public void init() {
        role1 = Role.builder().id(1L).code("GRO").name("GRO").build();
        attribute = Attribute.builder().id(1L).name("mobile")
                .jsonPath("$.user.mobileNumber").maskingTechnique("mobile").build();

        RoleAttribute roleAttribute = RoleAttribute.builder().role(role1)
                .attributes(Collections.singletonList(attribute)).accessType(AccessType.MASK).build();


        role2 = Role.builder().id(2L).code("LME").name("LME").build();

        RoleAttribute roleAttribute2 = RoleAttribute.builder().role(role2)
                .attributes(Collections.singletonList(attribute)).accessType(AccessType.PLAIN).build();


        abacFilter = new AbacFilter(Arrays.asList(roleAttribute, roleAttribute2));
    }

    @Test
    public void test() {

        Map<Attribute, AccessType> attributeAccessesForRole = abacFilter.getAttributeAccessForRole(Arrays.asList(role1
                , role2));
        attributeAccessesForRole.forEach((a, b) -> log.info(a + " \n " + b));

    }

}