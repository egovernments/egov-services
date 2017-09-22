package org.egov.tradelicense.web.repository;

import java.util.Date;

import org.egov.tl.commons.web.requests.DocumentTypeV2Response;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
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

	public DocumentTypeV2Response findById(RequestInfoWrapper requestInfoWrapper, String tenatId, Long documentTypeId) {

		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName()
				+ propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getDocumentServiceV2SearchPath();
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
			log.error(propertiesManger.getDocumentEndPointErrormsg());
		}
		if (documentTypeV2Response != null && documentTypeV2Response.getDocumentTypes() != null
				&& documentTypeV2Response.getDocumentTypes().size() > 0) {
			return documentTypeV2Response;
		} else {
			return null;
		}

	}

	public DocumentTypeV2Response findByIdAndTlValues(TradeLicense tradeLicense, SupportDocument supportDocument,
			RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName()
				+ propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getDocumentServiceV2SearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		if (supportDocument.getDocumentTypeId() != null) {
			content.append("ids=" + supportDocument.getDocumentTypeId());
		}

		if (tradeLicense.getTenantId() != null) {
			content.append("&tenantId=" + tradeLicense.getTenantId());
		}

		if (tradeLicense.getCategoryId() != null) {
			content.append("&categoryId=" + tradeLicense.getCategoryId());
		}

		if (tradeLicense.getSubCategoryId() != null) {
			content.append("&subCategoryId=" + tradeLicense.getSubCategoryId());
		}

		if (tradeLicense.getApplicationType() != null) {
			content.append("&applicationType=" + tradeLicense.getApplicationType().name());
		}

		content.append("&enabled=" + "true");

		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
		url = url + content.toString();
		DocumentTypeV2Response documentTypeV2Response = null;
		try {
			documentTypeV2Response = restTemplate.postForObject(url, tlMasterRequestInfoWrapper,
					DocumentTypeV2Response.class);
		} catch (Exception e) {
			log.error(propertiesManger.getDocumentEndPointErrormsg());
		}
		if (documentTypeV2Response != null && documentTypeV2Response.getDocumentTypes() != null
				&& documentTypeV2Response.getDocumentTypes().size() > 0) {
			return documentTypeV2Response;
		} else {
			return null;
		}

	}

	public DocumentTypeV2Response findTradeMandatoryDocuments(TradeLicense tradeLicense,
			RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName()
				+ propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getDocumentServiceV2SearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();

		if (tradeLicense.getTenantId() != null) {
			content.append("&tenantId=" + tradeLicense.getTenantId());
		}

		if (tradeLicense.getCategoryId() != null) {
			content.append("&categoryId=" + tradeLicense.getCategoryId());
		}

		if (tradeLicense.getSubCategoryId() != null) {
			content.append("&subCategoryId=" + tradeLicense.getSubCategoryId());
		}

		if (tradeLicense.getApplicationType() != null) {
			content.append("&applicationType=" + tradeLicense.getApplicationType().name());
		}

		content.append("&enabled=" + "true");
		content.append("&mandatory=" + "true");
		content.append("&fallback=" + "true");

		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
		url = url + content.toString();
		DocumentTypeV2Response documentTypeV2Response = null;
		try {
			documentTypeV2Response = restTemplate.postForObject(url, tlMasterRequestInfoWrapper,
					DocumentTypeV2Response.class);
		} catch (Exception e) {
			log.error(propertiesManger.getDocumentEndPointErrormsg());
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