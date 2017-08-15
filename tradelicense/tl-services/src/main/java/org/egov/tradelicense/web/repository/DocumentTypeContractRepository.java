package org.egov.tradelicense.web.repository;

import java.util.Date;

import org.egov.tl.commons.web.requests.DocumentTypeResponse;
import org.egov.tradelicense.common.domain.model.RequestInfoWrapper;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.web.requests.TlMasterRequestInfo;
import org.egov.tradelicense.web.requests.TlMasterRequestInfoWrapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentTypeContractRepository {

	private RestTemplate restTemplate;
	private String hostUrl;
	public static final String SEARCH_URL = "tl-masters/documenttype/v1/_search?";

	public DocumentTypeContractRepository(@Value("${egov.services.tl-masters_v1.hostname}") String hostUrl,
			RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = hostUrl;
	}

	public DocumentTypeResponse findById(TradeLicense tradeLicense, SupportDocument supportDocument,
			RequestInfoWrapper requestInfoWrapper) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (supportDocument.getDocumentTypeId() != null) {
			content.append("id=" + supportDocument.getDocumentTypeId());
		}

		if (tradeLicense.getTenantId() != null) {
			content.append("&tenantId=" + tradeLicense.getTenantId());
		}
		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
		url = url + content.toString();
		DocumentTypeResponse documentTypeResponse = null;
		try {
			documentTypeResponse = restTemplate.postForObject(url, tlMasterRequestInfoWrapper,
					DocumentTypeResponse.class);
		} catch (Exception e) {
			log.error("Error while connecting to the document type end point");
		}
		if (documentTypeResponse != null && documentTypeResponse.getDocumentTypes() != null
				&& documentTypeResponse.getDocumentTypes().size() > 0) {
			return documentTypeResponse;
		} else {
			return null;
		}

	}

	public TlMasterRequestInfoWrapper getTlMasterRequestInfoWrapper(RequestInfoWrapper requestInfoWrapper) {

		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = new TlMasterRequestInfoWrapper();
		TlMasterRequestInfo tlMasterRequestInfo = new TlMasterRequestInfo();
		ModelMapper mapper = new ModelMapper();
		mapper.map(requestInfoWrapper.getRequestInfo(), tlMasterRequestInfo);
		tlMasterRequestInfo.setTs(new Date().getTime());
		tlMasterRequestInfoWrapper.setRequestInfo(tlMasterRequestInfo);

		return tlMasterRequestInfoWrapper;
	}
}