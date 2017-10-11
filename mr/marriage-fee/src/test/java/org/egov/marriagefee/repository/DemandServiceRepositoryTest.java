package org.egov.marriagefee.repository;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.net.URI;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.AssertTrue;

import org.apache.commons.lang3.StringUtils;
import org.egov.marriagefee.config.PropertiesManager;
import org.egov.marriagefee.model.ApprovalDetails;
import org.egov.marriagefee.model.AuditDetails;
import org.egov.marriagefee.model.Demand;
import org.egov.marriagefee.model.DemandDetail;
import org.egov.marriagefee.model.Fee;
import org.egov.marriagefee.model.Location;
import org.egov.marriagefee.model.MarriageCertificate;
import org.egov.marriagefee.model.MarriageDocument;
import org.egov.marriagefee.model.MarriageRegn;
import org.egov.marriagefee.model.MarryingPerson;
import org.egov.marriagefee.model.Owner;
import org.egov.marriagefee.model.PriestInfo;
import org.egov.marriagefee.model.RegistrationUnit;
import org.egov.marriagefee.model.Witness;
import org.egov.marriagefee.model.enums.Action;
import org.egov.marriagefee.model.enums.ApplicationStatus;
import org.egov.marriagefee.model.enums.CertificateType;
import org.egov.marriagefee.model.enums.MaritalStatus;
import org.egov.marriagefee.model.enums.RelatedTo;
import org.egov.marriagefee.model.enums.Source;
import org.egov.marriagefee.model.enums.Venue;
import org.egov.marriagefee.web.contract.DemandRequest;
import org.egov.marriagefee.web.contract.DemandResponse;
import org.egov.marriagefee.web.contract.MarriageRegnRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;


@RunWith(MockitoJUnitRunner.class)
public class DemandServiceRepositoryTest {
	
	@Mock
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private PropertiesManager propertiesManager;
	
	@InjectMocks
	private DemandServiceRepository demandServiceRepository;
	
	

	@Test
	public void testCreateDemand() {
		MarriageRegnRequest marriageRegnRequest=MarriageRegnRequest.builder().marriageRegn(getMarriageRegnFromDB().get(0)).build();
		DemandResponse demandResponse= new DemandResponse();
		demandResponse.setDemands(getDemand());
		DemandRequest demandRequest= new DemandRequest();
		when(propertiesManager.getBillingServiceHostName()).thenReturn(StringUtils.EMPTY);
		when(propertiesManager.getDemandBusinessService()).thenReturn(StringUtils.EMPTY);
		when(restTemplate.postForObject(any(URI.class), any(DemandRequest.class),any(Class.class))).thenReturn(demandResponse);
		
		
	}
	
	
	private List<Demand> getDemand(){
		List<DemandDetail> demandDetails=new ArrayList<>();
	    Demand demand= new Demand();
	    DemandDetail demandDetail= new DemandDetail();
	    demandDetail.setDemandId("1");
	    demandDetail.setTaxAmount(new BigDecimal(1.9));
	    demandDetail.setCollectionAmount(new BigDecimal(1));
	    demandDetail.setTaxHeadMasterCode("MR_FEE");
	    demandDetails.add(demandDetail);
		AuditDetails auditDetails = AuditDetails.builder().createdBy("asish").createdTime(1503056473884l)
				.lastModifiedBy("asish").lastModifiedTime(1503056473884l).build();
		demand.setBusinessService(propertiesManager.getDemandBusinessService());
		demand.setConsumerCode("MR001");
		demand.setConsumerType("consumertype");
		demand.setId("1");
		demand.setAuditDetail(auditDetails);
		demand.setDemandDetails(demandDetails);
		demand.setMinimumAmountPayable(new BigDecimal(2));
		Owner owner=Owner.builder().id(85l).name("sankar").userName("9884187166").mobileNumber("9884187166").build();
		demand.setOwner(owner);
		demand.setTaxPeriodFrom(1427826600l);
		demand.setTaxPeriodTo(1443637799l);
		List<Demand> demands=new ArrayList<>();
		demands.add(demand);
		return demands;
		
		
	
	}
	
	public List<MarriageRegn> getMarriageRegnFromDB() {
		AuditDetails auditDetails = AuditDetails.builder().createdBy("asish").createdTime(1503056473884l)
				.lastModifiedBy("asish").lastModifiedTime(1503056473884l).build();

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

		MarriageRegn marriageRegn = MarriageRegn.builder().applicationNumber("8").approvalDetails(approvalDetails)
				.auditDetails(auditDetails).bride(bride).bridegroom(bridegroom).certificates(marriageCertificates)
				.city("bangalore").demands(demands).documents(documentsList).fee(Fee.builder().fee(new BigDecimal(50)).build()).isActive(true)
				.locality("locality").actions(Action.CREATE).marriageDate(214335l).marriagePhoto("photo")
				.placeOfMarriage("RESIDENCE").placeOfMarriage("bangalore").priest(priest).street("street")
				.regnDate(121231l).regnNumber("regnno").regnUnit(regnunit).rejectionReason("inappropriate document")
				.remarks("remarks").serialNo("serino").source(Source.SYSTEM).tenantId("ap.kurnool")
				.status(ApplicationStatus.APPROVED).stateId("stateid").venue(Venue.FUNCTION_HALL).volumeNo("volno")
				.witnesses(witnesses).remarks("remarks").build();
		List<MarriageRegn> marriageRegns = new ArrayList<>();
		marriageRegns.add(marriageRegn);
		return marriageRegns;

	}

}
