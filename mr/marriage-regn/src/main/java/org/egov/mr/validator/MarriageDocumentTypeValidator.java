package org.egov.mr.validator;

import java.util.List;

import org.egov.mr.model.MarriageDocumentType;
import org.egov.mr.model.enums.ApplicationStatus;
import org.egov.mr.repository.MarriageDocumentTypeRepository;
import org.egov.mr.service.MarriageDocumentTypeService;
import org.egov.mr.web.contract.MarriageDocTypeRequest;
import org.egov.mr.web.contract.MarriageDocumentTypeSearchCriteria;
import org.egov.mr.web.contract.MarriageRegnRequest;
import org.egov.mr.web.validator.MarriageRegnValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MarriageDocumentTypeValidator implements Validator {

	@Autowired
	private MarriageDocumentTypeRepository marriageDocumentTypeRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return MarriageDocTypeRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		MarriageDocTypeRequest marriageDocTypeRequest = null;
		if (target instanceof MarriageDocTypeRequest)
			marriageDocTypeRequest = (MarriageDocTypeRequest) target;
		else
			throw new RuntimeException("Invalid Object type for MarriageRegn validator");
		log.info("::::inside validator::::::");
		validateDocType(marriageDocTypeRequest, errors);
	}

	public void validateDocType(MarriageDocTypeRequest request, Errors errors) {
		List<MarriageDocumentType> marriageDocTypes = request.getMarriageDocTypes();
		for (MarriageDocumentType docType : marriageDocTypes) {

			MarriageDocumentTypeSearchCriteria Criteria = MarriageDocumentTypeSearchCriteria.builder()
					.name(docType.getName()).tenantId(docType.getTenantId()).build();
			List<MarriageDocumentType> marriageDocTypesList = marriageDocumentTypeRepository.search(Criteria);

			if (!marriageDocTypesList.isEmpty())
				errors.rejectValue("marriageDocTypes", "", "DocumentType is already exixts with tenantid: "
						+ docType.getTenantId() + " and Name: " + docType.getName());
		}

	}
}
