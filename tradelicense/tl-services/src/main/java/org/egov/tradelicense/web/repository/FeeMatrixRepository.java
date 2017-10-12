package org.egov.tradelicense.web.repository;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.egov.tl.commons.web.contract.FeeMatrixSearchResponse;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.CustomInvalidInputException;
import org.egov.tradelicense.common.domain.exception.EndPointException;
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
	private PropertiesManager propertiesManager;

	@Autowired
	FinancialYearContractRepository financialYearContractRepository;

	public FeeMatrixRepository(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;

	}

	public FeeMatrixSearchResponse findFeeMatrix(TradeLicense license, RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManager.getTradeLicenseMasterServiceHostName()
				+ propertiesManager.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManager.getFeeMatrixServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();

		if (license.getTenantId() != null && !license.getTenantId().isEmpty()) {

			content.append("tenantId=" + license.getTenantId());

		}

		if (license.getApplication() != null) {

			if (license.getApplication().getApplicationType() != null) {

				content.append("&applicationType=" + license.getApplication().getApplicationType());

				Long tradeCommencementDate = license.getTradeCommencementDate();
				Long currentDate = System.currentTimeMillis();
				Long financialYearRequiredDate = null;
				FinancialYearContract financialYearResponse = null;

				if (license.getApplication().getApplicationType().equalsIgnoreCase(ApplicationType.NEW.name())) {

					financialYearRequiredDate = tradeCommencementDate;
					financialYearResponse = financialYearContractRepository.findFinancialYearIdByDate(
							license.getTenantId(), tradeCommencementDate, requestInfoWrapper);

				} else if (license.getApplication().getApplicationType()
						.equalsIgnoreCase(ApplicationType.RENEW.name())) {

					financialYearRequiredDate = currentDate;
					financialYearResponse = financialYearContractRepository
							.findFinancialYearIdByDate(license.getTenantId(), currentDate, requestInfoWrapper);
				}

				if (financialYearResponse != null) {

					if (financialYearResponse.getFinYearRange() != null) {

						content.append("&financialYear=" + financialYearResponse.getFinYearRange());
					}

				} else {

					String financialNotFoundErrorMsg = propertiesManager.getFinancialYearNotFoundMsg();

					if (financialNotFoundErrorMsg != null) {

						SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
						String asOnDate = sf.format(new Date(financialYearRequiredDate));
						financialNotFoundErrorMsg = financialNotFoundErrorMsg.replace(":financialNotFoundDate",
								asOnDate);
					}

					throw new CustomInvalidInputException(propertiesManager.getFinancialYearNotFoundCode(),
							financialNotFoundErrorMsg, requestInfoWrapper.getRequestInfo());
				}
			}

		}

		if (license.getTradeType() != null) {

			content.append("&businessNature=" + license.getTradeType().toString());

		}

		if (license.getCategory() != null) {

			content.append("&category=" + license.getCategory());

		}

		if (license.getSubCategory() != null) {

			content.append("&subCategory=" + license.getSubCategory());

		}

		content.append("&fallBack=" + true);

		url = url + content.toString();
		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getRequestInfoWrapper(requestInfoWrapper);
		FeeMatrixSearchResponse feeMatrixSearchResponse = null;

		try {

			feeMatrixSearchResponse = restTemplate.postForObject(url, tlMasterRequestInfoWrapper,
					FeeMatrixSearchResponse.class);

		} catch (Exception e) {

			throw new EndPointException("Error connecting to FeeMatrix end point " + url,
					requestInfoWrapper.getRequestInfo());
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