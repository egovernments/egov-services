package org.egov.pgr.read.persistence.repository;

import org.egov.pgr.TestConfiguration;
import org.egov.pgr.read.domain.model.ComplaintSearchCriteria;
import org.egov.pgr.read.persistence.specification.SevaSpecification;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(TestConfiguration.class)
public class ComplaintJpaRepositoryTest {

    @Autowired
    private ComplaintJpaRepository complaintJpaRepository;

    @Test
    @Sql(scripts = {
            "/sql/addLastAccessedTime.sql",
            "/sql/clearComplaintStatusMapping.sql",
            "/sql/InsertComplaintStatusMapping.sql",
            "/sql/clearComplaint.sql",
            "/sql/InsertComplaintData.sql"})
    public void testShouldFindAllComplaints() {
        DateTime startDate = new DateTime(2016, 12, 19, 0, 0, 0, 0);
        DateTime endDate = new DateTime(2016, 12, 31, 0, 0, 0, 0);
        DateTime lastModifiedDate = new DateTime(2016, 12, 20, 0, 0, 0, 0);

        ComplaintSearchCriteria complaintSearchCriteria = ComplaintSearchCriteria.builder()
                .serviceRequestId("0005-2017-AB")
                .assignmentId(1L)
                .serviceCode("AODTDGC")
                .status("REGISTERED")
                .startDate(startDate.toDate())
                .endDate(endDate.toDate())
                .lastModifiedDatetime(lastModifiedDate.toDate())
                .userId(2L)
                .name("kumar")
                .mobileNumber("7475844747")
                .emailId("abc@gmail.com")
                .receivingMode(5L)
                .locationId(1L)
                .childLocationId(null)
                .build();

        SevaSpecification specification = new SevaSpecification(complaintSearchCriteria);
        List<org.egov.pgr.read.persistence.entity.Complaint> complaints = complaintJpaRepository.findAll(specification);

        assertThat(complaints.get(0).getCrn()).isEqualTo("0005-2017-AB");
        assertThat(complaints.get(0).getComplainant()).isNotNull();
        assertThat(complaints.get(0).getComplainant().getName()).isEqualTo("kumar");
        assertThat(complaints.get(0).getComplainant().getMobile()).isEqualTo("7475844747");
        assertThat(complaints.get(0).getComplainant().getEmail()).isEqualTo("abc@gmail.com");
        assertThat(complaints.get(0).getComplainant().getAddress()).isEqualTo("Near School");
        assertThat(complaints.get(0).getLocationId()).isEqualTo("1");
        assertThat(complaints.get(0).getLatitude()).isEqualTo(0);
        assertThat(complaints.get(0).getLongitude()).isEqualTo(0);
        assertThat(complaints.get(0).getComplaintType().getName())
                .isEqualTo("Absenteesim of door to door garbage collector");
        assertThat(complaints.get(0).getComplaintType().getCode()).isEqualTo("AODTDGC");
        assertThat(complaints.get(0).getLandmarkDetails()).isEqualTo("Near Temple");
        assertThat(complaints.get(0).getReceivingMode().getId()).isEqualTo(5);
        assertThat(complaints.get(0).getReceivingCenter().getId()).isEqualTo(5);
        assertThat(complaints.get(0).getStateId()).isEqualTo(5);
        assertThat(complaints.get(0).getDetails()).isEqualTo("This is a huge problem");
        assertThat(complaints.get(0).getDepartment()).isEqualTo(18L);
        assertThat(complaints.get(0).getAssignee()).isEqualTo(1L);
    }
}
