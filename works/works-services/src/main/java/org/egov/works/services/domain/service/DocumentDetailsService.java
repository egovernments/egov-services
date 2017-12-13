package org.egov.works.services.domain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.services.config.Constants;
import org.egov.works.services.config.PropertiesManager;
import org.egov.works.services.domain.repository.DocumentDetailRepository;
import org.egov.works.services.domain.repository.FileStoreRepository;
import org.egov.works.services.domain.validator.RequestValidator;
import org.egov.works.services.utils.ServiceUtils;
import org.egov.works.services.web.contract.*;
import org.egov.works.services.web.contract.factory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentDetailsService {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private DocumentDetailRepository documentDetailRepository;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	private ServiceUtils serviceUtils;

    @Autowired
    private RequestValidator requestValidator;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

	public DocumentDetailResponse createDocuments(final DocumentDetailRequest documentDetailRequest) {
        requestValidator.validateDocuments(documentDetailRequest);
		for (DocumentDetail document : documentDetailRequest.getDocumentDetails()) {
			document.setId(commonUtils.getUUID());
			document.setAuditDetails(serviceUtils
					.setAuditDetails(documentDetailRequest.getRequestInfo(), false));
		}
		kafkaTemplate.send(propertiesManager.getDocumentDetailsCreateTopic(), documentDetailRequest);
        DocumentDetailResponse documentDetailResponse = new DocumentDetailResponse();
        documentDetailResponse
                .setResponseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(documentDetailRequest.getRequestInfo(), true));
        documentDetailResponse.setDocumentDetails(documentDetailRequest.getDocumentDetails());
        return documentDetailResponse;
	}

	public DocumentDetailResponse updateDocuments(final DocumentDetailRequest documentDetailRequest) {
        requestValidator.validateDocuments(documentDetailRequest);
		for (DocumentDetail document : documentDetailRequest.getDocumentDetails()) {
            if(StringUtils.isBlank(document.getId()))
                document.setId(commonUtils.getUUID());
			document.setAuditDetails(serviceUtils
					.setAuditDetails(documentDetailRequest.getRequestInfo(), true));
		}
		kafkaTemplate.send(propertiesManager.getDocumentDetailsCreateTopic(), documentDetailRequest);
        DocumentDetailResponse documentDetailResponse = new DocumentDetailResponse();
        documentDetailResponse
                .setResponseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(documentDetailRequest.getRequestInfo(), true));
        documentDetailResponse.setDocumentDetails(documentDetailRequest.getDocumentDetails());
		return documentDetailResponse;
	}

	public DocumentDetailResponse searchDocuments(final DocumentDetailSearchCriteria documentDetailSearchCriteria, final RequestInfo requestInfo) {
        List<DocumentDetail> documents =  documentDetailRepository.findForCriteria(documentDetailSearchCriteria);
        DocumentDetailResponse documentDetailResponse = new DocumentDetailResponse();
        documentDetailResponse
                .setResponseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true));
        documentDetailResponse.setDocumentDetails(documents);
        return documentDetailResponse;
	}



}
