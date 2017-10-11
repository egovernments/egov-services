package org.egov.mr.repository;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.model.ApprovalDetails;
import org.egov.mr.model.AuditDetails;
import org.egov.mr.model.Demand;
import org.egov.mr.model.Fee;
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
import org.egov.mr.repository.querybuilder.MarriageRegnQueryBuilder;
import org.egov.mr.repository.rowmapper.MarriageRegnIdsRowMapper;
import org.egov.mr.repository.rowmapper.MarriageRegnRowMapper;
import org.egov.mr.web.contract.MarriageRegnCriteria;
import org.egov.mr.web.contract.MarriageRegnRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class MarriageRegnRepositoryTest {

	@Mock
	private MarriageRegnQueryBuilder marriageRegnQueryBuilder;

	@Mock
	private MarriageRegnIdsRowMapper marriageRegnIdsRowMapper;

	@Mock
	private MarriageRegnRowMapper marriageRegnRowMapper;

	@Mock
	private WitnessRepository witnessRepository;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@InjectMocks
	private MarriageRegnRepository marriageRegnRepository;

	@Test
	public void testFindForCriteria() {
		List<MarriageRegn> marriageRegns = getMarriageRegnFromDB();

		when(marriageRegnQueryBuilder.getQueryForListOfMarriageRegnIds(any(MarriageRegnCriteria.class),
				any(List.class))).thenReturn(StringUtils.EMPTY);
		when(jdbcTemplate.query(any(String.class), any(Object[].class), any(MarriageRegnIdsRowMapper.class)))
				.thenReturn(getApplicationnumbers());
		when(marriageRegnQueryBuilder.getQuery(any(MarriageRegnCriteria.class), any(List.class), any(List.class)))
				.thenReturn(StringUtils.EMPTY);
		when(jdbcTemplate.query(any(String.class), any(Object[].class), any(MarriageRegnRowMapper.class)))
				.thenReturn(marriageRegns);
		assertTrue(marriageRegns.equals(marriageRegnRepository.findForCriteria(new MarriageRegnCriteria())));
	}

	@Test
	public void testSave() {
		MarriageRegnRequest marriageRegnRequest = new MarriageRegnRequest();
		RequestInfo requestInfo = new RequestInfo();
		List<MarriageRegn> marriageRegns = getMarriageRegnFromDB();
		marriageRegnRequest.setMarriageRegn(marriageRegns.get(0));
		marriageRegnRequest.setRequestInfo(requestInfo);
		when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
		Mockito.doNothing().when(witnessRepository).save(Matchers.anyString(), Matchers.anyString(),
				Matchers.any(Witness.class));
		marriageRegnRepository.save(marriageRegns.get(0));
	}

	@Test
	public void testUpdate() {
		MarriageRegnRequest marriageRegnRequest = new MarriageRegnRequest();
		RequestInfo requestInfo = new RequestInfo();
		List<MarriageRegn> marriageRegns = getMarriageRegnFromDB();
		marriageRegnRequest.setMarriageRegn(marriageRegns.get(0));
		marriageRegnRequest.setRequestInfo(requestInfo);
		when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
	}

	@Test
	public void testUpdateForWitnessNull() {
		MarriageRegnRequest marriageRegnRequest = new MarriageRegnRequest();
		RequestInfo requestInfo = new RequestInfo();
		List<MarriageRegn> marriageRegns = getMarriageRegnFromDB();
		marriageRegns.get(0).setWitnesses(null);
		marriageRegnRequest.setMarriageRegn(marriageRegns.get(0));
		marriageRegnRequest.setRequestInfo(requestInfo);
		when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
		Mockito.doNothing().when(witnessRepository).update(Matchers.anyString(), Matchers.anyString(),
				Matchers.any(Witness.class));
		marriageRegnRepository.update(marriageRegns.get(0));

	}

	@Test
	public void testGenerateApplicationnumbers() {
		when(jdbcTemplate.queryForObject(Matchers.any(String.class), Matchers.<Class<String>>any()))
				.thenReturn("00015");
		String applicationNumber = marriageRegnRepository.generateApplicationNumber();
		assertEquals("00015", applicationNumber);
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

		Demand demand = new Demand();
		demand.setBusinessService("MarriageRegistration");
		List<Demand> demands = new ArrayList<>();
		demands.add(demand);

		List<String> actions = new ArrayList<>();
		actions.add("action");

		PriestInfo priest = PriestInfo.builder().aadhaar("32425365675").address("addreee").email("email")
				.mobileNo("459876799").religion(0l).name("string").build();

		RegistrationUnit regnunit = RegistrationUnit.builder().id(33L).name("kurnool")
				.address(Location.builder().locality(60L).zone(147896325L).revenueWard(150L).block(14788L)
						.street(159632478L).electionWard(123456789L).doorNo("132").pinCode(560103).build())
				.isMainRegistrationUnit(true).isActive(true).tenantId("ap.kurnool").auditDetails(auditDetails).build();

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
				.city("bangalore").demands(demands).documents(documentsList)
				.fee(Fee.builder().fee(new BigDecimal(50)).build()).isActive(true).locality("locality")
				.actions(Action.CREATE).marriageDate(214335l).marriagePhoto("photo").placeOfMarriage("RESIDENCE")
				.placeOfMarriage("bangalore").priest(priest).street("street").regnDate(121231l).regnNumber("regnno")
				.regnUnit(regnunit).rejectionReason("inappropriate document").remarks("remarks").serialNo("serino")
				.source(Source.SYSTEM).tenantId("ap.kurnool").status(ApplicationStatus.APPROVED).stateId("stateid")
				.venue(Venue.FUNCTION_HALL).volumeNo("volno").witnesses(witnesses).remarks("remarks").build();
		List<MarriageRegn> marriageRegns = new ArrayList<>();
		marriageRegns.add(marriageRegn);
		return marriageRegns;

	}

	public List<String> getApplicationnumbers() {

		String applnno = "12345";
		List<String> applnNoList = new ArrayList<>();
		applnNoList.add(applnno);
		return applnNoList;

	}
}
