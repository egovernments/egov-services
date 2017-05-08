package org.egov.pgrrest.read.persistence.repository;

import org.apache.commons.lang3.time.DateUtils;
import org.egov.pgrrest.TestConfiguration;
import org.egov.pgrrest.common.entity.Complaint;
import org.egov.pgrrest.read.domain.model.ComplaintSearchCriteria;
import org.egov.pgrrest.read.persistence.specification.SevaSpecification;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(TestConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ComplaintJpaRepositoryTest {

    @Autowired
    private org.egov.pgrrest.common.repository.ComplaintJpaRepository complaintJpaRepository;

    @Test
    @Sql(scripts = {
            "/sql/clearComplaint.sql",
            "/sql/InsertComplaintData.sql"
          })
    public void testShouldFindAllComplaints() throws ParseException {
        DateTime startDate = new DateTime(2014, 12, 21, 0, 0, 0, 0);
        DateTime endDate = new DateTime(2016, 12, 21, 0, 0, 0, 0);
        DateTime lastModifiedDate = new DateTime(2016, 12, 21, 0, 0, 0, 0);
        DateTime escalationDate = new DateTime(2016, 12, 24, 0, 0, 0, 0);
        int count=2; 
       

        ComplaintSearchCriteria complaintSearchCriteria = ComplaintSearchCriteria.builder()
                .status(Arrays.asList("REGISTERED","FORWARDED"))
                .receivingMode(5L)
                .locationId(1L)
                .childLocationId(4L)
                .userId(2L)
                .name("kumar")
                .startDate(startDate.toDate())
                .endDate(endDate.toDate())
                .escalationDate(escalationDate.toDate())
                .assignmentId(1L)
                .tenantId("ap.public")
                .build();

        SevaSpecification specification = new SevaSpecification(complaintSearchCriteria);
        List<Complaint> complaints = complaintJpaRepository.findAll(specification);
        
        assertThat(complaints.size()).isEqualTo(count);
        assertThat(complaints.get(0).getCrn()).isEqualTo("0005-2017-AB");
        assertThat(complaints.get(0).getComplainant()).isNotNull();
        assertThat(complaints.get(0).getComplainant().getName()).isEqualTo("kumar");
        assertThat(complaints.get(0).getComplainant().getMobile()).isEqualTo("7475844747");
        assertThat(complaints.get(0).getComplainant().getEmail()).isEqualTo("abc@gmail.com");
        assertThat(complaints.get(0).getComplainant().getAddress()).isEqualTo("Near School");
        assertThat(complaints.get(0).getLocationId()).isEqualTo("1");
        assertThat(complaints.get(0).getChildLocation()).isEqualTo(4L);
        assertThat(complaints.get(0).getLatitude()).isEqualTo(0);
        assertThat(complaints.get(0).getLongitude()).isEqualTo(0);
        assertThat(complaints.get(0).getCreatedDate()).isBetween(startDate.toDate(), endDate.toDate());
        assertTrue(DateUtils.truncatedEquals(complaints.get(0).getLastModifiedDate(),lastModifiedDate.toDate(),Calendar.SECOND));
        assertThat(complaints.get(0).getEscalationDate()).isBefore(escalationDate.toDate());
        assertThat(complaints.get(0).getComplaintType().getName()).isEqualTo("Absenteesim of door_to_door garbage collector");
        assertThat(complaints.get(0).getComplaintType().getCode()).isEqualTo("AODTDGCC");
        assertThat(complaints.get(0).getLandmarkDetails()).isEqualTo("Near Temple");
        assertThat(complaints.get(0).getReceivingMode().getId()).isEqualTo(5);
        assertThat(complaints.get(0).getReceivingCenter().getId()).isEqualTo(5);
        assertThat(complaints.get(0).getStateId()).isEqualTo(5);
        assertThat(complaints.get(0).getDetails()).isEqualTo("This is a huge problem");
        assertThat(complaints.get(0).getDepartment()).isEqualTo(18L);
        assertThat(complaints.get(0).getAssignee()).isEqualTo(1L);
        assertThat(complaints.get(0).getCreatedBy()).isEqualTo(2L);
        assertThat(complaints.get(0).getCitizenFeedback()).isEqualTo("FIVE");
       
   
        assertThat(complaints.get(1).getCrn()).isEqualTo("0009-2016-MN");
        assertThat(complaints.get(1).getComplainant()).isNotNull();
        assertThat(complaints.get(1).getComplainant().getName()).isEqualTo("kumar");
        assertThat(complaints.get(1).getComplainant().getMobile()).isEqualTo("8923618856");
        assertThat(complaints.get(1).getComplainant().getEmail()).isEqualTo("shyam@gmail.com");
        assertThat(complaints.get(1).getComplainant().getAddress()).isEqualTo("Near School");
        assertThat(complaints.get(1).getLocationId()).isEqualTo("1");
        assertThat(complaints.get(1).getChildLocation()).isEqualTo(4L);
        assertThat(complaints.get(1).getLatitude()).isEqualTo(0);
        assertThat(complaints.get(1).getLongitude()).isEqualTo(0);
        assertThat(complaints.get(1).getCreatedDate()).isBetween(startDate.toDate(), endDate.toDate());
        assertTrue(DateUtils.truncatedEquals(complaints.get(1).getLastModifiedDate(),lastModifiedDate.toDate(),Calendar.SECOND));
        assertThat(complaints.get(1).getEscalationDate()).isBefore(escalationDate.toDate());
        assertThat(complaints.get(1).getComplaintType().getName()).isEqualTo("Absenteesim_of_sweepers");
        assertThat(complaints.get(1).getComplaintType().getCode()).isEqualTo("AOSS");
        assertThat(complaints.get(1).getLandmarkDetails()).isEqualTo("Near Temple");
        assertThat(complaints.get(1).getReceivingMode().getId()).isEqualTo(5);
        assertThat(complaints.get(1).getReceivingCenter().getId()).isEqualTo(9);
        assertThat(complaints.get(1).getStateId()).isEqualTo(5);
        assertThat(complaints.get(1).getDetails()).isEqualTo("This is a huge problem");
        assertThat(complaints.get(1).getDepartment()).isEqualTo(19L);
        assertThat(complaints.get(1).getAssignee()).isEqualTo(1L);
        assertThat(complaints.get(1).getCreatedBy()).isEqualTo(2L);
        assertThat(complaints.get(1).getCitizenFeedback()).isEqualTo("FIVE");

    }
}
