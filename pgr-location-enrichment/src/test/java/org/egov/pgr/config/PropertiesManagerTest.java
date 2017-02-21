package org.egov.pgr.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PropertiesManagerTest {

    @Autowired
    private PropertiesManager propertiesManager;

    @Test
    public void testThatLocationEnrichedTopicIsPickedFromProperties() throws Exception {
        String expectedTopicName = "test.loc.enriched";
        assertEquals(expectedTopicName,propertiesManager.getLocationEnrichedTopicName());
    }

    @Test
    public void testThatBoundaryServiceHostIsPickedFromProperties() throws Exception {
        String expectedUrl = "http://boundary-service/v1/location/boundarys?boundary.latitude={latitude}&boundary.longitude={longitude}";
        assertEquals(expectedUrl,propertiesManager.getFetchBoundaryByLatLngUrl());
    }
}