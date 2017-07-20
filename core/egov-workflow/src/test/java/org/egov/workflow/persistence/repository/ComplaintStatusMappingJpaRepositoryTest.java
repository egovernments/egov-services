package org.egov.workflow.persistence.repository;

import org.egov.workflow.TestConfiguration;
import org.egov.workflow.persistence.entity.ComplaintStatusMapping;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfiguration.class)
public class ComplaintStatusMappingJpaRepositoryTest {

    @Autowired
    ComplaintStatusMappingJpaRepository complaintStatusMappingJpaRepository;

    @Test
    @Sql(scripts = {
            "/sql/clearComplaintStatusMapping.sql",
            "/sql/clearComplaintStatus.sql",
            "/sql/insertComplaintStatus.sql",
            "/sql/insertComplaintStatusMapping.sql"})
    public void test_should_find_next_status_given_current_status_and_roles_ordered_by_order_no_ascending() {
        List<ComplaintStatusMapping> result = complaintStatusMappingJpaRepository.
            findByCurrentStatusIdAndRoleInAndTenantIdOrderByOrderNoAsc(1L, Arrays.asList(1L, 7L),"default");

        assertThat(result.size()).isEqualTo(6);
        assertThat(result.get(0).getId()).isEqualTo(16);
        assertThat(result.get(1).getId()).isEqualTo(61);
        assertThat(result.get(2).getId()).isEqualTo(17);
        assertThat(result.get(3).getId()).isEqualTo(18);
        assertThat(result.get(4).getId()).isEqualTo(19);
        assertThat(result.get(5).getId()).isEqualTo(20);
    }
}