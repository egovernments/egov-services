package org.egov.pgr.persistence.repository;

import org.egov.pgr.domain.model.Complaint;
import org.egov.pgr.domain.model.ComplaintSearchCriteria;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ComplaintRepositoryTest {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ComplaintJpaRepository complaintJpaRepository;

    @Test
    @Sql(scripts = {"/sql/addLastAccessedTime.sql",
            "/sql/clearComplaintStatusMapping.sql",
            "/sql/InsertComplaintStatusMapping.sql",
            "/sql/clearComplaint.sql",
            "/sql/InsertComplaintData.sql"})
    public void testShouldFindAllComplaints() {

        DateTime date = new DateTime();
        date = new DateTime(2016, 12, 19, 0, 0, 0, 0);

        DateTime date2 = new DateTime();
        date2 = new DateTime(2016, 12, 31, 0, 0, 0, 0);
        DateTime date3 = new DateTime();
        date3 = new DateTime(2016, 12, 22, 0, 0, 0, 0);
        DateTime date4 = new DateTime();
        date4 = new DateTime(2016, 12, 20, 0, 0, 0, 0);

        ComplaintSearchCriteria complaintSearchCriteria = ComplaintSearchCriteria.builder()
                .serviceRequestId("0005-2017-AB")
                .assignmentId(1L)
                .serviceCode("AODTDGC")
                .status("REGISTERED")
                .startDate(date.toDate())
                .endDate(date2.toDate())
                .lastModifiedDatetime(date4.toDate())
                .userId(2L)
                .name("kumar")
                .mobileNumber("7475844747")
                .emailId("abc@gmail.com")
                .receivingMode(5L)
                .locationId(1L)
                .childLocationId(null)
                .build();

        List<Complaint> complaints = complaintRepository.findAll(complaintSearchCriteria);

        assertThat(complaints.get(0).getCrn()).isEqualTo("0005-2017-AB");
        assertThat(complaints.get(0).getComplainant()).isNotNull();
        assertThat(complaints.get(0).getComplainant().getFirstName()).isEqualTo("kumar");
        assertThat(complaints.get(0).getComplainant().getMobile()).isEqualTo("7475844747");
        assertThat(complaints.get(0).getComplainant().getEmail()).isEqualTo("abc@gmail.com");
        assertThat(complaints.get(0).getComplainant().getAddress()).isEqualTo("Near Temple");
        assertThat(complaints.get(0).getComplaintLocation().getLocationId()).isEqualTo("1");
        assertThat(complaints.get(0).getComplaintLocation().getCoordinates().getLatitude()).isEqualTo(0);
        assertThat(complaints.get(0).getComplaintLocation().getCoordinates().getLongitude()).isEqualTo(0);
        assertThat(complaints.get(0).getComplaintType().getName())
                .isEqualTo("Absenteesim of door to door garbage collector");
        assertThat(complaints.get(0).getDescription()).isEqualTo("This is a huge problem");
        assertThat(complaints.get(0).getDepartment()).isEqualTo("18");
    }


	/*@Test
	public void test_should_fetch_all_complaints_by_userid() {
		final org.egov.pgr.persistence.entity.Complaint expectedComplaint = getComplaint();
	//	when(complaintJpaRepository.findByCreatedByOrderByLastAccessedTimeDesc(any(Long.class))).thenReturn
	(Collections.singletonList(expectedComplaint));
		final List<Complaint> actualComplaints = complaintRepository.getAllComplaintsForGivenUser(1L);
		assertEquals(1, actualComplaints.size());
		assertEquals(expectedComplaint, actualComplaints.get(0));
	}
	
	@Test
	public void test_should_fetch_all_modified_complaints_for_citizen() {
		final org.egov.pgr.persistence.entity.Complaint expectedComplaint = getComplaint();
		//when(complaintJpaRepository.getAllModifiedComplaintsForCitizen(any(Date.class),any(Long.class))).thenReturn
		(Collections.singletonList(expectedComplaint));
		final List<Complaint> actualComplaints = complaintRepository.getAllModifiedComplaintsForCitizen(new Date(),1L);
		assertEquals(1, actualComplaints.size());
		assertEquals(expectedComplaint, actualComplaints.get(0));
	}
	
	private org.egov.pgr.persistence.entity.Complaint getComplaint() {
		return org.egov.pgr.persistence.entity.Complaint.builder().id(1L).details("details").crn("crn")
				.lastAccessedTime(new Date())
				.department(2L).build();
	}*/

}
