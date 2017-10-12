package org.egov.mr.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.mr.TestConfiguration;
import org.egov.mr.model.ApprovalDetails;
import org.egov.mr.model.AuditDetails;
import org.egov.mr.model.Demand;
import org.egov.mr.model.Fee;
import org.egov.mr.model.Location;
import org.egov.mr.model.MarriageCertificate;
import org.egov.mr.model.MarriageDocument;
import org.egov.mr.model.MarriageRegn;
import org.egov.mr.model.MarryingPerson;
import org.egov.mr.model.Page;
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
import org.egov.mr.service.MarriageRegnService;
import org.egov.mr.utils.FileUtils;
import org.egov.mr.web.contract.MarriageRegnCriteria;
import org.egov.mr.web.contract.MarriageRegnRequest;
import org.egov.mr.web.contract.MarriageRegnResponse;
import org.egov.mr.web.contract.ResponseInfoFactory;
import org.egov.mr.web.errorhandler.ErrorHandler;
import org.egov.mr.web.validator.MarriageRegnValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

@RunWith(SpringRunner.class)
@WebMvcTest(MarriageRegnController.class)
@Import(TestConfiguration.class)
public class MarriageRegnControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MarriageRegnService marriageRegnService;

	@InjectMocks
	private MarriageRegnController marriageRegnController;

	@MockBean
	private MarriageRegnValidator marriageRegnValidator;

	@MockBean
	private BindingResult modelAttributeBindingResult;

	@MockBean
	private ErrorHandler errorHandler;

	@MockBean
	ResponseInfoFactory responseInfoFactory;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testForCreate() {

		List<MarriageRegn> marriageregn = getMarriageRegnFromDB();
		ResponseInfo responseInfo = ResponseInfo.builder().apiId("string").status("201").ver("string")
				.ts(Long.valueOf("987456321")).resMsgId("string").build();
		
		MarriageRegnResponse response=MarriageRegnResponse.builder().marriageRegns(marriageregn).build();
		when(responseInfoFactory.createResponseInfoFromRequestInfo(Matchers.any(RequestInfo.class),
				Matchers.any(Boolean.class))).thenReturn(responseInfo);
		when(marriageRegnService.createAsync(Matchers.any(MarriageRegnRequest.class))).thenReturn(response);

		try {
			mockMvc.perform(
					post("/marriageRegns/appl/_create").content(getFileContents("MarriageRegnCreateRequest.json"))
							.contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(content().json(getFileContents("MarriageRegnCreateResponse.json")));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testForUpdate() {
		MarriageRegn marriageRegn = getMarriageRegnFromDB().get(0);
		ResponseInfo responseInfo = ResponseInfo.builder().apiId("string").status("200").ver("string")
				.ts(Long.valueOf("987456321")).resMsgId("string").build();
		MarriageRegnResponse response=MarriageRegnResponse.builder().marriageRegns(Arrays.asList(marriageRegn)).build();
		when(responseInfoFactory.createResponseInfoFromRequestInfo(Matchers.any(RequestInfo.class),
				Matchers.any(Boolean.class))).thenReturn(responseInfo);
		when(marriageRegnService.updateAsync(Matchers.any(MarriageRegnRequest.class))).thenReturn(response);

		try {
			mockMvc.perform(
					post("/marriageRegns/appl/_update").content(getFileContents("MarriageRegnCreateRequest.json"))
							.contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(content().json(getFileContents("MarriageRegnCreateResponse.json")));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testForSearch() {

		MarriageRegn marriageRegn = getMarriageRegnFromDB().get(0);
		ResponseInfo responseInfo = ResponseInfo.builder().apiId("string").status("200").ver("string")
				.ts(Long.valueOf("987456321")).resMsgId("string").build();
		when(modelAttributeBindingResult.hasErrors()).thenReturn(false);
		when(errorHandler.getErrorResponse(Matchers.any(), Matchers.any(RequestInfo.class))).thenReturn(null);

		when(responseInfoFactory.createResponseInfoFromRequestInfo(Matchers.any(RequestInfo.class),
				Matchers.any(Boolean.class))).thenReturn(null);
		when(marriageRegnService.getMarriageRegns(Matchers.any(MarriageRegnCriteria.class),
				Matchers.any(RequestInfo.class))).thenReturn(getmarriageregnForSearch());

		try {
			mockMvc.perform(
					post("/marriageRegns/appl/_search").content(getFileContents("MarriageRegnSearchRequest.json"))
							.contentType(MediaType.APPLICATION_JSON_UTF8).param("tenantId", "ap.kurnool"))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(content().json(getFileContents("MarriageRegnSearchResponse.json")));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String getFileContents(String filePath) throws IOException {
		return new FileUtils().getFileContents("org/egov/mr/web/controller/" + filePath);
	}

	/**
	 * @Creating_Object
	 * 
	 * @return
	 */
	public List<MarriageRegn> getMarriageRegnFromDB() {
		AuditDetails auditDetails = AuditDetails.builder().createdBy("asish").createdTime(1503056473884l)
				.lastModifiedBy("asish").lastModifiedTime(1503056473884l).build();
		/**
		 * @RegnUnit_HelperMethod
		 */
		RegistrationUnit regnunit = RegistrationUnit.builder().id(33L).name("kurnool")
				.address(Location.builder().locality(60L).zone(147896325L).revenueWard(150L).block(14788L)
						.street(159632478L).electionWard(123456789L).doorNo("132").pinCode(560103).build())
				.isMainRegistrationUnit(true).isActive(true).tenantId("ap.kurnool").auditDetails(auditDetails).build();

		/**
		 * @Certificates_HelperMethod
		 */
		MarriageCertificate marriageCertificate = MarriageCertificate.builder().applicationNumber("applnno")
				.bridePhoto("photo").bridegroomPhoto("photo").certificateDate(1496430744825L).certificateNo("certno")
				.certificatePlace("certplace").certificateType(CertificateType.REISSUE).husbandAddress("hus address")
				.husbandName("husname").wifeName("wife name").wifeAddress("wife address")
				.marriageDate(Long.valueOf("214335")).marriageVenueAddress("FUNCTION_HALL")
				.regnDate(Long.valueOf("214335")).regnNumber("regnnumber").regnSerialNo("serialno")
				.regnVolumeNo("volumeno").templateVersion("v2").tenantId("ap.kurnool").build();

		List<MarriageCertificate> marriageCertificates = new ArrayList<>();
		marriageCertificates.add(marriageCertificate);

		/**
		 * @Documents_HelperMethod
		 */
		MarriageDocument marriageDocument = MarriageDocument.builder().location("location").id("1")
				.auditDetails(auditDetails).documentType("documenttype").reissueCertificateId("1234")
				.tenantId("ap.kurnool").build();
		List<MarriageDocument> documentsList = new ArrayList<>();
		documentsList.add(marriageDocument);

		ApprovalDetails approvalDetails = ApprovalDetails.builder().action("action").assignee(1496430744825L)
				.comments("comments").department(1496430744825L).designation(1496430744825L).status("Approved").build();

		/**
		 * @witnesses_HelperMethod
		 */
		Witness witness = Witness.builder().name("name").occupation("doctor").dob(50l).aadhaar("2134567896")
				.email("name@gmail.com").address("full address").mobileNo("7654331890").relationshipWithApplicants("uncle")
				.relatedTo(RelatedTo.BRIDEGROOM).relationForIdentification("uncle").witnessNo(1).build();
		List<Witness> witnesses = new ArrayList<>();
		witnesses.add(witness);

		/**
		 * @demands_HelperMethod
		 */
		List<Demand> demands = new ArrayList<>();
		Demand demand = new Demand();
		demand.setId("2");
		demands.add(demand);

		List<String> actions = new ArrayList<>();
		actions.add("action");

		/**
		 * @Fee_HelperMethod
		 */
		Fee fee = new Fee();
		fee.setId("2");
		fee.setTenantId("ap.kurnool");
		fee.setFee(BigDecimal.valueOf(10.5));
		fee.setFeeCriteria("100.1");
		fee.setFromDate(Long.valueOf("9874563210"));
		fee.setToDate(Long.valueOf("9874563210"));

		/**
		 * @Priest_HelperMethod
		 */
		PriestInfo priest = PriestInfo.builder().aadhaar("32425365675").address("addreee").email("email")
				.mobileNo("459876799").religion(0l).name("string").build();

		/**
		 * @MarryingPerson_HelperMethod
		 */
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

		/**
		 * @marriageRegns_OBJECT_integration
		 */
		MarriageRegn marriageRegn = MarriageRegn.builder().regnUnit(regnunit).marriageDate(Long.valueOf("214335"))
				.venue(Venue.FUNCTION_HALL).street("street").placeOfMarriage("RESIDENCE").locality("locality")
				.city("bangalore").marriagePhoto("photo").fee(fee).bridegroom(bridegroom).bride(bride)
				.witnesses(witnesses).priest(priest).documents(documentsList).serialNo("serino").volumeNo("volno")
				.applicationNumber("8").regnNumber("regnno").regnDate(Long.valueOf("121231"))
				.status(ApplicationStatus.APPROVED).source(Source.SYSTEM).stateId("stateid")
				.approvalDetails(approvalDetails).rejectionReason("inappropriate document").remarks("remarks")
				.certificates(marriageCertificates).demands(demands).actions(Action.CREATE).auditDetails(auditDetails)
				.isActive(true).tenantId("ap.kurnool").build();

		List<MarriageRegn> marriageRegns = new ArrayList<>();
		marriageRegns.add(marriageRegn);
		return marriageRegns;

	}

	/**
	 * @getMR_For_Search
	 * 
	 * @return
	 */
	public MarriageRegnResponse getmarriageregnForSearch() {
		List<MarriageRegn> marriageRegnList = getMarriageRegnFromDB();
		Page page = Page.builder().build();
		ResponseInfo responseInfo = ResponseInfo.builder().apiId("string").status("200").ver("string")
				.ts(Long.valueOf("987456321")).resMsgId("string").build();
		MarriageRegnResponse marriageRegnResponse = MarriageRegnResponse.builder().marriageRegns(marriageRegnList)
				.page(page).responseInfo(responseInfo).build();
		return marriageRegnResponse;

	}

}
