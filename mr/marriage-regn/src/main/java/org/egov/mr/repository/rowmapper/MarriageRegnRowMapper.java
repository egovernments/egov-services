package org.egov.mr.repository.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import org.egov.mr.model.enums.CertificateType;
import org.egov.mr.model.enums.MaritalStatus;
import org.egov.mr.model.enums.RelatedTo;
import org.egov.mr.model.enums.ApplicationStatus;
import org.egov.mr.model.enums.Source;
import org.egov.mr.model.enums.Venue;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import ch.qos.logback.core.net.SyslogOutputStream;
import lombok.Getter;
import lombok.Setter;

@Component
public class MarriageRegnRowMapper implements ResultSetExtractor<List<MarriageRegn>> {

	@Override
	public List<MarriageRegn> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, MarriageRegnInfo> marriageRegnInfoMap = getMarriageRegnInfoMap(rs);
		List<MarriageRegn> marriageRegnList = getMarriageRegnList(marriageRegnInfoMap);
		return marriageRegnList;
	}

	private Map<String, MarriageRegnInfo> getMarriageRegnInfoMap(ResultSet rs) throws SQLException {
		@SuppressWarnings("unchecked")
		Map<String, MarriageRegnInfo> marriageRegnInfoMap = new LinkedHashMap<String, MarriageRegnInfo>();
		while (rs.next()) {
			String marriageRegnId = (String) rs.getObject("mr_applicationNumber");
			MarriageRegnInfo marriageRegnInfo = marriageRegnInfoMap.get(marriageRegnId);
			RegistrationUnit registrationUnit = new RegistrationUnit();
			Location location = new Location();
			MarryingPerson bridegroom = new MarryingPerson();
			MarryingPerson bride = new MarryingPerson();
			PriestInfo priest = new PriestInfo();
			ApprovalDetails approvalDetails = new ApprovalDetails();
			Demand demand= new Demand();
			AuditDetails auditDetails = new AuditDetails();
			Fee fee = new Fee();
			List<String> demandIds= new ArrayList<>();

			// populate empInfo fields from result set
			if (marriageRegnInfo == null) {
				marriageRegnInfo = new MarriageRegnInfo();
				
				marriageRegnInfo.setId(rs.getString("mr_id"));
				marriageRegnInfo.setMarriageDate(rs.getLong("mr_marriagedate"));
				marriageRegnInfo.setVenue(Venue.fromValue(rs.getString("mr_venue")));
				marriageRegnInfo.setStreet(rs.getString("mr_street"));
				marriageRegnInfo.setLocality(rs.getString("mr_locality"));
				marriageRegnInfo.setPlaceOfMarriage(rs.getString("mr_placeofmarriage"));
				marriageRegnInfo.setCity(rs.getString("mr_city"));
				marriageRegnInfo.setMarriagePhoto(rs.getString("mr_marriagePhoto"));
				marriageRegnInfo.setSerialNo(rs.getString("mr_serialNo"));
				marriageRegnInfo.setVolumeNo(rs.getString("mr_volumeNo"));
				marriageRegnInfo.setRegistrationNumber(rs.getString("mr_regnumber"));
				marriageRegnInfo.setRegistrationDate(rs.getLong("mr_regndate"));
				marriageRegnInfo.setApplicationNumber(rs.getString("mr_applicationNumber"));
				marriageRegnInfo.setStatus(ApplicationStatus.fromValue(rs.getString("mr_status")));
				marriageRegnInfo.setSource(Source.fromValue(rs.getString("mr_source")));
				marriageRegnInfo.setStateId(rs.getString("mr_stateId"));
				marriageRegnInfo.setIsActive(rs.getBoolean("mr_isactive"));
				marriageRegnInfo.setTenantId(rs.getString("mr_tenantid"));
				String demandId = rs.getString("mr_demandid");
				System.err.println("demandId"+demandId);
				demandIds.add(demandId);
				System.err.println("demandIds"+demandIds);
				marriageRegnInfo.setDemandIds(demandIds);
				
				demand.setId(rs.getString("mr_demandid"));
				List<Demand> demands=new ArrayList<>();
				demands.add(demand);
				marriageRegnInfo.setDemands(demands);
				
				auditDetails.setCreatedBy(rs.getString("mr_createdby"));
				auditDetails.setCreatedTime(rs.getLong("mr_createdtime"));
				auditDetails.setLastModifiedBy(rs.getString("mr_lastmodifiedby"));
				auditDetails.setLastModifiedTime(rs.getLong("mr_lastmodifiedtime"));
				marriageRegnInfo.setAuditDetails(auditDetails);

				// fee
				fee.setId(rs.getString("f_id"));
				fee.setTenantId(rs.getString("f_tenantId"));
				fee.setFee(rs.getBigDecimal("f_fee"));
				fee.setFeeCriteria(rs.getString("f_feeCriteria"));
				fee.setFromDate(rs.getLong("f_fromDate"));
				fee.setToDate(rs.getLong("f_toDate"));
				marriageRegnInfo.setFee(fee);

				location.setBlock(rs.getLong("ru_block"));
				location.setDoorNo(rs.getString("ru_doorno"));
				location.setElectionWard(rs.getLong("ru_electionward"));
				location.setLocality(rs.getLong("ru_locality"));
				location.setPinCode(rs.getInt("ru_pincode"));
				location.setRevenueWard(rs.getLong("ru_revenueward"));
				location.setStreet(rs.getLong("ru_street"));
				location.setZone(rs.getLong("ru_zone"));

				registrationUnit.setId(rs.getLong("ru_id"));
				registrationUnit.setIsActive(rs.getBoolean("ru_isactive"));
				registrationUnit.setName(rs.getString("ru_name"));
				registrationUnit.setTenantId(rs.getString("ru_tenantid"));
				registrationUnit.setAddress(location);
				registrationUnit.setIsMainRegistrationUnit(rs.getBoolean("ru_ismainregistrationunit"));
				marriageRegnInfo.setRegnUnit(registrationUnit);

				// bridegroom
				bridegroom.setId(rs.getLong("mpbg_id"));
				bridegroom.setName(rs.getString("mpbg_name"));
				bridegroom.setParentName(rs.getString("mpbg_parentname"));
				bridegroom.setStreet(rs.getString("mpbg_street"));
				bridegroom.setLocality(rs.getString("mpbg_locality"));
				bridegroom.setCity(rs.getString("mpbg_city"));
				bridegroom.setStatus(MaritalStatus.fromValue(rs.getString("mpbg_status")));
				bridegroom.setAadhaar(rs.getString("mpbg_aadhaar"));
				bridegroom.setMobileNo(rs.getString("mpbg_mobileno"));
				bridegroom.setEmail(rs.getString("mpbg_email"));
				bridegroom.setReligion(rs.getLong("mpbg_religion"));
				bridegroom.setReligionPractice(rs.getString("mpbg_religionpractice"));
				bridegroom.setEducation(rs.getString("mpbg_education"));
				bridegroom.setOccupation(rs.getString("mpbg_occupation"));
				bridegroom.setDob(rs.getLong("mpbg_dob"));
				bridegroom.setHandicapped(rs.getString("mpbg_handicapped"));
				bridegroom.setResidenceAddress(rs.getString("mpbg_residenceaddress"));
				bridegroom.setPhoto(rs.getString("mpbg_photo"));
				bridegroom.setNationality(rs.getString("mpbg_nationality"));
				bridegroom.setOfficeAddress(rs.getString("mpbg_officeaddress"));
				marriageRegnInfo.setBridegroom(bridegroom);

				// bride
				bride.setId(rs.getLong("mpb_id"));
				bride.setName(rs.getString("mpb_name"));
				bride.setParentName(rs.getString("mpb_parentname"));
				bride.setStreet(rs.getString("mpb_street"));
				bride.setLocality(rs.getString("mpb_locality"));
				bride.setCity(rs.getString("mpb_city"));
				bride.setStatus(MaritalStatus.fromValue(rs.getString("mpb_status")));
				bride.setAadhaar(rs.getString("mpb_aadhaar"));
				bride.setMobileNo(rs.getString("mpb_mobileno"));
				bride.setEmail(rs.getString("mpb_email"));
				bride.setReligion(rs.getLong("mpb_religion"));
				bride.setReligionPractice(rs.getString("mpb_religionpractice"));
				bride.setEducation(rs.getString("mpb_education"));
				bride.setOccupation(rs.getString("mpb_occupation"));
				bride.setDob(rs.getLong("mpb_dob"));
				bride.setHandicapped(rs.getString("mpb_handicapped"));
				bride.setResidenceAddress(rs.getString("mpb_residenceaddress"));
				bride.setPhoto(rs.getString("mpb_photo"));
				bride.setNationality(rs.getString("mpb_nationality"));
				bride.setOfficeAddress(rs.getString("mpb_officeaddress"));
				marriageRegnInfo.setBride(bride);

				// priest
				priest.setName(rs.getString("mr_priestname"));
				priest.setReligion(rs.getLong("mr_priestreligion"));
				priest.setAddress(rs.getString("mr_priestaddress"));
				priest.setAadhaar(rs.getString("mr_priestaadhaar"));
				priest.setMobileNo(rs.getString("mr_priestmobileno"));
				priest.setEmail(rs.getString("mr_priestemail"));
				marriageRegnInfo.setPriest(priest);

				// approvaldetails
				approvalDetails.setAction(rs.getString("mr_approvalaction"));
				approvalDetails.setAssignee(rs.getLong("mr_approvalassignee"));
				approvalDetails.setComments(rs.getString("mr_approvalcomments"));
				approvalDetails.setDepartment(rs.getLong("mr_approvaldepartment"));
				approvalDetails.setDesignation(rs.getLong("mr_approvaldesignation"));
				approvalDetails.setStatus(rs.getString("mr_approvalstatus"));
				marriageRegnInfo.setApprovalDetails(approvalDetails);
				marriageRegnInfoMap.put(marriageRegnId, marriageRegnInfo);
			}

			Map<String, Witness> witnessMap = marriageRegnInfo.getWitnesses();
			String applicationnumber = (String) rs.getObject("w_aadhaar");

			if (applicationnumber != null) {
				Witness witness = witnessMap.get(applicationnumber);
				if (witness == null) {
					witness = new Witness();

					// witness
					witness.setId(rs.getLong("w_id"));
					witness.setWitnessNo(rs.getInt("w_witnessno"));
					witness.setName(rs.getString("w_name"));
					witness.setAadhaar(rs.getString("w_aadhaar"));
					witness.setAddress(rs.getString("w_address"));
					witness.setDob(rs.getLong("w_dob"));
					witness.setOccupation(rs.getString("w_occupation"));
					witness.setRelationForIdentification(rs.getString("w_relation"));
					witness.setRelatedTo(RelatedTo.fromValue(rs.getString("w_relatedto")));
					witness.setRelationshipWithApplicants(rs.getString("w_relationshipwithapplicants"));
					witness.setPhoto(rs.getString("w_photo"));
					witnessMap.put(applicationnumber, witness);
				}
			}

			Map<String, MarriageCertificate> certificateMap = marriageRegnInfo.getCertificates();
			String certificateNo = (String) rs.getObject("mc_certificateno");

			if (certificateNo != null) {
				MarriageCertificate marriageCertificate = certificateMap.get(certificateNo);
				if (marriageCertificate == null) {
					marriageCertificate = new MarriageCertificate();

					marriageCertificate.setCertificateNo(rs.getString("mc_certificateno"));
					marriageCertificate.setCertificateDate(rs.getLong("mc_certificatedate"));
					marriageCertificate
							.setCertificateType(CertificateType.fromValue(rs.getString("mc_certificatetype")));
					marriageCertificate.setRegnNumber(rs.getString("mc_regnnumber"));
					marriageCertificate.setBridegroomPhoto(rs.getString("mc_bridegroomphoto"));
					marriageCertificate.setBridePhoto(rs.getString("mc_bridephoto"));
					marriageCertificate.setHusbandName(rs.getString("mc_husbandname"));
					marriageCertificate.setHusbandAddress(rs.getString("mc_husbandaddress"));
					marriageCertificate.setWifeName(rs.getString("mc_wifename"));
					marriageCertificate.setWifeAddress(rs.getString("mc_wifeaddress"));
					marriageCertificate.setMarriageDate(rs.getLong("mc_marriagedate"));
					marriageCertificate.setMarriageVenueAddress(rs.getString("mc_marriagevenueaddress"));
					marriageCertificate.setRegnDate(rs.getLong("mc_regndate"));
					marriageCertificate.setRegnSerialNo(rs.getString("mc_regnserialno"));
					marriageCertificate.setRegnVolumeNo(rs.getString("mc_regnvolumeno"));
					marriageCertificate.setCertificatePlace(rs.getString("mc_certificateplace"));
					marriageCertificate.setTemplateVersion(rs.getString("mc_templateversion"));
					marriageCertificate.setApplicationNumber(rs.getString("mc_applicationnumber"));
					marriageCertificate.setTenantId(rs.getString("mc_tenantid"));
					certificateMap.put(certificateNo, marriageCertificate);
				}
			}

		}
		return marriageRegnInfoMap;
	}

	private List<MarriageRegn> getMarriageRegnList(Map<String, MarriageRegnInfo> marriageRegnInfoMap) {
		List<MarriageRegn> marriageRegnList = new ArrayList<MarriageRegn>();
		for (Map.Entry<String, MarriageRegnInfo> marriageRegnInfoEntry : marriageRegnInfoMap.entrySet()) {
			MarriageRegnInfo marriageRegnInfo = marriageRegnInfoEntry.getValue();
			List<Long> demands = new ArrayList<>();

			// build
			MarriageRegn marriageRegn = MarriageRegn.builder().id(marriageRegnInfo.id)
					.regnUnit(marriageRegnInfo.regnUnit).marriageDate(marriageRegnInfo.marriageDate)
					.venue(marriageRegnInfo.venue).street(marriageRegnInfo.street)
					.placeOfMarriage(marriageRegnInfo.placeOfMarriage).locality(marriageRegnInfo.locality)
					.city(marriageRegnInfo.city).marriagePhoto(marriageRegnInfo.marriagePhoto)
					.fee(marriageRegnInfo.fee).bridegroom(marriageRegnInfo.bridegroom).bride(marriageRegnInfo.bride).priest(marriageRegnInfo.priest)
					.documents(marriageRegnInfo.documents).serialNo(marriageRegnInfo.serialNo)
					.volumeNo(marriageRegnInfo.volumeNo).applicationNumber(marriageRegnInfo.applicationNumber)
					.regnNumber(marriageRegnInfo.registrationNumber).regnDate(marriageRegnInfo.registrationDate)
					.status(marriageRegnInfo.status).source(marriageRegnInfo.source).stateId(marriageRegnInfo.stateId)
					.approvalDetails(marriageRegnInfo.approvalDetails).rejectionReason(marriageRegnInfo.rejectionReason)
					.remarks(marriageRegnInfo.remarks).demands(marriageRegnInfo.demands).demandIds(marriageRegnInfo.demandIds)
					.isActive(marriageRegnInfo.isActive).tenantId(marriageRegnInfo.tenantId).build();

			List<Witness> witnessList = new ArrayList<Witness>();
			for (Map.Entry<String, Witness> witnessEntry : marriageRegnInfo.getWitnesses().entrySet()) {
				Witness witness = witnessEntry.getValue();
				witnessList.add(witness);
			}
			marriageRegn.setWitnesses(witnessList);

			List<MarriageCertificate> certificateList = new ArrayList<MarriageCertificate>();
			for (Map.Entry<String, MarriageCertificate> certificateEntry : marriageRegnInfo.getCertificates()
					.entrySet()) {
				MarriageCertificate certificate = certificateEntry.getValue();
				certificateList.add(certificate);
			}
			marriageRegn.setCertificates(certificateList);
			marriageRegnList.add(marriageRegn);
		}
		return marriageRegnList;
	}

	@Getter
	@Setter
	private class MarriageRegnInfo {
		
		private String id;
		
		private RegistrationUnit regnUnit;

		private Long marriageDate;

		private Venue venue;

		private String street;

		private String placeOfMarriage;

		private String locality;

		private String city;

		private String marriagePhoto;

		private Fee fee;

		private MarryingPerson bridegroom;

		private MarryingPerson bride;

		private Map<String, Witness> witnesses = new HashMap<String, Witness>();

		private PriestInfo priest;

		private List<MarriageDocument> documents = new ArrayList<MarriageDocument>();

		private Map<String, MarriageCertificate> certificates = new HashMap<String, MarriageCertificate>();

		private String serialNo;

		private String volumeNo;

		private String applicationNumber;

		private String registrationNumber;
		
		private Long registrationDate;

		private ApplicationStatus status;

		private Source source;

		private String stateId;

		private ApprovalDetails approvalDetails;

		private Boolean isActive;

		private AuditDetails auditDetails;

		private String tenantId;
		
		private String rejectionReason;
		
		private String remarks;
		
		private String actions;
		
		private List<Demand> demands ;
		
		private List<String> demandIds ;
	}

}
