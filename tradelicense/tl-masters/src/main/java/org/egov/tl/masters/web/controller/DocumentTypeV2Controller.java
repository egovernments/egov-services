package org.egov.tl.masters.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.DocumentType;
import org.egov.tl.commons.web.contract.DocumentTypeContract;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.requests.DocumentTypeV2Request;
import org.egov.tl.commons.web.requests.DocumentTypeV2Response;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.masters.domain.service.DocumentTypeV2Service;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.CustomBindException;
import org.egov.tradelicense.domain.exception.DocumentTypeNotEmptyException;
import org.egov.tradelicense.domain.exception.DocumentTypeNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/documenttype/v2")
public class DocumentTypeV2Controller {

	@Autowired
	DocumentTypeV2Service documentTypeService;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@Autowired
	PropertiesManager propertiesManager;

	@RequestMapping(path = "/_create", method = RequestMethod.POST)
	public DocumentTypeV2Response createDocumenttype(@Valid @RequestBody DocumentTypeV2Request documentTypeRequest,
			BindingResult errors) throws Exception {

		RequestInfo requestInfo = documentTypeRequest.getRequestInfo();
		if (errors.hasErrors()) {
			throw new CustomBindException(errors, requestInfo);
		}
		// check for existence of licenses
		if (documentTypeRequest.getDocumentTypes() == null) {
			throw new DocumentTypeNotFoundException(propertiesManager.getDocumentTypeNotFoundMsg(), requestInfo);
		} else if (documentTypeRequest.getDocumentTypes().size() == 0) {
			throw new DocumentTypeNotEmptyException(propertiesManager.getDocumentTypeEmptyMsg(), requestInfo);
		}

		ModelMapper model = new ModelMapper();
		DocumentTypeV2Response documentTypeResponse = new DocumentTypeV2Response();
		documentTypeResponse.setResponseInfo(getResponseInfo(requestInfo));
		List<DocumentType> documentTypes = new ArrayList<>();
		DocumentType documenttype;

		for (DocumentTypeContract documentTypeContract : documentTypeRequest.getDocumentTypes()) {

			documenttype = new DocumentType();
			model.map(documentTypeContract, documenttype);
			AuditDetails auditDetails = new AuditDetails();
			this.setAuditDetails(auditDetails, requestInfo, Boolean.TRUE);
			documenttype.setAuditDetails(auditDetails);

			documentTypes.add(documenttype);
		}
		documentTypes = documentTypeService.add(documentTypes, requestInfo);

		List<DocumentTypeContract> documentTypeContracts = new ArrayList<>();
		DocumentTypeContract contract;

		for (DocumentType document : documentTypes) {
			contract = new DocumentTypeContract();
			model.map(document, contract);
			documentTypeContracts.add(contract);
		}

		documentTypeRequest.setDocumentTypes(documentTypeContracts);
		documentTypeService.addToQue(documentTypeRequest);
		documentTypeResponse.setDocumentTypes(documentTypeContracts);

		// creating success message with the documenttype numbers numbers
		if (documentTypeResponse.getResponseInfo() != null && documentTypeResponse.getDocumentTypes() != null
				&& documentTypeResponse.getDocumentTypes().size() > 0) {
			String statusMessage = propertiesManager.getDocumentTypeCreateSuccessMessage();
			documentTypeResponse.getResponseInfo().setStatus(statusMessage);
		}

		return documentTypeResponse;
	}

	@RequestMapping(path = "/_update", method = RequestMethod.POST)
	public DocumentTypeV2Response updateDocumenttype(@Valid @RequestBody DocumentTypeV2Request documentTypeRequest,
			BindingResult errors) throws Exception {

		// validate(documentTypeRequest.getDocumentTypes(), errors);
		RequestInfo requestInfo = documentTypeRequest.getRequestInfo();

		if (errors.hasErrors()) {
			throw new CustomBindException(errors, requestInfo);
		}
		// check for existence of documenttypes
		if (documentTypeRequest.getDocumentTypes() == null) {
			throw new DocumentTypeNotFoundException(propertiesManager.getDocumentTypeNotFoundMsg(), requestInfo);
		} else if (documentTypeRequest.getDocumentTypes().size() == 0) {
			throw new DocumentTypeNotEmptyException(propertiesManager.getDocumentTypeEmptyMsg(), requestInfo);
		}

		ModelMapper model = new ModelMapper();
		DocumentTypeV2Response documentTypeResponse = new DocumentTypeV2Response();
		documentTypeResponse.setResponseInfo(getResponseInfo(requestInfo));
		List<DocumentType> documentTypes = new ArrayList<>();
		DocumentType documenttype;

		for (DocumentTypeContract documentTypeContract : documentTypeRequest.getDocumentTypes()) {

			documenttype = new DocumentType();
			model.map(documentTypeContract, documenttype);
			AuditDetails auditDetails = new AuditDetails();
			this.setAuditDetails(auditDetails, requestInfo, Boolean.FALSE);
			documenttype.setAuditDetails(auditDetails);
			documentTypes.add(documenttype);
		}
		documentTypeService.update(documentTypes, requestInfo);
		List<DocumentTypeContract> documentTypeContracts = new ArrayList<>();
		DocumentTypeContract contract;

		for (DocumentType document : documentTypes) {
			contract = new DocumentTypeContract();
			model.map(document, contract);
			documentTypeContracts.add(contract);
		}

		documentTypeRequest.setDocumentTypes(documentTypeContracts);
		documentTypeService.updateToQue(documentTypeRequest);
		documentTypeResponse.setDocumentTypes(documentTypeContracts);

		// creating success message with the Documenttypes numbers
		if (documentTypeResponse.getResponseInfo() != null && documentTypeResponse.getDocumentTypes() != null
				&& documentTypeResponse.getDocumentTypes().size() > 0) {
			String statusMessage = propertiesManager.getDocumentTypeUpdateSuccessMessage();
			documentTypeResponse.getResponseInfo().setStatus(statusMessage);
		}

		return documentTypeResponse;
	}

	/**
	 * Description : This api for searching DocumentType master
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param enabled
	 * @param applicationType
	 * @param pageSize
	 * @param offSet
	 * @return DocumentTypeResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/_search", method = RequestMethod.POST)
	public DocumentTypeV2Response search(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String name, @RequestParam(required = false) String enabled,
			@RequestParam(required = false) String mandatory, @RequestParam(required = false) String applicationType,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String category,
			@RequestParam(required = false) String subCategory, @RequestParam(required = false) Integer offSet,
			@RequestParam(required = false) Boolean fallback)
			throws Exception {

		List<DocumentTypeContract> contracts = new ArrayList<DocumentTypeContract>();

		List<DocumentType> documentTypes = documentTypeService.search(requestInfo.getRequestInfo(), tenantId, ids, name,
				enabled, mandatory, applicationType, category, subCategory, pageSize, offSet, fallback);

		DocumentTypeContract contract;
		ModelMapper model = new ModelMapper();
		for (DocumentType documentType : documentTypes) {
			contract = new DocumentTypeContract();
			model.map(documentType, contract);
			contracts.add(contract);
		}
		DocumentTypeV2Response response = new DocumentTypeV2Response();
		response.setDocumentTypes(contracts);
		ResponseInfo responseInfo = getResponseInfo(requestInfo.getRequestInfo());
		response.setResponseInfo(responseInfo);

		return response;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {

		return responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);

	}

	private AuditDetails setAuditDetails(AuditDetails auditDetails, RequestInfo requestInfo, Boolean create) {

		if (requestInfo != null && requestInfo.getUserInfo() != null && requestInfo.getUserInfo().getId() != null) {

			if (create == Boolean.TRUE) {
				auditDetails.setCreatedBy(requestInfo.getUserInfo().getId().toString());
				auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getId().toString());
				auditDetails.setCreatedTime(new Date().getTime());
				auditDetails.setLastModifiedTime(new Date().getTime());
			} else {
				auditDetails.setLastModifiedTime(new Date().getTime());
				auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getId().toString());
			}

		}

		return auditDetails;
	}

}
