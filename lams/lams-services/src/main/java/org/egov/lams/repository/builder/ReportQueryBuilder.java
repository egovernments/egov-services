package org.egov.lams.repository.builder;

import java.util.Map;

import org.egov.lams.web.contract.BaseRegisterRequest;
import org.egov.lams.web.contract.RenewalPendingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ReportQueryBuilder {

	public static final Logger logger = LoggerFactory.getLogger(ReportQueryBuilder.class);

	public static final String BASE_REGISTER_REPORT_QUERY = "select agreement.id as id, agreement.agreement_no as agreementnumber,agreement.status as status,agreement.source as source, usr.name as allotteename, asset.doorno as doorno,(select name from eg_boundary where id = asset.locality and tenantid =:tenantid) as locality,(select name from eg_boundary where id = asset.revenueward and tenantid =:tenantid) as revenueward,(select name from eg_boundary where id = asset.electionward and tenantid =:tenantid) as electionward,agreement.trade_license_number as tradelicensenumber, agreement.tender_number as tendernumber,agreement.tender_date as tenderdate, agreement.bank_guarantee_amount as bankguaranteeamount,agreement.bank_guarantee_date as bankguaranteedate, agreement.security_deposit as securitydeposit,agreement.security_deposit_date as securitydepositdate, agreement.commencement_date as commencementdate,agreement.goodwillamount as goodwillamount, agreement.timeperiod as timeperiod,agreement.expiry_date as expirydate,(select SUM(dd.amount) from eglams_demand dmd , eg_demand_details dd where agreement.id=dmd.agreementid and dmd.demandid::int = dd.id_demand) as rent,(select SUM(dd.amount) - SUM(dd.amt_collected) from eglams_demand dmd , eg_demand_details dd where agreement.id=dmd.agreementid and dmd.demandid::int = dd.id_demand) as pendingrent, agreement.payment_cycle as paymentcycle from eglams_agreement agreement, eg_user usr, egasset_asset asset where agreement.allottee = usr.id and agreement.asset = asset.id and agreement.tenant_id =:tenantid";

	public String getQueryForBaseRegisterReport(BaseRegisterRequest baseRegisterModel, Map<String, Object> params) {

		StringBuilder selectQuery = new StringBuilder(BASE_REGISTER_REPORT_QUERY);
		appendParams(baseRegisterModel, params, selectQuery);
		log.info("base register query" + selectQuery.toString());
		return selectQuery.toString();

	}

	/* same query is used with different search parameters */
	
	public String getQueryForRenewalPendingReport(RenewalPendingRequest renewalPendingModel,
			Map<String, Object> params) {

		StringBuilder selectQuery = new StringBuilder(BASE_REGISTER_REPORT_QUERY);
		appendParamsForRenewalPending(renewalPendingModel, params, selectQuery);
		log.info("Renewal Pending Report query" + selectQuery.toString());
		return selectQuery.toString();

	}

	private void appendParams(BaseRegisterRequest baseRegisterModel, Map<String, Object> params,
			StringBuilder selectQuery) {

		if (baseRegisterModel.getAssetCategory() != null) {
			selectQuery.append(" and asset.assetcategory =:assetcategory");
			params.put("assetcategory", baseRegisterModel.getAssetCategory());
		}
		if (baseRegisterModel.getRevenueWard() != null) {
			selectQuery.append(" and asset.revenueward = :revenueward");
			params.put("revenueward", baseRegisterModel.getRevenueWard());
		}
		if (baseRegisterModel.getTenantId() != null) {
			params.put("tenantid", baseRegisterModel.getTenantId());
		}
		selectQuery.append(" and agreement.status in ('ACTIVE') ");
	}

	private void appendParamsForRenewalPending(RenewalPendingRequest renewalPendingModel, Map<String, Object> params,
			StringBuilder selectQuery) {

		if (renewalPendingModel.getAssetCategory() != null) {
			selectQuery.append(" and asset.assetcategory =:assetcategory");
			params.put("assetcategory", renewalPendingModel.getAssetCategory());
		}
		if (renewalPendingModel.getRevenueWard() != null) {
			selectQuery.append(" and asset.revenueward = :revenueward");
			params.put("revenueward", renewalPendingModel.getRevenueWard());
		}
		if (renewalPendingModel.getTenantId() != null) {
			params.put("tenantid", renewalPendingModel.getTenantId());
		}

		selectQuery.append(" and agreement.status in ('ACTIVE') ");

		if (renewalPendingModel.getExpiryTime() != null) {
			selectQuery.append(" and agreement.expiry_date BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '");
			selectQuery.append(renewalPendingModel.getExpiryTime() + "'");

		}
		log.info("expiryTime" + params.get("expiryTime"));

	}
}
