package org.egov.mr.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.contract.ProcessInstance;
import org.egov.mr.contract.TaskResponse;
import org.egov.mr.model.ApprovalDetails;
import org.egov.mr.model.AuditDetails;
import org.egov.mr.model.Demand;
import org.egov.mr.model.Location;
import org.egov.mr.model.MarriageCertificate;
import org.egov.mr.model.MarriageDocument;
import org.egov.mr.model.MarriageRegn;
import org.egov.mr.model.MarryingPerson;
import org.egov.mr.model.PriestInfo;
import org.egov.mr.model.RegistrationUnit;
import org.egov.mr.model.Witness;
import org.egov.mr.model.enums.Action;
import org.egov.mr.model.enums.ApplicationStatus;
import org.egov.mr.model.enums.CertificateType;
import org.egov.mr.model.enums.MaritalStatus;
import org.egov.mr.model.enums.RelatedTo;
import org.egov.mr.model.enums.Source;
import org.egov.mr.model.enums.Venue;
import org.egov.mr.repository.WorkflowRepository;
import org.egov.mr.web.contract.MarriageRegnRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MRWorkflowServiceTest {

	@Mock
	private WorkflowRepository workflowRepository;

	@InjectMocks
	private MRWorkflowService mrWorkflowService;

	@Test
	public void testShouldStartWorkflow() {

		when(workflowRepository.startWorkflow(Matchers.any(MarriageRegnRequest.class)))
				.thenReturn(getProcessInstance());
		mrWorkflowService.startWorkflow(MarriageRegnRequest.builder().marriageRegn(getMarriageRegn().get(0))
				.requestInfo(new RequestInfo()).build());
	}
	
	@Test
	public void testShouldUpdateWorkflow(){
		
		when(workflowRepository.updateWorkflow(Matchers.any(MarriageRegnRequest.class)))
		.thenReturn(new TaskResponse());
		mrWorkflowService.updateWorkflow(MarriageRegnRequest.builder().marriageRegn(getMarriageRegn().get(0))
				.requestInfo(new RequestInfo()).build());
		
	}

	private ProcessInstance getProcessInstance() {
		ProcessInstance instance = new ProcessInstance();
		instance.setBusinessKey("MarriageRegn");
		instance.setType("MRRegn");
		instance.setInitiatorPosition(12l);
		return instance;
	}

	public List<MarriageRegn> getMarriageRegn() {
		AuditDetails auditDetails = AuditDetails.builder().createdBy("asish").createdTime(123l).lastModifiedBy("asish")
				.lastModifiedTime(123l).build();

		MarriageCertificate marriageCertificate = MarriageCertificate.builder().applicationNumber("applnno")
				.bridePhoto("photo").bridegroomPhoto("photo").certificateDate(1496430744825L).certificateNo("certno")
				.certificatePlace("certplace").certificateType(CertificateType.REISSUE).husbandAddress("hus address")
				.husbandName("husname").wifeName("wife name").wifeAddress("wife address").marriageDate(1496430744825L)
				.marriageVenueAddress("venu address").regnDate(1496430744825L).regnNumber("regnnumber")
				.regnSerialNo("serialno").regnVolumeNo("volumeno").templateVersion("v2").tenantId("ap.kurnool").build();
		List<MarriageCertificate> marriageCertificates = new ArrayList<>();
		marriageCertificates.add(marriageCertificate);

		MarriageDocument marriageDocument = MarriageDocument.builder().location("location").id("1")
				.auditDetails(auditDetails).documentType("documenttype").reissueCertificateId("1234")
				.tenantId("ap.kurnool").build();
		List<MarriageDocument> documentsList = new ArrayList<>();
		documentsList.add(marriageDocument);

		ApprovalDetails approvalDetails = ApprovalDetails.builder().action("action").assignee(1496430744825L)
				.comments("comments").department(1496430744825L).designation(1496430744825L).status("Approved").build();

		Witness witness = Witness.builder().name("name").occupation("doctor").dob(50l).aadhaar("2134567896")
				.email("name@gmail.com").address("full address").mobileNo("7654331890").relationshipWithApplicants("uncle")
				.relatedTo(RelatedTo.BRIDEGROOM).relationForIdentification("uncle").witnessNo(1).build();
		List<Witness> witnesses = new ArrayList<>();
		witnesses.add(witness);
		List<Demand> demands = new ArrayList<>();
		/*Demand demand = Demand.builder().tenantId("default").build();
		demands.add(demand);*/

		List<String> actions = new ArrayList<>();
		actions.add("action");

		PriestInfo priest = PriestInfo.builder().aadhaar("32425365675").address("addreee").email("email")
				.mobileNo("459876799").religion(0l).name("string").build();

		RegistrationUnit regnunit = RegistrationUnit.builder().id(33L).code("AP").name("kurnool")
				.address(Location.builder().locality(60L).zone(147896325L).revenueWard(150L).block(14788L)
						.street(159632478L).electionWard(123456789L).doorNo("132").pinCode(560103).build())
				.isActive(true).tenantId("ap.kurnool").auditDetails(auditDetails).build();

		MarryingPerson bridegroom = MarryingPerson.builder().aadhaar("23546576878").city("bangalore").dob(121234l)
				.education("BE").email("email").id(10l).mobileNo("9246745333").name("bridegroom name1")
				.locality("locality").nationality("INDIAN").parentName("parentname").photo("photo")
				.status(MaritalStatus.UNMARRIED).occupation("engineer").street("street").religion(1l)
				.religionPractice("hindu").residenceAddress("rewsidential address").handicapped("no").build();

		MarryingPerson bride = MarryingPerson.builder().aadhaar("23546576878").city("bangalore").dob(121234l)
				.occupation("doctor").education("mbbs").email("email").id(6l).mobileNo("9246773333").name("bride name")
				.nationality("INDIAN").parentName("parentname").photo("photo").status(MaritalStatus.UNMARRIED)
				.street("street").religion(1l).locality("locality").religionPractice("hindu")
				.residenceAddress("rewsidential address").handicapped("no").build();

		MarriageRegn marriageRegn = MarriageRegn.builder().applicationNumber("1").approvalDetails(approvalDetails)
				.auditDetails(auditDetails).bride(bride).bridegroom(bridegroom).certificates(marriageCertificates)
				.city("bangalore").demands(demands).documents(documentsList).isActive(false).locality("locality")
				.actions(Action.CREATE).marriageDate(214335l).marriagePhoto("photo").placeOfMarriage("RESIDENCE")
				.placeOfMarriage("bangalore").priest(priest).street("street").regnDate(121231l).regnNumber("regnno")
				.regnUnit(regnunit).rejectionReason("inappropriate document").remarks("remarks").serialNo("serino")
				.source(Source.SYSTEM).tenantId("ap.kurnool").status(ApplicationStatus.APPROVED).stateId("stateid")
				.venue(Venue.FUNCTION_HALL).volumeNo("volno").witnesses(witnesses).remarks("remarks").build();
		List<MarriageRegn> marriageRegns = new ArrayList<>();
		marriageRegns.add(marriageRegn);

		return marriageRegns;

	}

}
