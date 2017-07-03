package org.egov.pgrrest.common.persistence.repository;

import org.egov.pgrrest.TestConfiguration;
import org.egov.pgrrest.common.persistence.entity.ServiceType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfiguration.class)
public class ServiceTypeJpaRepositoryTest {

    @Autowired
    private ServiceRequestTypeJpaRepository repository;

    @Test
    @Sql(scripts = {"/sql/clearComplaintType.sql", "/sql/InsertComplaintType.sql"})
    public void test_should_find_all_active_complaint_types_for_given_category() {
        final long categoryId = 4L;
        final List<ServiceType> complaintTypes = repository.findActiveServiceTypes(categoryId, "tenantId");

        assertNotNull(complaintTypes);
        assertEquals(1, complaintTypes.size());
    }

}