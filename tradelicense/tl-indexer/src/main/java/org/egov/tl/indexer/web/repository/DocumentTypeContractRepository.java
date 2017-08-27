package org.egov.tl.indexer.web.repository;

import java.util.Date;

import org.egov.tl.commons.web.requests.DocumentTypeV2Response;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.indexer.config.PropertiesManager;
import org.egov.tl.indexer.domain.exception.EndPointException;
import org.egov.tl.indexer.web.requests.TlMasterRequestInfo;
import org.egov.tl.indexer.web.requests.TlMasterRequestInfoWrapper;
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
	private PropertiesManager propertiesManager;

	public DocumentTypeContractRepository(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public DocumentTypeV2Response findById(RequestInfoWrapper requestInfoWrapper, String tenatId, Long documentTypeId) {

		String hostUrl = propertiesManager.getTradeLicenseMasterServiceHostName()
				+ propertiesManager.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManager.getDocumentServiceV2SearchPath();
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
		DocumentTypeV2Response documentTypeV2Response = null;
		try {
			documentTypeV2Response = restTemplate.postForObject(url, tlMasterRequestInfoWrapper,
					DocumentTypeV2Response.class);
		} catch (Exception e) {
			throw new EndPointException(propertiesManager.getEndPointError() + url,
					requestInfoWrapper.getRequestInfo());
		}
		if (documentTypeV2Response != null && documentTypeV2Response.getDocumentTypes() != null
				&& documentTypeV2Response.getDocumentTypes().size() > 0) {
			return documentTypeV2Response;
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