package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.common.model.AttributeDefinition;
import org.egov.pgrrest.common.model.ServiceDefinition;
import org.egov.pgrrest.common.model.ValueDefinition;
import org.egov.pgrrest.read.domain.model.ServiceDefinitionSearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ServiceDefinitionRepositoryTest {

    @Autowired
    private ServiceDefinitionRepository serviceDefinitionRepository;

    @Test
    @Sql(scripts = {"/sql/clearServiceDefinition.sql", "/sql/insertServiceDefinition.sql" })
    public void test_should_return_service_definition_for_given_service_code_and_tenant_id() {
        final ServiceDefinition serviceDefinition = serviceDefinitionRepository
            .find(new ServiceDefinitionSearchCriteria("DMV66", "default"));

        assertNotNull(serviceDefinition);
        assertEquals("DMV66", serviceDefinition.getCode());
        assertEquals("default", serviceDefinition.getTenantId());
        final List<AttributeDefinition> attributes = serviceDefinition.getAttributes();
        assertNotNull(attributes);
        assertEquals(1, attributes.size());
        final AttributeDefinition firstAttribute = attributes.get(0);
        assertEquals(1, firstAttribute.getOrder());
        assertEquals("WHISHETN", firstAttribute.getCode());
        assertFalse(firstAttribute.isReadOnly());
        assertTrue(firstAttribute.isRequired());
        assertEquals("singlevaluelist", firstAttribute.getDataType());
        assertEquals("data type description", firstAttribute.getDataTypeDescription());
        assertEquals("What is the ticket/tag/DL number?", firstAttribute.getDescription());
        final List<ValueDefinition> values = firstAttribute.getValues();
        assertNotNull(values);
        assertEquals(2, values.size());
        assertEquals("123", values.get(0).getKey());
        assertEquals("Ford", values.get(0).getName());
        assertEquals("124", values.get(1).getKey());
        assertEquals("Chrysler", values.get(1).getName());
    }

}