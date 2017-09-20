package org.egov.tradelicense.web.repository;

import java.util.Date;

import org.egov.tl.commons.web.contract.FeeMatrixSearchResponse;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.domain.enums.ApplicationType;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.web.contract.FinancialYearContract;
import org.egov.tradelicense.web.requests.TlMasterRequestInfo;
import org.egov.tradelicense.web.requests.TlMasterRequestInfoWrapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FeeMatrixRepository {

	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManger;
	
	@Autowired
	FinancialYearContractRepository financialYearContractRepository;

	public FeeMatrixRepository(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;

	}

	public FeeMatrixSearchResponse findFeeMatrix(TradeLicense license, RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName()
				+ propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getFeeMatrixServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		

		if (license.getTenantId() != null) {

			content.append("tenantId=" + license.getTenantId());

		}

		if (license.getApplication() != null) {

			if (license.getApplication().getApplicationType() != null) {

				content.append("&applicationType=" + license.getApplication().getApplicationType());
				
				Long tradeCommencementDate = license.getTradeCommencementDate();
				Long currentDate = System.currentTimeMillis();
				FinancialYearContract financialYearResponse = null;
				
				if(license.getApplication().getApplicationType().equalsIgnoreCase(ApplicationType.NEW.name())){
					
					financialYearResponse = financialYearContractRepository
							.findFinancialYearIdByDate(license.getTenantId(), tradeCommencementDate, requestInfoWrapper);
					
				} else if(license.getApplication().getApplicationType().equalsIgnoreCase(ApplicationType.RENEW.name())){
					
					financialYearResponse = financialYearContractRepository
							.findFinancialYearIdByDate(license.getTenantId(), currentDate, requestInfoWrapper);
				}
				
				if(financialYearResponse != null && financialYearResponse.getId() != null){
					content.append("&financialYear=" + financialYearResponse.getId().toString());
				}
			}

		}

		if (license.getTradeType() != null) {

			content.append("&businessNature=" + license.getTradeType().toString());

		}

		if (license.getCategoryId() != null) {

			content.append("&categoryId=" + license.getCategoryId());

		}

		if (license.getSubCategoryId() != null) {

			content.append("&subCategoryId=" + license.getSubCategoryId());

		}
		
		content.append("&fallBack=" + true);

		url = url + content.toString();
		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getRequestInfoWrapper(requestInfoWrapper);
		FeeMatrixSearchResponse feeMatrixSearchResponse = null;
		
		try {

			feeMatrixSearchResponse = restTemplate.postForObject(url, tlMasterRequestInfoWrapper,
					FeeMatrixSearchResponse.class);
			
		} catch (Exception e) {

			log.error(propertiesManger.getCatEndPointError());
		}

		if (feeMatrixSearchResponse != null && feeMatrixSearchResponse.getFeeMatrices() != null
				&& feeMatrixSearchResponse.getFeeMatrices().size() > 0) {

			return feeMatrixSearchResponse;

		} else {

			return null;
		}

	}

	public TlMasterRequestInfoWrapper getRequestInfoWrapper(RequestInfoWrapper requestInfoWrapper) {

		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = new TlMasterRequestInfoWrapper();
		TlMasterRequestInfo tlMasterRequestInfo = new TlMasterRequestInfo();
		ModelMapper mapper = new ModelMapper();
		mapper.map(requestInfoWrapper.getRequestInfo(), tlMasterRequestInfo);
		tlMasterRequestInfo.setTs(new Date().getTime());
		tlMasterRequestInfoWrapper.setRequestInfo(tlMasterRequestInfo);

		return tlMasterRequestInfoWrapper;
	}
}