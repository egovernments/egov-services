package org.egov.access.domain.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by parvati on 31/3/17.
 */
public class ActionSearchCriteriaTest {

    @Test
    public void testForEqualsAndHashCode() throws  Exception {
        List<Long> roleIdsList1 = new ArrayList<Long>();
        roleIdsList1.add(4L);
        roleIdsList1.add(5L);
        ActionSearchCriteria searchCriteria1 = ActionSearchCriteria.builder().roleIds(roleIdsList1).build();
        ActionSearchCriteria searchCriteria2 = ActionSearchCriteria.builder().roleIds(roleIdsList1).build();

        assertEquals(searchCriteria1,searchCriteria2);
        assertEquals(searchCriteria1.hashCode(),searchCriteria2.hashCode());
    }

    @Test
    public void testForNotEqualObjects() throws Exception {
        List<Long> roleIdsList1 = new ArrayList<Long>();
        roleIdsList1.add(4L);
        roleIdsList1.add(5L);
        ActionSearchCriteria searchCriteria1 = ActionSearchCriteria.builder().roleIds(roleIdsList1).build();
        List<Long> roleIdsList2 = new ArrayList<Long>();
        roleIdsList2.add(3L);
        roleIdsList2.add(2L);
        ActionSearchCriteria searchCriteria2 = ActionSearchCriteria.builder().roleIds(roleIdsList2).build();

        assertNotEquals(searchCriteria1,searchCriteria2);
        assertNotEquals(searchCriteria1.hashCode(),searchCriteria2.hashCode());
    }
}
