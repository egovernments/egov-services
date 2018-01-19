package org.egov.lams.repository.helper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.Asset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AssetHelper {
	public static final Logger logger = LoggerFactory.getLogger(AssetHelper.class);

	public String getAssetParams(AgreementCriteria assetCriteria) {

		logger.info("inside get assetparams method");
		StringBuilder assetParams = new StringBuilder();

		if (assetCriteria.getAssetCategory() == null && assetCriteria.getElectionWard() == null
				&& assetCriteria.getRevenueWard() == null && assetCriteria.getAsset() == null
				&& assetCriteria.getLocality() == null && assetCriteria.getAssetCode() == null
				&& assetCriteria.getShopNumber() == null) {
			// this if condition is not entered in general from search agreements
			throw new RuntimeException("All search criteria for asset details are null");
		}
		boolean isAppendAndClause = false;

		if (assetCriteria.getAsset() != null) {
			assetParams.append("id=" + getIdParams(assetCriteria.getAsset()));
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
		}
		if (assetCriteria.getAssetCategory() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
			assetParams.append("assetCategory=" + assetCriteria.getAssetCategory());
		}

		if (assetCriteria.getAssetCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
			assetParams.append("code=" + assetCriteria.getAssetCode());
		}
		if (assetCriteria.getLocality() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
			assetParams.append("locality=" + assetCriteria.getLocality());
		}
		if (assetCriteria.getRevenueWard() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
			assetParams.append("ward=" + assetCriteria.getRevenueWard());
		}

		if (assetCriteria.getElectionWard() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
			assetParams.append("electionWard=" + assetCriteria.getElectionWard());
		}

		if (assetCriteria.getTenantId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
			assetParams.append("tenantId=" + assetCriteria.getTenantId());
		}

		if (assetCriteria.getShopNumber() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
			assetParams.append("doorNo="+ assetCriteria.getShopNumber());
		}
		
		if (assetCriteria.getShoppingComplexName() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
			assetParams.append("name=" + assetCriteria.getShoppingComplexName());
		}

		isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
		assetParams.append("size="+500);
		//FIXME TODO remove hard coded values in asset and allottee helper

		logger.info("inside get asset method and the string query is"+assetParams);
		return assetParams.toString();
	}

	/**
	 * method to return list of assetid from list of asset object
	 * 
	 * @param assetList
	 * @return
	 */
	public Set<Long> getAssetIdList(List<Asset> assetList) {
		
		Set<Long> assetIdSet = new HashSet<>();
		assetIdSet.addAll(assetList.stream().map(asset -> asset.getId()).collect(Collectors.toList()));
		return assetIdSet;
	}

	/**
	 * method to return list of assetid from, agreement list object
	 * 
	 * @param agreementList
	 * @return
	 */
	public Set<Long> getAssetIdListByAgreements(List<Agreement> agreementList) {
		Set<Long> assetIdSet = new HashSet<>();
		assetIdSet.addAll(agreementList.stream().map(agreement -> agreement.getAsset().getId()).collect(Collectors.toList()));
		return assetIdSet;
	}

	private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag) {
			queryString.append("&");
		}
		return true;
	}

	/**
	 * Given a list of Long, this method returns comma separated values of the
	 * list.
	 * 
	 * @param idList
	 * @return
	 */
	private String getIdParams(Set<Long> idList) {
		if (idList.isEmpty())
			return "";
		Long[] list = idList.toArray(new Long[idList.size()]);
		StringBuilder query = new StringBuilder();
		// might be a need to throw exception
		query.append(Long.toString(list[0]));
		for (int i = 1; i < idList.size(); i++) {
			query.append("," + list[i]);
		}

		return query.toString();
	}
}
