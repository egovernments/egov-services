package org.egov.egf.master.domain.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.egov.common.constants.EgfConstants;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.ChartOfAccount;
import org.egov.egf.master.domain.model.ChartOfAccountSearch;
import org.egov.egf.master.persistence.entity.ChartOfAccountEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.ChartOfAccountJdbcRepository;
import org.egov.egf.master.web.requests.ChartOfAccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChartOfAccountRepository {

	@Autowired
	private ChartOfAccountJdbcRepository chartOfAccountJdbcRepository;
	@Autowired
	private MastersQueueRepository chartOfAccountQueueRepository;

	public ChartOfAccount findById(ChartOfAccount chartOfAccount) {
		ChartOfAccountEntity entity = chartOfAccountJdbcRepository
				.findById(new ChartOfAccountEntity().toEntity(chartOfAccount));
		return entity.toDomain();

	}

	@Transactional
	public ChartOfAccount save(ChartOfAccount chartOfAccount) {
		ChartOfAccountEntity entity = chartOfAccountJdbcRepository
				.create(new ChartOfAccountEntity().toEntity(chartOfAccount));
		return entity.toDomain();
	}

	@Transactional
	public ChartOfAccount update(ChartOfAccount chartOfAccount) {
		ChartOfAccountEntity entity = chartOfAccountJdbcRepository
				.update(new ChartOfAccountEntity().toEntity(chartOfAccount));
		return entity.toDomain();
	}

	public void add(ChartOfAccountRequest request) {
		Map<String, Object> message = new HashMap<>();

		if (request.getRequestInfo().getAction().equalsIgnoreCase(EgfConstants.ACTION_CREATE)) {
			message.put("chartofaccount_create", request);
		} else {
			message.put("chartofaccount_update", request);
		}
		chartOfAccountQueueRepository.add(message);
	}

	public Pagination<ChartOfAccount> search(ChartOfAccountSearch domain) {

		Set<ChartOfAccount> chartOfAccountSet = new HashSet<ChartOfAccount>();

		Pagination<ChartOfAccount> finalResult = null;
		Pagination<ChartOfAccount> result = chartOfAccountJdbcRepository.search(domain);

		if (domain != null && domain.getAccountCodePurpose() != null
				&& domain.getAccountCodePurpose().getId() != null) {

			domain.setAccountCodePurpose(null);

			for (ChartOfAccount coa : result.getPagedData()) {
				chartOfAccountSet.add(coa);
				domain.setGlcode(coa.getGlcode() + "%");
				Pagination<ChartOfAccount> result1 = chartOfAccountJdbcRepository.search(domain);
				for (ChartOfAccount temp : result1.getPagedData()) {
					chartOfAccountSet.add(temp);
				}
				finalResult = result1;
			}
			finalResult.setTotalResults(chartOfAccountSet.size());
			finalResult.setPagedData(new ArrayList<>(chartOfAccountSet));

			return finalResult;

		} else

			return result;

	}

}