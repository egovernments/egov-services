package org.egov.mr.repository.rowmapper;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.egov.mr.model.ApprovalDetails;
import org.egov.mr.model.AuditDetails;
import org.egov.mr.model.Demand;
import org.egov.mr.model.DemandDetail;
import org.egov.mr.model.Fee;
import org.egov.mr.model.Location;
import org.egov.mr.model.MarriageCertificate;
import org.egov.mr.model.MarriageRegn;
import org.egov.mr.model.MarryingPerson;
import org.egov.mr.model.Owner;
import org.egov.mr.model.PriestInfo;
import org.egov.mr.model.RegistrationUnit;
import org.egov.mr.model.Witness;
import org.egov.mr.model.enums.ApplicationStatus;
import org.egov.mr.model.enums.CertificateType;
import org.egov.mr.model.enums.MaritalStatus;
import org.egov.mr.model.enums.RelatedTo;
import org.egov.mr.model.enums.Source;
import org.egov.mr.model.enums.Venue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarriageRegnRowMapperTest {

	@Mock
	ResultSet rs;

	@InjectMocks
	private MarriageRegnRowMapper marriageRegnRowMapper;

	@Test
	public void test() throws Exception {
		when(rs.next()).thenReturn(true).thenReturn(false);

		when(rs.getObject("mr_applicationNumber")).thenReturn("00015");
		when(rs.getLong("mr_marriagedate")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getString("mr_venue")).thenReturn(Venue.FUNCTION_HALL.toString());
		when(rs.getString("mr_street")).thenReturn("Belandur");
		when(rs.getString("mr_locality")).thenReturn("Belandur");
		when(rs.getString("mr_placeofmarriage")).thenReturn(Venue.FUNCTION_HALL.toString());
		when(rs.getString("mr_city")).thenReturn("Bangalore");
		when(rs.getString("mr_marriagePhoto")).thenReturn("NoPhoto");
		when(rs.getObject("mr_fee")).thenReturn(getFee());
		when(rs.getString("mr_serialNo")).thenReturn("00015");
		when(rs.getString("mr_volumeNo")).thenReturn("2");
		when(rs.getString("mr_applicationNumber")).thenReturn("00015");
		when(rs.getString("mr_status")).thenReturn("ACTIVE");
		when(rs.getString("mr_source")).thenReturn("source");
		when(rs.getString("mr_stateId")).thenReturn("XSD123");
		when(rs.getBoolean("mr_isactive")).thenReturn(true);
		when(rs.getString("mr_tenantid")).thenReturn("ap.kurnool");

		when(rs.getString("mr_createdby")).thenReturn("6");
		when(rs.getLong("mr_createdtime")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getString("mr_lastmodifiedby")).thenReturn("6");
		when(rs.getLong("mr_lastmodifiedtime")).thenReturn(Long.valueOf("9874563210"));

		when(rs.getLong("ru_block")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getString("ru_doorno")).thenReturn("10");
		when(rs.getLong("ru_electionward")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getLong("ru_locality")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getInt("ru_pincode")).thenReturn(Integer.valueOf(98745621));
		when(rs.getLong("ru_revenueward")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getLong("ru_street")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getLong("ru_zone")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getLong("ru_id")).thenReturn(Long.valueOf("2"));
		when(rs.getBoolean("ru_isactive")).thenReturn(true);
		when(rs.getString("ru_name")).thenReturn("Bangalore");
		when(rs.getString("ru_tenantid")).thenReturn("ap.kurnool");
		when(rs.getString("ru_code")).thenReturn("00015");

		/**
		 * @bridegroom
		 */
		when(rs.getLong("mpbg_id")).thenReturn(Long.valueOf("6"));
		when(rs.getString("mpbg_name")).thenReturn("brideGroomName");
		when(rs.getString("mpbg_parentname")).thenReturn("bgParentName");
		when(rs.getString("mpbg_street")).thenReturn("belandur");
		when(rs.getString("mpbg_locality")).thenReturn("belandur");
		when(rs.getString("mpbg_city")).thenReturn("Bangalore");
		when(rs.getString("mpbg_status")).thenReturn("ACTIVE");
		when(rs.getString("mpbg_aadhaar")).thenReturn("XCV12NBV");
		when(rs.getString("mpbg_mobileno")).thenReturn("9517563268");
		when(rs.getString("mpbg_email")).thenReturn("abc@gmail.com");
		when(rs.getLong("mpbg_religion")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getString("mpbg_religionpractice")).thenReturn("Hindu");
		when(rs.getString("mpbg_education")).thenReturn("BE");
		when(rs.getString("mpbg_occupation")).thenReturn("driver");
		when(rs.getLong("mpbg_dob")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getString("mpbg_handicapped")).thenReturn("NotHandicapped");
		when(rs.getString("mpbg_residenceaddress")).thenReturn("Bangalore");
		when(rs.getString("mpbg_photo")).thenReturn("NoPhoto");
		when(rs.getString("mpbg_nationality")).thenReturn("Indian");

		when(rs.getLong("mpb_id")).thenReturn(Long.valueOf("6"));
		when(rs.getString("mpb_name")).thenReturn("brideName");
		when(rs.getString("mpb_parentname")).thenReturn("bParentName");
		when(rs.getString("mpb_street")).thenReturn("belandur");
		when(rs.getString("mpb_locality")).thenReturn("belandur");
		when(rs.getString("mpb_city")).thenReturn("Bangalore");
		when(rs.getString("mpb_status")).thenReturn("ACTIVE");
		when(rs.getString("mpb_aadhaar")).thenReturn("XCV12NBV");
		when(rs.getString("mpb_mobileno")).thenReturn("9517563268");
		when(rs.getString("mpb_email")).thenReturn("abc@gmail.com");
		when(rs.getLong("mpb_religion")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getString("mpb_religionpractice")).thenReturn("Hindu");
		when(rs.getString("mpb_education")).thenReturn("BE");
		when(rs.getString("mpb_occupation")).thenReturn("driver");
		when(rs.getLong("mpb_dob")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getString("mpb_handicapped")).thenReturn("NotHandicapped");
		when(rs.getString("mpb_residenceaddress")).thenReturn("Bangalore");
		when(rs.getString("mpb_photo")).thenReturn("NoPhoto");
		when(rs.getString("mpb_nationality")).thenReturn("Indian");

		/**
		 * @priest
		 */
		when(rs.getString("mr_priestname")).thenReturn("Acharya");
		when(rs.getLong("mr_priestreligion")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getString("mr_priestaddress")).thenReturn("Belandur");
		when(rs.getString("mr_priestaadhaar")).thenReturn("XCV51NBV");
		when(rs.getString("mr_priestmobileno")).thenReturn("9874563210");
		when(rs.getString("mr_priestemail")).thenReturn("abc@gmail.com");
		/**
		 * @approval
		 */
		when(rs.getString("mr_approvalaction")).thenReturn("APPROVED");
		when(rs.getLong("mr_approvalassignee")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getString("mr_approvalcomments")).thenReturn("NoComments");
		when(rs.getLong("mr_approvaldepartment")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getLong("mr_approvaldesignation")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getString("mr_approvalstatus")).thenReturn("ACTIVE");

		/**
		 * @witnessmap
		 */
		when(rs.getInt("w_witnessno")).thenReturn(Integer.valueOf(98745632));
		when(rs.getString("w_name")).thenReturn("Witnesser");
		when(rs.getString("w_aadhaar")).thenReturn("XCV15NBV");
		when(rs.getString("w_address")).thenReturn("BAngalore");
		when(rs.getInt("w_age")).thenReturn(Integer.valueOf(98745632));
		when(rs.getString("w_occupation")).thenReturn("Witnessing");
		when(rs.getString("w_relation")).thenReturn("Relative");
		when(rs.getString("w_relatedto")).thenReturn(RelatedTo.BRIDEGROOM.toString());
		when(rs.getString("w_relationship")).thenReturn("Brother");

		/**
		 * @certificatemap
		 */
		when(rs.getString("mc_certificateno")).thenReturn("00015");
		when(rs.getLong("mc_certificatedate")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getString("mc_certificatetype")).thenReturn(CertificateType.REGISTRATION.toString());
		when(rs.getString("mc_regnnumber")).thenReturn("00015");
		when(rs.getString("mc_bridegroomphoto")).thenReturn("NoPhoto");
		when(rs.getString("mc_bridephoto")).thenReturn("NoPhoto");
		when(rs.getString("mc_husbandname")).thenReturn("Mural");
		when(rs.getString("mc_husbandaddress")).thenReturn("Bangalore");
		when(rs.getString("mc_wifename")).thenReturn("Sangeet");
		when(rs.getString("mc_wifeaddress")).thenReturn("Bangalore");
		when(rs.getLong("mc_marriagedate")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getString("mc_marriagevenueaddress")).thenReturn("FUNCTION_HALL");
		when(rs.getLong("mc_regndate")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getString("mc_regnserialno")).thenReturn("00015");
		when(rs.getString("mc_regnvolumeno")).thenReturn("2");
		when(rs.getString("mc_certificateplace")).thenReturn("Bangalore");
		when(rs.getString("mc_templateversion")).thenReturn("00015");
		when(rs.getString("mc_applicationnumber")).thenReturn("00015");
		when(rs.getString("mc_tenantid")).thenReturn("ap.kurnool");

		/**
		 * 
		 */
		MarriageRegnRowMapper marriageRegnRowMapper = new MarriageRegnRowMapper();
		List<MarriageRegn> marriageRegns = marriageRegnRowMapper.extractData(rs);
		// assertEquals(marriageRegns.get(0), getMarriageRegn());
		System.err.println(marriageRegns.get(0));
		System.err.println(getMarriageRegn());
	}

	public MarriageRegn getMarriageRegn() {
		MarriageRegn marriageRegn = new MarriageRegn();
		marriageRegn.setMarriageDate(Long.valueOf("9874563210"));
		marriageRegn.setSerialNo("00015");
		marriageRegn.setStreet("Belandur");
		marriageRegn.setPlaceOfMarriage(Venue.FUNCTION_HALL.toString());
		marriageRegn.setLocality("Belandur");
		marriageRegn.setCity("Bangalore");
		marriageRegn.setMarriagePhoto("NoPhoto");
		marriageRegn.setFee(getFee());
		marriageRegn.setBridegroom(getBrideGroomMarryinPerson());
		marriageRegn.setBride(getBrideMarryinPerson());
		marriageRegn.setWitnesses(new ArrayList<>());
		marriageRegn.setPriest(getPriest());
		marriageRegn.setStatus(ApplicationStatus.ACTIVE);
		marriageRegn.setVolumeNo("2");
		marriageRegn.setApplicationNumber("00015");
		marriageRegn.setStateId("XSD123");
		marriageRegn.setCertificates(new ArrayList<>());
		marriageRegn.setDemands(null);
		marriageRegn.setActions(null);
		marriageRegn.setTenantId("ap.kurnool");
		marriageRegn.setRegnUnit(getRegistrationunit());
		marriageRegn.setDocuments(null);
		return marriageRegn;

	}

	private RegistrationUnit getRegistrationunit() {
		RegistrationUnit registrationUnit = new RegistrationUnit();

		Location location = new Location();
		location.setBlock(Long.valueOf("9874563210"));
		location.setDoorNo("10");
		location.setElectionWard(Long.valueOf("9874563210"));
		location.setLocality(Long.valueOf("9874563210"));
		location.setPinCode(98745621);
		location.setRevenueWard(Long.valueOf("9874563210"));
		location.setStreet(Long.valueOf("9874563210"));
		location.setZone(Long.valueOf("9874563210"));

		registrationUnit.setAddress(location);
		registrationUnit.setIsMainRegistrationUnit(true);
		registrationUnit.setId(Long.valueOf("2"));
		registrationUnit.setIsActive(true);
		registrationUnit.setName("Bangalore");
		registrationUnit.setTenantId("ap.kurnool");

		return registrationUnit;
	}

	private Fee getFee() {
		Fee fee = new Fee();
		fee.setId(null);
		fee.setTenantId(null);
		fee.setFeeCriteria(null);
		fee.setFee(BigDecimal.valueOf(50));
		fee.setFromDate(null);
		fee.setToDate(null);
		return fee;
	}

	private List<Witness> getWitnesses() {
		List<Witness> witnesses = new ArrayList<Witness>();
		Witness witness = new Witness();
		witness.setAadhaar("XCV15NBV");
		witness.setAddress("BAngalore");
		witness.setDob(Long.valueOf(98745632));
		witness.setEmail("abc@gmail.com");
		witness.setMobileNo("9874563210");
		witness.setName("Mural");
		witness.setOccupation("Witnessing");
		witness.setRelatedTo(RelatedTo.BRIDEGROOM);
		witness.setRelationForIdentification("Brother");
		witness.setRelationshipWithApplicants("Relative");
		witness.setWitnessNo(Integer.valueOf(98745632));

		witnesses.add(witness);
		return witnesses;
	}

	private List<MarriageCertificate> getMarriageCertificate() {
		MarriageCertificate marriageCertificate = new MarriageCertificate();
		marriageCertificate.setApplicationNumber("00015");
		marriageCertificate.setBridegroomPhoto("NoPhoto");
		marriageCertificate.setBridePhoto("NoPhoto");
		marriageCertificate.setCertificateDate(Long.valueOf("9874563210"));
		marriageCertificate.setCertificateNo("00015");
		marriageCertificate.setCertificatePlace("Bangalore");
		marriageCertificate.setCertificateType(CertificateType.REGISTRATION);
		marriageCertificate.setHusbandAddress("Bangalore");
		marriageCertificate.setHusbandName("Mural");
		marriageCertificate.setMarriageDate(Long.valueOf("9874563210"));
		marriageCertificate.setMarriageVenueAddress("FUNCTION_HALL");
		marriageCertificate.setRegnDate(Long.valueOf("9874563210"));
		marriageCertificate.setRegnNumber("00015");
		marriageCertificate.setRegnSerialNo("00015");
		marriageCertificate.setRegnVolumeNo("2");
		marriageCertificate.setTemplateVersion("00015");
		marriageCertificate.setTenantId("ap.kurnool");
		marriageCertificate.setWifeAddress("Bangalore");
		marriageCertificate.setWifeName("Sangeet");
		List<MarriageCertificate> marriageCertidicates = new ArrayList<>();
		marriageCertidicates.add(marriageCertificate);
		return marriageCertidicates;
	}

	private List<DemandDetail> getDemandDetails() {
		List<DemandDetail> demandDetails = new ArrayList<>();
		DemandDetail demandDetail = new DemandDetail();
		demandDetail.setAuditDetail(getAuditDetails());
		demandDetail.setCollectionAmount(BigDecimal.valueOf(10.1));
		demandDetail.setDemandId("00015");
		demandDetail.setId("2");
		demandDetail.setTaxAmount(BigDecimal.valueOf(10.1));
		demandDetail.setTaxHeadMasterCode("00015");
		demandDetail.setTenantId("ap.kurnool");
		demandDetails.add(demandDetail);
		return demandDetails;
	}

	private List<Demand> getDemands() {
		Demand demand = new Demand();
		demand.setAuditDetail(getAuditDetails());
		demand.setBusinessService("MarriageRegistration");
		demand.setConsumerCode("00015");
		demand.setConsumerType("MR");
		demand.setId("2");
		demand.setDemandDetails(getDemandDetails());
		demand.setMinimumAmountPayable(BigDecimal.valueOf(10.1));
		demand.setOwner(getOwner());
		demand.setTaxPeriodFrom(Long.valueOf("9874563210"));
		demand.setTaxPeriodTo(Long.valueOf("9874563210"));
		demand.setTenantId("ap.kurnool");

		List<Demand> demands = new ArrayList<>();
		demands.add(demand);
		return demands;
	}

	private Owner getOwner() {
		Owner owner = new Owner();
		owner.setAadhaarNumber("XVSB12XJ");
		owner.setEmailId("abc@gmail.com");
		owner.setId(Long.valueOf("2"));
		owner.setMobileNumber("9874563210");
		owner.setName("Abhi");
		owner.setPermanentAddress("Bangalore");
		owner.setTenantId("ap.kurnool");
		owner.setUserName("eGov");
		return owner;
	}

	private AuditDetails getAuditDetails() {
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("2");
		auditDetails.setCreatedTime(Long.valueOf("9874563210"));
		auditDetails.setLastModifiedBy("6");
		auditDetails.setLastModifiedTime(Long.valueOf("9874563210"));
		return auditDetails;
	}

	private ApprovalDetails getApprovalDetails() {
		ApprovalDetails approvalDetails = new ApprovalDetails();
		approvalDetails.setAction("APPROVED");
		approvalDetails.setAssignee(Long.valueOf("9874563210"));
		approvalDetails.setComments("NoComments");
		approvalDetails.setDepartment(Long.valueOf("9874563210"));
		approvalDetails.setDesignation(Long.valueOf("9874563210"));
		approvalDetails.setStatus("ACTIVE");

		return approvalDetails;
	}

	private PriestInfo getPriest() {
		PriestInfo priestInfo = new PriestInfo();
		priestInfo.setAadhaar("XCV51NBV");
		priestInfo.setAddress("Belandur");
		priestInfo.setEmail("abc@gmail.com");
		priestInfo.setMobileNo("9874563210");
		priestInfo.setName("Acharya");
		priestInfo.setReligion(Long.valueOf("9874563210"));
		return priestInfo;
	}

	private MarryingPerson getBrideGroomMarryinPerson() {
		MarryingPerson marryingPerson = new MarryingPerson();
		marryingPerson.setResidenceAddress("Bangalore");
		marryingPerson.setAadhaar("XCV12NBV");
		marryingPerson.setCity("Bangalore");
		marryingPerson.setDob(Long.valueOf("9874563210"));
		marryingPerson.setEducation("BE");
		marryingPerson.setEmail("abc@gmail.com");
		marryingPerson.setHandicapped("NotHandicapped");
		marryingPerson.setId(Long.valueOf("6"));
		marryingPerson.setLocality("belandur");
		marryingPerson.setMobileNo("9517563268");
		marryingPerson.setName("brideGroomName");
		marryingPerson.setNationality("Indian");
		marryingPerson.setOccupation("driver");
		marryingPerson.setParentName("bgParentName");
		marryingPerson.setPhoto("NoPhoto");
		marryingPerson.setReligion(Long.valueOf("9874563210"));
		marryingPerson.setReligionPractice("Hindu");
		marryingPerson.setStreet("belandur");
		return marryingPerson;
	}

	private MarryingPerson getBrideMarryinPerson() {
		MarryingPerson marryingPerson = new MarryingPerson();
		marryingPerson.setResidenceAddress("Bangalore");
		marryingPerson.setAadhaar("XCV12NBV");
		marryingPerson.setCity("Bangalore");
		marryingPerson.setDob(Long.valueOf("9874563210"));
		marryingPerson.setEducation("BE");
		marryingPerson.setEmail("abc@gmail.com");
		marryingPerson.setHandicapped("NotHandicapped");
		marryingPerson.setId(Long.valueOf("6"));
		marryingPerson.setLocality("belandur");
		marryingPerson.setMobileNo("9517563268");
		marryingPerson.setName("brideName");
		marryingPerson.setNationality("Indian");
		marryingPerson.setOccupation("driver");
		marryingPerson.setParentName("bParentName");
		marryingPerson.setPhoto("NoPhoto");
		marryingPerson.setReligion(Long.valueOf("9874563210"));
		marryingPerson.setReligionPractice("Hindu");
		marryingPerson.setStreet("belandur");
		return marryingPerson;
	}

}
