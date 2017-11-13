package org.egov.lams.services.service.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.lams.common.web.contract.AssetCategory;
import org.egov.lams.common.web.contract.AuditDetails;
import org.egov.lams.common.web.contract.Boundary;
import org.egov.lams.common.web.contract.CompensationMode;
import org.egov.lams.common.web.contract.DocumentType;
import org.egov.lams.common.web.contract.DocumentType.ApplicationEnum;
import org.egov.lams.common.web.contract.HoldingType;
import org.egov.lams.common.web.contract.LandDocs;
import org.egov.lams.common.web.contract.LandRegister;
import org.egov.lams.common.web.contract.LandRegister.AcquisitionTypeEnum;
import org.egov.lams.common.web.contract.ModeOfAcquisition;
import org.egov.lams.common.web.contract.PlaningType;
import org.egov.lams.common.web.contract.Purpose;
import org.egov.lams.common.web.contract.RegisterName;
import org.egov.lams.common.web.contract.RoadType;
import org.egov.lams.common.web.contract.SubRegisterName;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class LandRegisterRowMapper implements ResultSetExtractor<List<LandRegister>> {

	@Override
	public List<LandRegister> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<LandRegister> landRegisters = new ArrayList<>();
		while (rs.next()) {
			LandRegister landRegister = new LandRegister();
			List<LandDocs> documents = new ArrayList<>();
			DocumentType documentType = new DocumentType();
			AssetCategory assetCategory = new AssetCategory();
			AuditDetails auditDetails = AuditDetails.builder().createdBy(rs.getString("docs_createdby"))
					.createdTime(rs.getLong("docs_createdtime")).lastModifiedBy(rs.getString("docs_lastmodifiedby"))
					.lastModifiedTime(rs.getLong("docs_lastmodifiedtime")).build();
			try {
				LandDocs landDocs = new LandDocs();
				landDocs.setId(rs.getString("docs_id"));
				landDocs.setFileStore(rs.getString("docs_filestore"));

				documentType.setApplication(ApplicationEnum.fromValue(rs.getString("docs_documenttype")));
				landDocs.setDocumentType(documentType);

				documents.add(landDocs);

				ModeOfAcquisition modeOfAcquisition = new ModeOfAcquisition();
				modeOfAcquisition.setCode(rs.getString("land_acquisitiontype"));

				CompensationMode compensationMode = new CompensationMode();
				compensationMode.setCode(rs.getString("land_modeofcompensation"));

				Purpose purpose = new Purpose();
				purpose.setCode(rs.getString("land_purpose"));

				RegisterName registerName = new RegisterName();
				registerName.setCode(rs.getString("land_registername"));

				RoadType roadType = new RoadType();
				roadType.setCode(rs.getString("land_roadtype"));

				SubRegisterName subRegisterName = new SubRegisterName();
				subRegisterName.setCode(rs.getString("land_subregistername"));

				HoldingType typeOfHolding = new HoldingType();
				typeOfHolding.setCode(rs.getString("land_typeofholding"));

				PlaningType typeOfPlanning = new PlaningType();
				typeOfPlanning.setCode(rs.getString("land_typeofplanning"));

				Boundary regionalOfficeBoundary = new Boundary();
				regionalOfficeBoundary.setId(rs.getString("land_regionaloffice"));

				Boundary locationBoundary = new Boundary();
				locationBoundary.setId(rs.getString("land_location"));

				landRegister.setId(rs.getString("land_id"));
				landRegister.setTenantId(rs.getString("land_tenantid"));
				landRegister.setAcquisitionNo(rs.getString("land_acquisitionno"));
				landRegister.setAcquisitionType(AcquisitionTypeEnum.fromValue(rs.getString("land_acquisitiontype")));
				landRegister.setAreaAsPerRegister(rs.getDouble("land_areaasperregister"));
				landRegister.setBuildingReference(rs.getString("land_buildingreference"));
				landRegister.setCodeOfReservation(rs.getString("land_codeofreservation"));
				landRegister.setComments(rs.getString("land_comments"));
				landRegister.setCostOfAcquisition(rs.getDouble("land_costofacquisition"));
				landRegister.setDateOfPayment(rs.getLong("land_dateofpayment"));
				landRegister.setDocuments(documents);
				landRegister.setGattNo(rs.getString("land_gattno"));
				landRegister.setLandArea(rs.getDouble("land_landarea"));
				landRegister.setLandNumber(rs.getString("land_landnumber"));
				landRegister.setLandType(assetCategory);
				landRegister.setLocation(locationBoundary);
				landRegister.setModeOfAcquisition(modeOfAcquisition);
				landRegister.setModeOfCompensation(compensationMode);
				landRegister.setOldOwnerName(rs.getString("land_oldownername"));
				landRegister.setPossessionDate(rs.getLong("land_possessiondate"));
				landRegister.setPurpose(purpose);
				landRegister.setRegionalOffice(regionalOfficeBoundary);
				landRegister.setReservationArea(rs.getDouble("land_reservationarea"));
				landRegister.setRoadType(roadType);
				landRegister.setStateId(rs.getString("land_stateid"));
				landRegister.setSurveyNo(rs.getString("land_surveyno"));
				landRegister.setTypeOfHolding(typeOfHolding);
				landRegister.setTypeOfPlanning(typeOfPlanning);
				landRegister.setWidth(rs.getDouble("land_width"));
				landRegister.setAuditDetails(auditDetails);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			landRegisters.add(landRegister);
		}
		return landRegisters;
	}
}
