package org.egov.pgr.employee.enrichment.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class PropertiesManagerTest {


    @Autowired
    private PropertiesManager propertiesManager;

    @Test
    public void testAssigneeServiceUrlIsReturned() throws Exception {
        String expectedUrl = "http://workflow/assignee";
        assertEquals(expectedUrl, propertiesManager.getAssigneeUrl());
    }
}