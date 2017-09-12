package org.egov.egf.budget.index.consumer;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.egov.egf.budget.index.persistence.repository.ElasticSearchRepository;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.egov.egf.budget.web.contract.BudgetDetailContract;
import org.egov.egf.budget.web.contract.BudgetDetailRequest;
import org.egov.egf.budget.web.contract.BudgetReAppropriationContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationRequest;
import org.egov.egf.budget.web.contract.BudgetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BudgetIndexerListener {

	private static final String BUDGET_OBJECT_TYPE = "budget";
	private static final String BUDGETDETAIL_OBJECT_TYPE = "budgetdetail";
	private static final String BUDGETREAPPROPRIATION_OBJECT_TYPE = "budgetreappropriation";

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	@Autowired
	private ElasticSearchRepository elasticSearchRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(id = "${kafka.topics.egf.budget.completed.id}", topics = {
			"${kafka.topics.egf.budget.completed.topic}" }, group = "${kafka.topics.egf.budget.completed.group}")
	public void listen(final HashMap<String, Object> budgetRequestMap) {

		if (budgetRequestMap.get("budget_persisted") != null) {

			final BudgetRequest request = objectMapper.convertValue(budgetRequestMap.get("budget_persisted"),
					BudgetRequest.class);

			if (request.getBudgets() != null && !request.getBudgets().isEmpty())
				for (final BudgetContract budget : request.getBudgets()) {
					final HashMap<String, Object> indexObj = getBudgetContractIndexObject(budget);
					elasticSearchRepository.index(BUDGET_OBJECT_TYPE, budget.getTenantId() + "-" + budget.getName(),
							indexObj);
				}
		}

		if (budgetRequestMap.get("budgetreappropriation_persisted") != null) {

			final BudgetReAppropriationRequest request = objectMapper.convertValue(
					budgetRequestMap.get("budgetreappropriation_persisted"), BudgetReAppropriationRequest.class);

			if (request.getBudgetReAppropriations() != null && !request.getBudgetReAppropriations().isEmpty())
				for (final BudgetReAppropriationContract budgetReAppropriationContract : request
						.getBudgetReAppropriations()) {
					final HashMap<String, Object> indexObj = getBudgetReAppropriationContractIndexObject(
							budgetReAppropriationContract);
					elasticSearchRepository
							.index(BUDGETREAPPROPRIATION_OBJECT_TYPE,
									budgetReAppropriationContract.getTenantId() + "-"
											+ budgetReAppropriationContract.getBudgetDetail().getBudget().getName(),
									indexObj);
				}
		}

		if (budgetRequestMap.get("budgetdetail_persisted") != null) {

			final BudgetDetailRequest request = objectMapper
					.convertValue(budgetRequestMap.get("budgetdetail_persisted"), BudgetDetailRequest.class);

			if (request.getBudgetDetails() != null && !request.getBudgetDetails().isEmpty())
				for (final BudgetDetailContract budgetDetailContract : request.getBudgetDetails()) {
					final HashMap<String, Object> indexObj = getBudgetDetailContractIndexObject(budgetDetailContract);
					elasticSearchRepository.index(BUDGETDETAIL_OBJECT_TYPE,
							budgetDetailContract.getTenantId() + "-" + budgetDetailContract.getBudget().getName(),
							indexObj);
				}
		}

	}

	private HashMap<String, Object> getBudgetReAppropriationContractIndexObject(
			final BudgetReAppropriationContract budgetReAppropriationContract) {

		final HashMap<String, Object> indexObj = new HashMap<String, Object>();
		indexObj.put("id", budgetReAppropriationContract.getId());
		indexObj.put("budgetDetail", budgetReAppropriationContract.getBudgetDetail());
		indexObj.put("additionAmount", budgetReAppropriationContract.getAdditionAmount());
		indexObj.put("asOnDate", budgetReAppropriationContract.getAsOnDate());
		indexObj.put("deductionAmount", budgetReAppropriationContract.getDeductionAmount());
		indexObj.put("originalAdditionAmount", budgetReAppropriationContract.getOriginalAdditionAmount());
		indexObj.put("originalDeductionAmount", budgetReAppropriationContract.getOriginalDeductionAmount());
		indexObj.put("anticipatoryAmount", budgetReAppropriationContract.getAnticipatoryAmount());
		indexObj.put("status", budgetReAppropriationContract.getStatus());
		indexObj.put("tenantId", budgetReAppropriationContract.getTenantId());
		indexObj.put("createdBy", budgetReAppropriationContract.getCreatedBy());
		indexObj.put("lastModifiedBy", budgetReAppropriationContract.getLastModifiedBy());

		if (budgetReAppropriationContract.getCreatedDate() != null)
			indexObj.put("createdDate", formatter.format(budgetReAppropriationContract.getCreatedDate()));
		else
			indexObj.put("createdDate", null);
		if (budgetReAppropriationContract.getLastModifiedDate() != null)
			indexObj.put("lastModifiedDate", formatter.format(budgetReAppropriationContract.getLastModifiedDate()));
		else
			indexObj.put("lastModifiedDate", null);

		return indexObj;
	}

	private HashMap<String, Object> getBudgetDetailContractIndexObject(
			final BudgetDetailContract budgetDetailContract) {

		final HashMap<String, Object> indexObj = new HashMap<String, Object>();
		indexObj.put("id", budgetDetailContract.getId());
		indexObj.put("budget", budgetDetailContract.getBudget());
		indexObj.put("budgetGroup", budgetDetailContract.getBudgetGroup());
		indexObj.put("usingDepartment", budgetDetailContract.getUsingDepartment());
		indexObj.put("createdBy", budgetDetailContract.getCreatedBy());
		indexObj.put("executingDepartment", budgetDetailContract.getExecutingDepartment());
		indexObj.put("fund", budgetDetailContract.getFund());
		indexObj.put("lastModifiedBy", budgetDetailContract.getLastModifiedBy());
		indexObj.put("function", budgetDetailContract.getFunction());
		indexObj.put("scheme", budgetDetailContract.getScheme());
		indexObj.put("subScheme", budgetDetailContract.getSubScheme());
		indexObj.put("functionary", budgetDetailContract.getFunctionary());
		indexObj.put("boundary", budgetDetailContract.getBoundary());
		indexObj.put("anticipatoryAmount", budgetDetailContract.getAnticipatoryAmount());
		indexObj.put("originalAmount", budgetDetailContract.getOriginalAmount());
		indexObj.put("approvedAmount", budgetDetailContract.getApprovedAmount());
		indexObj.put("planningPercent", budgetDetailContract.getPlanningPercent());
		indexObj.put("budgetAvailable", budgetDetailContract.getBudgetAvailable());
		indexObj.put("status", budgetDetailContract.getStatus());
		indexObj.put("documentNumber", budgetDetailContract.getDocumentNumber());
		indexObj.put("uniqueNo", budgetDetailContract.getUniqueNo());
		indexObj.put("materializedPath", budgetDetailContract.getMaterializedPath());

		indexObj.put("tenantId", budgetDetailContract.getTenantId());

		if (budgetDetailContract.getCreatedDate() != null)
			indexObj.put("createdDate", formatter.format(budgetDetailContract.getCreatedDate()));
		else
			indexObj.put("createdDate", null);
		if (budgetDetailContract.getLastModifiedDate() != null)
			indexObj.put("lastModifiedDate", formatter.format(budgetDetailContract.getLastModifiedDate()));
		else
			indexObj.put("lastModifiedDate", null);

		return indexObj;
	}

	private HashMap<String, Object> getBudgetContractIndexObject(final BudgetContract budgetContract) {

		final HashMap<String, Object> indexObj = new HashMap<String, Object>();
		indexObj.put("id", budgetContract.getId());
		indexObj.put("name", budgetContract.getName());
		indexObj.put("financialYear", budgetContract.getFinancialYear());
		indexObj.put("estimationType", budgetContract.getEstimationType());
		indexObj.put("createdBy", budgetContract.getCreatedBy());
		indexObj.put("parent", budgetContract.getParent());
		indexObj.put("primaryBudget", budgetContract.getPrimaryBudget());
		indexObj.put("lastModifiedBy", budgetContract.getLastModifiedBy());
		indexObj.put("active", budgetContract.getActive());
		indexObj.put("referenceBudget", budgetContract.getReferenceBudget());
		indexObj.put("tenantId", budgetContract.getTenantId());
		indexObj.put("documentNumber", budgetContract.getDocumentNumber());
		indexObj.put("status", budgetContract.getStatus());
		indexObj.put("materializedPath", budgetContract.getMaterializedPath());
		indexObj.put("description", budgetContract.getDescription());

		if (budgetContract.getCreatedDate() != null)
			indexObj.put("createdDate", formatter.format(budgetContract.getCreatedDate()));
		else
			indexObj.put("createdDate", null);
		if (budgetContract.getLastModifiedDate() != null)
			indexObj.put("lastModifiedDate", formatter.format(budgetContract.getLastModifiedDate()));
		else
			indexObj.put("lastModifiedDate", null);

		return indexObj;
	}

}
