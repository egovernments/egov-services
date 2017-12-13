package org.egov.lcms.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.AdvocateDetails;
import org.egov.lcms.models.Case;
import org.egov.lcms.models.CaseRequest;
import org.egov.lcms.models.Summon;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SummonValidator {
	/**
	 * * This API will validate the summon object
	 * 
	 * @param summonRequest
	 * @throws Exception
	 */
	@Autowired
	PropertiesManager propertiesManager;

	public void validateSummon(CaseRequest caseRequest) throws Exception {
		for (Case legalCase : caseRequest.getCases()) {
			Summon summon = legalCase.getSummon();
			if (summon.getTenantId() == null) {
				throw new CustomException(propertiesManager.getTenantMandatoryCode(),
						propertiesManager.getTenantMandatoryMessage());
			} else if (summon.getSummonReferenceNo() == null || summon.getSummonReferenceNo().isEmpty()) {
				throw new CustomException(propertiesManager.getRefrencenoCode(),
						propertiesManager.getRefrencenoMessage());
			} else if (summon.getYear() == null) {
				throw new CustomException(propertiesManager.getYearCode(), propertiesManager.getYearMessage());
			} else if (summon.getPlantiffName() == null) {
				throw new CustomException(propertiesManager.getPlaintiffCode(),
						propertiesManager.getPlaintiffMessage());

			} else if (summon.getPlantiffAddress() == null) {
				throw new CustomException(propertiesManager.getPlaintiffaddressCode(),
						propertiesManager.getPlaintiffaddressMessage());
			} else if (summon.getDefendant() == null || summon.getDefendant().isEmpty()) {
				throw new CustomException(propertiesManager.getDefendentCode(),
						propertiesManager.getDefendentMessage());

			} else if (summon.getCourtName() == null) {
				throw new CustomException(propertiesManager.getCourtnameCode(),
						propertiesManager.getCourtnameMessage());

			} else if (summon.getWard() == null || summon.getWard().isEmpty()) {
				throw new CustomException(propertiesManager.getWardCode(), propertiesManager.getWardMessage());
			} else if (summon.getBench() == null) {
				throw new CustomException(propertiesManager.getBenchCode(), propertiesManager.getBenchMessage());
			} else if (summon.getRegister() == null) {
				throw new CustomException(propertiesManager.getStampCode(), propertiesManager.getStampMessage());
			} else if (summon.getSummonDate() == null) {
				throw new CustomException(propertiesManager.getSummondateCode(),
						propertiesManager.getSummondateMessage());
			} else if (summon.getCaseType() == null) {
				throw new CustomException(propertiesManager.getCasetypeCode(), propertiesManager.getCasetypeMessage());
			} else if (summon.getCaseNo() == null || summon.getCaseNo().isEmpty()) {
				throw new CustomException(propertiesManager.getCasenoCode(), propertiesManager.getCasenoMessage());

			} else if (summon.getCaseDetails() == null) {
				throw new CustomException(propertiesManager.getCasedetailsCode(),
						propertiesManager.getCasedetailsMessage());

			} else if (summon.getDepartmentName() == null) {
				throw new CustomException(propertiesManager.getDepartmentNameCode(),
						propertiesManager.getDepartmentNameMessage());
			} else if (summon.getHearingDate() == null) {
				throw new CustomException(propertiesManager.getHearingdateCode(),
						propertiesManager.getHearingdateMessage());
			} else if (summon.getSide() == null) {
				throw new CustomException(propertiesManager.getSideCode(), propertiesManager.getSideMessage());
			}

		}
	}
	
	public void validateDuplicateAdvocates(Case caseObj) {
		List<String> advocateCodes = new ArrayList<String>();
		for (AdvocateDetails advocateDetails : caseObj.getAdvocateDetails()) {
			advocateCodes.add(advocateDetails.getAdvocate().getCode());
		}
		Set<String> advocateCodeSet = new HashSet<String>(advocateCodes);
		if (advocateCodeSet.size() < advocateCodes.size()) {
			throw new CustomException(propertiesManager.getDuplicateAdvocate(),
					propertiesManager.getDuplicateAdvocateMessage());
		}

	}
}
