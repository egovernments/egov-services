package org.egov.workflow.persistence.repository;

import org.egov.workflow.persistence.entity.ComplaintStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.egov.workflow.TestConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfiguration.class)
public class ComplaintStatusJpaRepositoryTest {

    @Autowired
    ComplaintStatusJpaRepository complaintStatusJpaRepository;

    @Test
    @Sql(scripts = {"/sql/clearComplaintStatusMapping.sql","/sql/clearComplaintStatus.sql", "/sql/insertComplaintStatus.sql"})
    public void test_should_retrieve_all_complaint_statuses() {
        List<ComplaintStatus> all = complaintStatusJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(9);
    }

    @Test
    @Sql(scripts = {"/sql/clearComplaintStatusMapping.sql","/sql/clearComplaintStatus.sql", "/sql/insertComplaintStatus.sql"})
    public void test_should_find_by_name() {
        ComplaintStatus result = complaintStatusJpaRepository.findByName("REJECTED");
        assertThat(result.getId()).isEqualTo(5);
    }
}