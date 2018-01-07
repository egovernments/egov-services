package org.egov.lams.repository.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.SubSeqRenewal;
import org.springframework.stereotype.Component;

@Component
public class AgreementHelper {

	/**
	 * The given list of agreements is filtered for given allottees and assets.
	 * Then each agreement in the list is enriched with allottee and asset data.
	 * @param agreements
	 * @param allottees
	 * @param assets
	 * @return
	 */
	public List<Agreement> filterAndEnrichAgreements(List<Agreement> agreements, List<Allottee> allottees,
			List<Asset> assets) {
		
		List<Agreement> newAgreements = new ArrayList<>();
		Map<Long, Asset> assetMap = new HashMap<>();
		Map<Long, Allottee> allotteeMap = new HashMap<>();

		for (Allottee allottee : allottees) {
			allotteeMap.put(allottee.getId(), allottee);
		}
		for (Asset asset : assets) {
			assetMap.put(asset.getId(), asset);
		}
		for (Agreement agreement : agreements) {
			Long allotteeId = agreement.getAllottee().getId();
			Long assetId = agreement.getAsset().getId();
			if (allotteeMap.containsKey(allotteeId) && 
					assetMap.containsKey(assetId)) 
			{
				agreement.setAllottee(allotteeMap.get(allotteeId));
				agreement.setAsset(assetMap.get(assetId));
				newAgreements.add(agreement);
			}
		}
		return newAgreements;
	}
	
	public List<Agreement> enrichAgreementsWithSubSeqRenewals(List<Agreement> agreements,
			Map<Long, List<SubSeqRenewal>> subSeqRenewalMap) {
		List<Agreement> newAgreements = new ArrayList<>();

		for (Agreement agreement : agreements) {
			if (subSeqRenewalMap.containsKey(agreement.getId())) {
				agreement.setSubSeqRenewals(subSeqRenewalMap.get(agreement.getId()));
			}
			newAgreements.add(agreement);
		}
		return newAgreements;
	}

}
