package org.egov.tradelicense.web.repository;

import java.util.Date;

import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.response.DocumentTypeResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.web.requests.TlMasterRequestInfo;
import org.egov.tradelicense.web.requests.TlMasterRequestInfoWrapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentTypeContractRepository {

	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManger;

	public DocumentTypeContractRepository(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public DocumentTypeResponse findById(RequestInfoWrapper requestInfoWrapper, String tenatId, Long documentTypeId) {

		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName()
				+ propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getDocumentServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();

		if (documentTypeId != null) {
			if (documentTypeId != null) {
				content.append("ids=" + documentTypeId);
			}
		}

		if (tenatId != null) {

			content.append("&tenantId=" + tenatId);

		}

		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
		url = url + content.toString();
		DocumentTypeResponse documentTypeResponse = null;
		try {
			documentTypeResponse = restTemplate.postForObject(url, tlMasterRequestInfoWrapper,
					DocumentTypeResponse.class);
		} catch (Exception e) {
			log.error(propertiesManger.getDocumentEndPointErrormsg());
		}
		if (documentTypeResponse != null && documentTypeResponse.getDocumentTypes() != null
				&& documentTypeResponse.getDocumentTypes().size() > 0) {
			return documentTypeResponse;
		} else {
			return null;
		}

	}

	public DocumentTypeResponse findById(TradeLicense tradeLicense, SupportDocument supportDocument,
			RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName()
				+ propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getDocumentServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		if (supportDocument.getDocumentTypeId() != null) {
			content.append("ids=" + supportDocument.getDocumentTypeId());
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
			log.error(propertiesManger.getDocumentEndPointErrormsg());
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