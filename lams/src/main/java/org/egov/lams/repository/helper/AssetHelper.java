package org.egov.lams.repository.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.Asset;
import org.springframework.stereotype.Component;

@Component
public class AssetHelper {

	public String getAssetUrl(AgreementCriteria assetCriteria) {

		StringBuilder assetParams = new StringBuilder();

		if (assetCriteria.getAssetCategory() == null && assetCriteria.getElectionWard() == null
				&& assetCriteria.getRevenueWard() == null && assetCriteria.getAsset() == null
				&& assetCriteria.getLocality() == null && assetCriteria.getAssetCode() == null) {
			// this if condition is not entered in geneal from search agreements
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
		/*
		 * if (searchAsset.getShoppingComplexNo() != null) { isAppendAndClause =
		 * addAndClauseIfRequired(isAppendAndClause, assetParams);
		 * assetParams.append("getShoppingComplexNo="+searchAsset.
		 * getShoppingComplexNo()); }
		 */
		if (assetCriteria.getAssetCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
			assetParams.append("code=" + assetCriteria.getAssetCode());
		}
		if (assetCriteria.getLocality() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
			assetParams.append("locality=" + assetCriteria.getLocality());
		}
		/*if (searchAsset.getRevenueWard() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
			assetParams.append("ward=" + searchAsset.getRevenueWard());
		}*/

		/*
		  if (searchAsset.getElectionWard() != null) { isAppendAndClause =
		  	addAndClauseIfRequired(isAppendAndClause, assetParams);
		  	assetParams.append("election_Ward="+searchAsset.getElectionWard()); }
		 */
		if (assetCriteria.getTenantId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, assetParams);
			assetParams.append("tenantId=" + assetCriteria.getTenantId());
		}
		/*
		 * if (searchAsset.getDoorNo() != null) { isAppendAndClause =
		 * addAndClauseIfRequired(isAppendAndClause, assetParams);
		 * assetParams.append("doorno=?");
		 * preparedStatementValues.add(searchAsset.getDoorNo()); }
		 */
		return assetParams.toString();
	}
	/**
	 * method to return list of assetid from list of asset object
	 * @param assetList
	 * @return
	 */
	public List<Long> getAssetIdList(List<Asset> assetList) {
		return assetList.stream().map(asset -> asset.getId()).collect(Collectors.toList());
	}

	/**
	 * method to return list of assetid fro, agreement list object
	 * @param agreementList
	 * @return
	 */
	public List<Long> getAssetIdListByAgreements(List<Agreement> agreementList) {
		return agreementList.stream().map(agreement -> agreement.getAsset().getId()).collect(Collectors.toList());
	}

	private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag) {
			queryString.append(" &");
		}
		return true;
	}

	/**
	 * Given a list of Long, this method returns comma separated values of the list.
	 * @param idList
	 * @return
	 */
	private String getIdParams(List<Long> idList) {
		if (idList.isEmpty())
			return "";
		StringBuilder query = new StringBuilder();
		//might be a need to throw exception
		query.append(Long.toString(idList.get(0)));
		for (int i = 1; i < idList.size(); i++) {
			query.append("," + idList.get(i));
		}
		
		return query.toString();
	}
}
