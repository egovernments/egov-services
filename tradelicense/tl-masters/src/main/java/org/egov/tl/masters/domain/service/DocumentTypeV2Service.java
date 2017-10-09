package org.egov.tl.masters.domain.service;

import java.util.List;

import org.egov.tl.commons.web.contract.DocumentType;
import org.egov.tl.commons.web.contract.DocumentTypeContract;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.DocumentTypeV2Request;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.masters.domain.repository.DocumentTypeDomainRepository;
import org.egov.tl.masters.persistence.repository.DocumentTypeJdbcRepository;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.DuplicateDocumentTypeException;
import org.egov.tradelicense.domain.exception.InvalidCategoryException;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.domain.exception.InvalidSubCategoryException;
import org.egov.tradelicense.util.ConstantUtility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * CategoryService implementation class
 * 
 * @author Shubham Pratap Singh
 *
 */

@Service
@Slf4j
@Transactional(readOnly = true)
public class DocumentTypeV2Service {

	@Autowired
	DocumentTypeJdbcRepository documentTypeJdbcRepository;

	@Autowired
	DocumentTypeDomainRepository documentTypeDomainRepository;

	@Autowired
	private PropertiesManager propertiesManager;

	public List<DocumentType> validateRelated(List<DocumentType> documenttypes, RequestInfo requestInfo,
			Boolean isNew) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		for (DocumentType documentType : documenttypes) {

			if (!isNew && documentType.getId() == null) {

				throw new InvalidInputException(propertiesManager.getInvalidDocumentTypeIdMsg(), requestInfo);
			} else if (!isNew && documentType.getId() != null) {

				if (documentTypeJdbcRepository.validateIdExistance(documentType.getId(),
						ConstantUtility.DOCUMENT_TYPE_TABLE_NAME) == Boolean.FALSE) {
					throw new InvalidInputException(propertiesManager.getInvalidDocumentTypeIdMsg(), requestInfo);

				}
			}

			Integer documents = documentTypeJdbcRepository.checkForDuplicate(documentType, requestInfo);
			if (documents != 0) {

				throw new DuplicateDocumentTypeException(propertiesManager.getDocumentTypeCustomMsg(), requestInfo);
			}

			if (documentType.getCategory() != null && !documentType.getCategory().isEmpty()
					&& documentTypeJdbcRepository.validateCodeExistance(documentType.getCategory(),
							ConstantUtility.CATEGORY_TABLE_NAME) == Boolean.FALSE) {
				throw new InvalidCategoryException(propertiesManager.getCategoryErrorMsg(), requestInfo);

			}

			if (documentType.getSubCategory() != null && !documentType.getSubCategory().isEmpty() && documentTypeJdbcRepository
					.validateSubCategoryIdExistance(documentType.getSubCategory()) == Boolean.FALSE) {
				throw new InvalidInputException(propertiesManager.getSubCategoryErrorMsg(), requestInfo);
			}

		}

		return documenttypes;

	}

	@Transactional
	public List<DocumentType> add(List<DocumentType> documentTypes, RequestInfo requestInfo) {

		// external end point validations
		validateRelated(documentTypes, requestInfo, true);

		// setting the id for the license and support document and fee details
		for (DocumentType documentType : documentTypes) {

			documentType.setId(documentTypeJdbcRepository.getNextSequence());

		}

		return documentTypes;
	}

	@Transactional
	public List<DocumentType> update(List<DocumentType> documentTypes, RequestInfo requestInfo) {

		// external end point validations
		validateRelated(documentTypes, requestInfo, false);

		return documentTypes;
	}

	@Transactional
	public void update(List<DocumentTypeContract> documentTypes) {
		ModelMapper moldemap = new ModelMapper();
		DocumentType documenttypeModel;
		for (DocumentTypeContract documentTypeContract : documentTypes) {
			documenttypeModel = new DocumentType();
			moldemap.map(documentTypeContract, documenttypeModel);
			documentTypeDomainRepository.update(documenttypeModel);
		}
	}

	public void addToQue(DocumentTypeV2Request request) {
		documentTypeDomainRepository.add(request);
	}

	public void updateToQue(DocumentTypeV2Request request) {
		documentTypeDomainRepository.update(request);
	}

	@Transactional
	public void save(List<DocumentTypeContract> documentTypes) {
		ModelMapper moldemap = new ModelMapper();
		DocumentType documenttypeModel;
		for (DocumentTypeContract documentTypeContract : documentTypes) {
			documenttypeModel = new DocumentType();
			moldemap.map(documentTypeContract, documenttypeModel);
			documentTypeDomainRepository.save(documenttypeModel);
		}
	}

	public List<DocumentType> search(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String enabled, String mandatory, String applicationType, String category, String subCategory,
			Integer pageSize, Integer offSet, Boolean fallback) {

		List<DocumentType> documentTypes = documentTypeJdbcRepository.getDocumentTypeContracts(tenantId, ids, name,
				enabled, mandatory, applicationType, category, subCategory, pageSize, offSet, fallback);

		for (DocumentType documentType : documentTypes) {
			documentType
					.setCategoryName(documentTypeDomainRepository.getCategoryName(documentType.getCategory()));

			documentType.setSubCategoryName(
					documentTypeDomainRepository.getCategoryName(documentType.getSubCategory()));
		}

		return documentTypes;
	}

}