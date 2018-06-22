package org.egov.lams.repository.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.enums.Action;
import org.egov.lams.service.AgreementService;
import org.egov.lams.service.LamsConfigurationService;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DemandHelper {

	public static final Logger logger = LoggerFactory.getLogger(AgreementService.class);
	
	private static final List<String> GST_REASONS = Arrays.asList("ADV_CGST", "ADV_SGST","GW_CGST", "GW_SGST", "CENTRAL_GST", "STATE_GST");

	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
	LamsConfigurationService lamsConfigurationService;

	public String getDemandReasonUrlParams(AgreementRequest agreementRequest, String taxReason, Date date) {

		Agreement agreement = agreementRequest.getAgreement();

		logger.info("the criteria for demandReasonSearch are ::: " + "?moduleName="
				+ propertiesManager.getDemandModuleName() + "&taxPeriod=" + agreement.getTimePeriod() + "&fromDate="
				+ agreement.getCommencementDate() + "&toDate=" + date + "&installmentType="
				+ agreement.getPaymentCycle().toString() + "&taxCategory=" + propertiesManager.getTaxCategoryName());
		Date fromDate;
		Date gstDate = getGstEffectiveDate(agreement.getTenantId());
		if(Action.RENEWAL.equals(agreement.getAction()))
			fromDate = agreement.getRenewalDate();
		else
			fromDate = agreement.getCommencementDate();
		if (agreement.getCommencementDate().compareTo(gstDate) < 0
				&& ("STATE_GST".equalsIgnoreCase(taxReason) || "CENTRAL_GST".equalsIgnoreCase(taxReason))) {
			fromDate = gstDate;
		}
		StringBuilder urlParams = new StringBuilder();
		urlParams.append("?moduleName=" + propertiesManager.getDemandModuleName());
		urlParams.append("&taxPeriod=" + agreement.getTimePeriod());
		urlParams.append("&fromDate=" + DateFormatUtils.format(fromDate, "dd/MM/yyyy"));
		urlParams.append("&installmentType=" + agreement.getPaymentCycle().toString());
		if (GST_REASONS.stream().anyMatch(reason -> reason.equalsIgnoreCase(taxReason))) {
			urlParams.append("&taxCategory=GST");
		} else if ("SERVICE_TAX".equalsIgnoreCase(taxReason)) {
			urlParams.append("&taxCategory=SERVICETAX");
		} else
			urlParams.append("&taxCategory=" + (propertiesManager.getTaxReasonPenalty().equalsIgnoreCase(taxReason)
					? propertiesManager.getPenaltyCategoryName() : propertiesManager.getTaxCategoryName()));

		urlParams.append("&tenantId=" + agreement.getTenantId());
		urlParams.append("&taxReason=" + taxReason);
		urlParams.append("&toDate=" + DateFormatUtils.format(date, "dd/MM/yyyy"));
		return urlParams.toString();
	}
	
	public String getDemandIdParams(List<Long> idList) {
		String dmdIdString = null;
		if (!idList.isEmpty()) {
			dmdIdString = idList.stream().map(Object::toString).collect(Collectors.joining(","));
		}
		return dmdIdString;
	}
	
	private Date getGstEffectiveDate(String tenantId) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");// remove this Simple Date Format
		Date gstDate = null;
		LamsConfigurationGetRequest lamsConfigurationGetRequest = new LamsConfigurationGetRequest();
		lamsConfigurationGetRequest.setName(propertiesManager.getGstEffectiveDate());
		lamsConfigurationGetRequest.setTenantId(tenantId);
		List<String> gstDates = lamsConfigurationService.getLamsConfigurations(lamsConfigurationGetRequest)
				.get(propertiesManager.getGstEffectiveDate());
		if (gstDates.isEmpty()) {
			return null;
		} else {
			try {
				gstDate = formatter.parse(gstDates.get(0));
			} catch (ParseException e) {
				logger.error("exception in parsing GST date  ::: " + e);
			}

		}
		return gstDate;
	}
}
