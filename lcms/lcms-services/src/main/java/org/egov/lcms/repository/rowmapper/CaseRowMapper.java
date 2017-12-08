package org.egov.lcms.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.enums.EntryType;
import org.egov.lcms.models.Address;
import org.egov.lcms.models.Bench;
import org.egov.lcms.models.Case;
import org.egov.lcms.models.CaseCategory;
import org.egov.lcms.models.CaseStatus;
import org.egov.lcms.models.CaseType;
import org.egov.lcms.models.Court;
import org.egov.lcms.models.Department;
import org.egov.lcms.models.Document;
import org.egov.lcms.models.Register;
import org.egov.lcms.models.Side;
import org.egov.lcms.models.Summon;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CaseRowMapper implements RowMapper<Case> {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	PropertiesManager propertiesManager;

	@Override
	public Case mapRow(ResultSet rs, int rowNum) throws SQLException {

		Case caseObj = new Case();
		caseObj.setCode(rs.getString("code"));
		caseObj.setOldCaseNo(rs.getString("oldcaseno"));
		caseObj.setSuitNo(rs.getString("suitNo"));
		caseObj.setJudgeDetails(rs.getString("judgeDetails"));
		caseObj.setCaseRefernceNo(rs.getString("caseRefernceNo"));
		caseObj.setDepartmentPerson(rs.getString("departmentPerson"));
		caseObj.setCaseRegistrationDate(rs.getLong("caseRegistrationDate"));
		caseObj.setVakalatnamaGenerationDate(rs.getLong("vakalatnamaGenerationDate"));
		caseObj.setIsVakalatnamaGenerated(rs.getBoolean("isVakalatnamaGenerated"));
		caseObj.setCoName(rs.getString("coName"));
		caseObj.setAge(rs.getString("age"));
		caseObj.setDays(rs.getInt("days"));
		caseObj.setTenantId(getString(rs.getString("tenantid")));

		Summon summon = new Summon();
		summon.setCode(getString(rs.getObject("code")));
		summon.setIsSummon(getBoolean(rs.getBoolean("isSummon")));
		summon.setSummonReferenceNo(getString(rs.getObject("summonReferenceNo")));
		summon.setCaseNo(getString(rs.getObject("caseNo")));
		summon.setSummonDate(getLong(rs.getObject("summonDate")));
		summon.setYear(getString(rs.getObject("year")));
		summon.setPlantiffName(getString(rs.getObject("plantiffName")));
		summon.setDefendant(getString(rs.getObject("defendant")));
		summon.setSectionApplied(getString(rs.getObject("sectionApplied")));
		summon.setHearingDate(getLong(rs.getObject("hearingDate")));
		summon.setHearingTime(getString(rs.getObject("hearingTime")));
		summon.setWard(getString(rs.getObject("ward")));
		summon.setTenantId(getString(rs.getObject("tenantId")));
		summon.setStateId(getString(rs.getObject("stateId")));
		summon.setCaseDetails(getString(rs.getObject("caseDetails")));
		summon.setEntryType(EntryType.fromValue(getString(rs.getString("entryType"))));
		summon.setIsUlbinitiated(getBoolean(rs.getBoolean("isUlbinitiated")));

		try {
			if (rs.getString("departmentName") != null) {
				TypeReference<Department> departmentNameRefernce = new TypeReference<Department>() {
				};
				Department department = new Department();
				department = objectMapper.readValue(getString(rs.getString("departmentName")), departmentNameRefernce);
				summon.setDepartmentName(department);
			}

			if (rs.getString("caseType") != null) {
				CaseType caseType = new CaseType();
				TypeReference<CaseType> caseTypeReference = new TypeReference<CaseType>() {
				};
				caseType = objectMapper.readValue(getString(rs.getString("caseType")), caseTypeReference);
				summon.setCaseType(caseType);
			}

			if (rs.getString("caseCategory") != null) {
				CaseCategory caseCategory = new CaseCategory();
				TypeReference<CaseCategory> casecategoryRef = new TypeReference<CaseCategory>() {
				};
				caseCategory = objectMapper.readValue(rs.getString("caseCategory"), casecategoryRef);
				summon.setCaseCategory(caseCategory);
			}

			if (rs.getString("courtName") != null) {
				Court court = new Court();
				TypeReference<Court> courtRefType = new TypeReference<Court>() {
				};
				court = objectMapper.readValue(rs.getString("courtName"), courtRefType);
				summon.setCourtName(court);
			}

			if (rs.getString("bench") != null) {
				Bench bench = new Bench();
				TypeReference<Bench> benchRef = new TypeReference<Bench>() {
				};
				bench = objectMapper.readValue(rs.getString("bench"), benchRef);
				summon.setBench(bench);

			}

			if (rs.getString("side") != null) {
				Side side = new Side();
				TypeReference<Side> sideRef = new TypeReference<Side>() {
				};
				side = objectMapper.readValue(rs.getString("side"), sideRef);
				summon.setSide(side);
			}

			if (rs.getString("register") != null) {
				Register register = new Register();
				TypeReference<Register> registerRef = new TypeReference<Register>() {
				};
				register = objectMapper.readValue(rs.getString("register"), registerRef);
				summon.setRegister(register);

			}

			if (rs.getString("documents") != null) {
				List<Document> documents = new ArrayList<Document>();
				TypeReference<List<Document>> documentRefType = new TypeReference<List<Document>>() {
				};
				documents = objectMapper.readValue(rs.getString("documents"), documentRefType);
				summon.setDocuments(documents);

			}

			if (rs.getString("witness") != null) {
				List<String> witness = new ArrayList<String>();
				TypeReference<List<String>> witnessRefType = new TypeReference<List<String>>() {
				};
				witness = objectMapper.readValue(getString(rs.getString("witness")), witnessRefType);
				caseObj.setWitness(witness);
			}
			if (rs.getString("address") != null) {
				Address address = new Address();
				TypeReference<Address> adressRefType = new TypeReference<Address>() {
				};
				address = objectMapper.readValue(getString(rs.getString("address")), adressRefType);
				caseObj.setAddress(address);
			}

			if (rs.getString("plantiffaddress") != null) {
				Address address = new Address();
				TypeReference<Address> adReference = new TypeReference<Address>() {
				};
				address = objectMapper.readValue(getString(rs.getString("plantiffaddress")), adReference);
				summon.setPlantiffAddress(address);

			}

			caseObj.setSummon(summon);
			
			if(rs.getString("casestatus") != null){
				TypeReference<CaseStatus> caseStatusRef = new TypeReference<CaseStatus>() {
				};
				caseObj.setCaseStatus(objectMapper.readValue(getString(rs.getString("casestatus")), caseStatusRef));
			}

		} catch (Exception e) {

			throw new CustomException(propertiesManager.getParsingError(), e.getMessage());
		}

		return caseObj;
	}

	/**
	 * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? null : object.toString();
	}

	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return object == null ? 0 : Long.parseLong(object.toString());
	}

	private Boolean getBoolean(Object object) {
		return object == null ? Boolean.FALSE : (Boolean) object;
	}
}