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
import org.egov.works.services.utils.ServiceUtils;
import org.egov.works.services.web.contract.DocumentDetail;
import org.egov.works.services.web.contract.DocumentDetailRequest;
import org.egov.works.services.web.contract.DocumentDetailSearchCriteria;
import org.egov.works.services.web.contract.DocumentDetailSearchRequest;
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
	private FileStoreRepository fileStoreRepository;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	private ServiceUtils serviceUtils;

	public List<DocumentDetail> createDocuments(final DocumentDetailRequest documentDetailRequest) {
		for (DocumentDetail document : documentDetailRequest.getDocumentDetails()) {
			document.setId(commonUtils.getUUID());
			document.setAuditDetails(serviceUtils
					.setAuditDetails(documentDetailRequest.getRequestInfo(), false));
		}
		kafkaTemplate.send(propertiesManager.getDocumentDetailsCreateTopic(), documentDetailRequest);
		return documentDetailRequest.getDocumentDetails();
	}

	public List<DocumentDetail> updateDocuments(final DocumentDetailRequest documentDetailRequest) {
		for (DocumentDetail document : documentDetailRequest.getDocumentDetails()) {
            if(StringUtils.isBlank(document.getId()))
                document.setId(commonUtils.getUUID());
			document.setAuditDetails(serviceUtils
					.setAuditDetails(documentDetailRequest.getRequestInfo(), true));
		}
		kafkaTemplate.send(propertiesManager.getDocumentDetailsUpdateTopic(), documentDetailRequest);
		return documentDetailRequest.getDocumentDetails();
	}

	public List<DocumentDetail> searchDocuments(final DocumentDetailSearchCriteria documentDetailSearchCriteria) {
		return documentDetailRepository.findForCriteria(documentDetailSearchCriteria);
	}

	public void validateDocuments(final DocumentDetailRequest documentDetailRequest) {

		Map<String, String> messages = new HashMap<>();
		for (DocumentDetail documentDetail : documentDetailRequest.getDocumentDetails()) {
			if (StringUtils.isNotBlank(documentDetail.getFileStore())) {
				boolean fileExists = fileStoreRepository.searchFileStore(documentDetail.getTenantId(),
						documentDetail.getFileStore(), documentDetailRequest.getRequestInfo());
				if (!fileExists) {
					messages.put(Constants.KEY_FILESTORE_INVALID, Constants.MESSAGE_FILESTORE_INVALID);
				}
			}
		}
		if (messages != null && !messages.isEmpty())
			throw new CustomException(messages);
	}

	public void validateSearchDocuments(final DocumentDetailSearchRequest documentDetailSearchRequest) {
		Map<String, String> messages = new HashMap<>();
		if (StringUtils.isBlank(documentDetailSearchRequest.getTenantId())) {
			messages.put(Constants.KEY_TENANTID_INVALID, Constants.MESSAGE_TENANTID_INVALID);
		}
		if (messages != null && !messages.isEmpty())
			throw new CustomException(messages);

	}

}
