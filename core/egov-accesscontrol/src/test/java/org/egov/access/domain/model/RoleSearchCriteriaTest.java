package org.egov.access.domain.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RoleSearchCriteriaTest {

    @Test
    public void testForEqualsAndHashCode() {
        RoleSearchCriteria searchCriteria1 = RoleSearchCriteria.builder().codes("EMP").build();
        RoleSearchCriteria searchCriteria2 = RoleSearchCriteria.builder().codes("EMP").build();

        assertEquals(searchCriteria1, searchCriteria2);
        assertEquals(searchCriteria1.hashCode(), searchCriteria2.hashCode());
    }

    @Test
    public void testForNotEqualObjects() {
        RoleSearchCriteria searchCriteria1 = RoleSearchCriteria.builder().codes("CITIZEN,EMPLOYEE").build();
        RoleSearchCriteria searchCriteria2 = RoleSearchCriteria.builder().codes("CITIZEN").build();

        assertNotEquals(searchCriteria1, searchCriteria2);
        assertNotEquals(searchCriteria1.hashCode(), searchCriteria2.hashCode());
    }

    @Test
    public void testGetCodesReturnListOfCommaSeparatedCode(){
        RoleSearchCriteria searchCriteria = RoleSearchCriteria.builder().codes("CITIZEN,EMPLOYEE").build();
        assertEquals(Arrays.asList("CITIZEN", "EMPLOYEE"), searchCriteria.getCodes());
    }

    @Test
    public void testGetCodesTrimsTheEachCodeBeforeAddingThemToList(){
        RoleSearchCriteria searchCriteria1 = RoleSearchCriteria.builder().codes("   CITIZEN, EMPLOYEE      ").build();
        assertEquals(Arrays.asList("CITIZEN", "EMPLOYEE"), searchCriteria1.getCodes());
    }
}
