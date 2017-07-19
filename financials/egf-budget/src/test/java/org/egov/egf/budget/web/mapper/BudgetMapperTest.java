package org.egov.egf.budget.web.mapper;

import static org.junit.Assert.assertEquals;

import org.egov.common.domain.model.User;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.domain.model.EgfStatus;
import org.egov.egf.budget.domain.model.EstimationType;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.egov.egf.budget.web.contract.BudgetSearchContract;
import org.egov.egf.master.web.contract.EgfStatusContract;
import org.egov.egf.master.web.contract.FinancialYearContract;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BudgetMapperTest {

	@InjectMocks
	private BudgetMapper budgetMapper;

	@Before
	public void setup() {
		budgetMapper = new BudgetMapper();
	}

	@Test
	public void test_to_domain() {

		Budget expectedDomain = budgetMapper.toDomain(contract());

		assertEquals(expectedDomain, domain());

	}

	@Test
	public void test_to_contract() {

		BudgetContract expectedContract = budgetMapper.toContract(domain());

		assertEquals(expectedContract.getId(), contract().getId());
		assertEquals(expectedContract.getName(), contract().getName());
		assertEquals(expectedContract.getFinancialYear().getId(), contract().getFinancialYear().getId());
		assertEquals(expectedContract.getEstimationType(), contract().getEstimationType());
		assertEquals(expectedContract.getParent().getId(), contract().getParent().getId());
		assertEquals(expectedContract.getReferenceBudget().getId(), contract().getReferenceBudget().getId());
		assertEquals(expectedContract.getDescription(), contract().getDescription());
		assertEquals(expectedContract.getActive(), contract().getActive());
		assertEquals(expectedContract.getPrimaryBudget(), contract().getPrimaryBudget());
		assertEquals(expectedContract.getMaterializedPath(), contract().getMaterializedPath());
		assertEquals(expectedContract.getDocumentNumber(), contract().getDocumentNumber());
		assertEquals(expectedContract.getStatus().getId(), contract().getStatus().getId());
		assertEquals(expectedContract.getCreatedBy().getId(), contract().getCreatedBy().getId());
		assertEquals(expectedContract.getLastModifiedBy().getId(), contract().getLastModifiedBy().getId());
		assertEquals(expectedContract.getTenantId(), contract().getTenantId());

	}

	@Test
	public void test_to_search_domain() {

		BudgetSearch expectedSearchDomain = budgetMapper.toSearchDomain(searchContract());

		assertEquals(expectedSearchDomain, searchDomain());

	}

	@Test
	public void test_to_search_contract() {

		BudgetSearchContract expectedSearchContract = budgetMapper.toSearchContract(searchDomain());

		assertEquals(expectedSearchContract.getId(), searchContract().getId());
		assertEquals(expectedSearchContract.getName(), searchContract().getName());
		assertEquals(expectedSearchContract.getFinancialYear().getId(), searchContract().getFinancialYear().getId());
		assertEquals(expectedSearchContract.getEstimationType(), searchContract().getEstimationType());
		assertEquals(expectedSearchContract.getParent().getId(), searchContract().getParent().getId());
		assertEquals(expectedSearchContract.getReferenceBudget().getId(), searchContract().getReferenceBudget().getId());
		assertEquals(expectedSearchContract.getDescription(), searchContract().getDescription());
		assertEquals(expectedSearchContract.getActive(), searchContract().getActive());
		assertEquals(expectedSearchContract.getPrimaryBudget(), searchContract().getPrimaryBudget());
		assertEquals(expectedSearchContract.getMaterializedPath(), searchContract().getMaterializedPath());
		assertEquals(expectedSearchContract.getDocumentNumber(), searchContract().getDocumentNumber());
		assertEquals(expectedSearchContract.getStatus().getId(), searchContract().getStatus().getId());
		assertEquals(expectedSearchContract.getCreatedBy().getId(), searchContract().getCreatedBy().getId());
		assertEquals(expectedSearchContract.getLastModifiedBy().getId(), searchContract().getLastModifiedBy().getId());
		assertEquals(expectedSearchContract.getTenantId(), searchContract().getTenantId());
		assertEquals(expectedSearchContract.getPageSize(), searchContract().getPageSize());
		assertEquals(expectedSearchContract.getOffset(), searchContract().getOffset());
		

	}

	public Budget domain() {

		Budget budget = new Budget();

		budget.setId("id");
		budget.setName("name");
		budget.setFinancialYear(FinancialYearContract.builder().id("id").build());
		budget.setEstimationType(EstimationType.BE);
		budget.setParent(Budget.builder().id("parent").build());
		budget.setDescription("description");
		budget.setActive(true);
		budget.setPrimaryBudget(true);
		budget.setMaterializedPath("materializedPath");
		budget.setReferenceBudget(Budget.builder().id("referenceBudget").build());
		budget.setDocumentNumber("documentNumber");
		budget.setStatus(EgfStatus.builder().id("status").code("code").description("description")
				.moduleType("moduleType").build());
		budget.setCreatedBy(User.builder().id("user").build());
		budget.setLastModifiedBy(User.builder().id("user").build());
		budget.setTenantId("tenantId");

		return budget;
	}

	public BudgetContract contract() {

		BudgetContract contract = new BudgetContract();

		contract.setId("id");
		contract.setName("name");
		contract.setFinancialYear(FinancialYearContract.builder().id("id").build());
		contract.setEstimationType(EstimationType.BE);
		contract.setParent(BudgetContract.builder().id("parent").build());
		contract.setDescription("description");
		contract.setActive(true);
		contract.setPrimaryBudget(true);
		contract.setMaterializedPath("materializedPath");
		contract.setReferenceBudget(BudgetContract.builder().id("referenceBudget").build());
		contract.setDocumentNumber("documentNumber");
		contract.setStatus(EgfStatusContract.builder().id("status").code("code").description("description")
				.moduleType("moduleType").build());
		contract.setCreatedBy(User.builder().id("user").build());
		contract.setLastModifiedBy(User.builder().id("user").build());
		contract.setTenantId("tenantId");

		return contract;
	}

	public BudgetSearch searchDomain() {

		BudgetSearch budgetSearch = new BudgetSearch();

		budgetSearch.setId("id");
		budgetSearch.setName("name");
		budgetSearch.setFinancialYear(FinancialYearContract.builder().id("id").build());
		budgetSearch.setEstimationType(EstimationType.BE);
		budgetSearch.setParent(Budget.builder().id("parent").build());
		budgetSearch.setDescription("description");
		budgetSearch.setActive(true);
		budgetSearch.setPrimaryBudget(true);
		budgetSearch.setMaterializedPath("materializedPath");
		budgetSearch.setReferenceBudget(Budget.builder().id("referenceBudget").build());
		budgetSearch.setDocumentNumber("documentNumber");
		budgetSearch.setStatus(EgfStatus.builder().id("status").build());
		budgetSearch.setCreatedBy(User.builder().id("user").build());
		budgetSearch.setLastModifiedBy(User.builder().id("user").build());
		budgetSearch.setTenantId("tenantId");
		budgetSearch.setPageSize(1);
		budgetSearch.setOffset(1);

		return budgetSearch;
	}

	public BudgetSearchContract searchContract() {

		BudgetSearchContract contract = new BudgetSearchContract();

		contract.setId("id");
		contract.setName("name");
		contract.setFinancialYear(FinancialYearContract.builder().id("id").build());
		contract.setEstimationType(EstimationType.BE);
		contract.setParent(BudgetContract.builder().id("parent").build());
		contract.setDescription("description");
		contract.setActive(true);
		contract.setPrimaryBudget(true);
		contract.setMaterializedPath("materializedPath");
		contract.setReferenceBudget(BudgetContract.builder().id("referenceBudget").build());
		contract.setDocumentNumber("documentNumber");
		contract.setStatus(EgfStatusContract.builder().id("status").build());
		contract.setCreatedBy(User.builder().id("user").build());
		contract.setLastModifiedBy(User.builder().id("user").build());
		contract.setTenantId("tenantId");
		contract.setPageSize(1);
		contract.setOffset(1);

		return contract;
	}

}
