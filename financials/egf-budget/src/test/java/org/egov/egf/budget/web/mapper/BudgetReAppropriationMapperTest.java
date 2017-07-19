package org.egov.egf.budget.web.mapper;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.egov.common.domain.model.User;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.domain.model.EgfStatus;
import org.egov.egf.budget.web.contract.BudgetDetailContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationSearchContract;
import org.egov.egf.master.web.contract.EgfStatusContract;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BudgetReAppropriationMapperTest {

	@InjectMocks
	private BudgetReAppropriationMapper budgetDetailMapper;

	@Before
	public void setup() {
		budgetDetailMapper = new BudgetReAppropriationMapper();
	}

	@Test
	public void test_to_domain() {

		BudgetReAppropriation expectedDomain = budgetDetailMapper.toDomain(contract());

		assertEquals(expectedDomain.getId(), domain().getId());
		assertEquals(expectedDomain.getBudgetDetail().getId(), domain().getBudgetDetail().getId());
		assertEquals(expectedDomain.getAdditionAmount(), domain().getAdditionAmount());
		assertEquals(expectedDomain.getDeductionAmount(), domain().getDeductionAmount());
		assertEquals(expectedDomain.getOriginalAdditionAmount(), domain().getOriginalAdditionAmount());
		assertEquals(expectedDomain.getOriginalDeductionAmount(), domain().getOriginalDeductionAmount());
		assertEquals(expectedDomain.getAnticipatoryAmount(), domain().getAnticipatoryAmount());
		assertEquals(expectedDomain.getStatus().getId(), domain().getStatus().getId());
		assertEquals(expectedDomain.getCreatedBy().getId(), domain().getCreatedBy().getId());
		assertEquals(expectedDomain.getLastModifiedBy().getId(), domain().getLastModifiedBy().getId());
		assertEquals(expectedDomain.getTenantId(), domain().getTenantId());

	}

	@Test
	public void test_to_contract() {

		BudgetReAppropriationContract expectedContract = budgetDetailMapper.toContract(domain());

		assertEquals(expectedContract.getId(), contract().getId());
		assertEquals(expectedContract.getBudgetDetail().getId(), contract().getBudgetDetail().getId());
		assertEquals(expectedContract.getAdditionAmount(), contract().getAdditionAmount());
		assertEquals(expectedContract.getDeductionAmount(), contract().getDeductionAmount());
		assertEquals(expectedContract.getOriginalAdditionAmount(), contract().getOriginalAdditionAmount());
		assertEquals(expectedContract.getOriginalDeductionAmount(), contract().getOriginalDeductionAmount());
		assertEquals(expectedContract.getAnticipatoryAmount(), contract().getAnticipatoryAmount());
		assertEquals(expectedContract.getStatus().getId(), contract().getStatus().getId());
		assertEquals(expectedContract.getCreatedBy().getId(), contract().getCreatedBy().getId());
		assertEquals(expectedContract.getLastModifiedBy().getId(), contract().getLastModifiedBy().getId());
		assertEquals(expectedContract.getTenantId(), contract().getTenantId());

	}

	@Test
	public void test_to_search_domain() {

		BudgetReAppropriationSearch expectedSearchDomain = budgetDetailMapper.toSearchDomain(searchContract());

		assertEquals(expectedSearchDomain.getId(), searchDomain().getId());
		assertEquals(expectedSearchDomain.getBudgetDetail().getId(), searchDomain().getBudgetDetail().getId());
		assertEquals(expectedSearchDomain.getAdditionAmount(), searchDomain().getAdditionAmount());
		assertEquals(expectedSearchDomain.getDeductionAmount(), searchDomain().getDeductionAmount());
		assertEquals(expectedSearchDomain.getOriginalAdditionAmount(), searchDomain().getOriginalAdditionAmount());
		assertEquals(expectedSearchDomain.getOriginalDeductionAmount(), searchDomain().getOriginalDeductionAmount());
		assertEquals(expectedSearchDomain.getAnticipatoryAmount(), searchDomain().getAnticipatoryAmount());
		assertEquals(expectedSearchDomain.getStatus().getId(), searchDomain().getStatus().getId());
		assertEquals(expectedSearchDomain.getCreatedBy().getId(), searchDomain().getCreatedBy().getId());
		assertEquals(expectedSearchDomain.getLastModifiedBy().getId(), searchDomain().getLastModifiedBy().getId());
		assertEquals(expectedSearchDomain.getTenantId(), searchDomain().getTenantId());

	}

	@Test
	public void test_to_search_contract() {

		BudgetReAppropriationSearchContract expectedSearchContract = budgetDetailMapper
				.toSearchContract(searchDomain());

		assertEquals(expectedSearchContract.getId(), searchContract().getId());
		assertEquals(expectedSearchContract.getBudgetDetail().getId(), searchContract().getBudgetDetail().getId());
		assertEquals(expectedSearchContract.getAdditionAmount(), searchContract().getAdditionAmount());
		assertEquals(expectedSearchContract.getDeductionAmount(), searchContract().getDeductionAmount());
		assertEquals(expectedSearchContract.getOriginalAdditionAmount(), searchContract().getOriginalAdditionAmount());
		assertEquals(expectedSearchContract.getOriginalDeductionAmount(),
				searchContract().getOriginalDeductionAmount());
		assertEquals(expectedSearchContract.getAnticipatoryAmount(), searchContract().getAnticipatoryAmount());
		assertEquals(expectedSearchContract.getStatus().getId(), searchContract().getStatus().getId());
		assertEquals(expectedSearchContract.getCreatedBy().getId(), searchContract().getCreatedBy().getId());
		assertEquals(expectedSearchContract.getLastModifiedBy().getId(), searchContract().getLastModifiedBy().getId());
		assertEquals(expectedSearchContract.getTenantId(), searchContract().getTenantId());

	}

	public BudgetReAppropriation domain() {

		BudgetReAppropriation budgetReAppropriation = new BudgetReAppropriation();

		budgetReAppropriation.setId("id");
		budgetReAppropriation.setBudgetDetail(BudgetDetail.builder().id("id").build());
		budgetReAppropriation.setAdditionAmount(BigDecimal.ONE);
		budgetReAppropriation.setDeductionAmount(BigDecimal.ONE);
		budgetReAppropriation.setOriginalAdditionAmount(BigDecimal.ONE);
		budgetReAppropriation.setOriginalDeductionAmount(BigDecimal.ONE);
		budgetReAppropriation.setAnticipatoryAmount(BigDecimal.ONE);
		budgetReAppropriation.setStatus(EgfStatus.builder().id("id").build());
		budgetReAppropriation.setCreatedBy(User.builder().id("id").build());
		budgetReAppropriation.setLastModifiedBy(User.builder().id("id").build());
		budgetReAppropriation.setTenantId("tenantId");

		return budgetReAppropriation;
	}

	public BudgetReAppropriationContract contract() {

		BudgetReAppropriationContract contract = new BudgetReAppropriationContract();

		contract.setId("id");
		contract.setBudgetDetail(BudgetDetailContract.builder().id("id").build());
		contract.setAdditionAmount(BigDecimal.ONE);
		contract.setDeductionAmount(BigDecimal.ONE);
		contract.setOriginalAdditionAmount(BigDecimal.ONE);
		contract.setOriginalDeductionAmount(BigDecimal.ONE);
		contract.setAnticipatoryAmount(BigDecimal.ONE);
		contract.setStatus(EgfStatusContract.builder().id("id").build());
		contract.setCreatedBy(User.builder().id("id").build());
		contract.setLastModifiedBy(User.builder().id("id").build());
		contract.setTenantId("tenantId");

		return contract;
	}

	public BudgetReAppropriationSearch searchDomain() {

		BudgetReAppropriationSearch budgetReAppropriationSearch = new BudgetReAppropriationSearch();

		budgetReAppropriationSearch.setId("id");
		budgetReAppropriationSearch.setBudgetDetail(BudgetDetail.builder().id("id").build());
		budgetReAppropriationSearch.setAdditionAmount(BigDecimal.ONE);
		budgetReAppropriationSearch.setDeductionAmount(BigDecimal.ONE);
		budgetReAppropriationSearch.setOriginalAdditionAmount(BigDecimal.ONE);
		budgetReAppropriationSearch.setOriginalDeductionAmount(BigDecimal.ONE);
		budgetReAppropriationSearch.setAnticipatoryAmount(BigDecimal.ONE);
		budgetReAppropriationSearch.setStatus(EgfStatus.builder().id("id").build());
		budgetReAppropriationSearch.setCreatedBy(User.builder().id("id").build());
		budgetReAppropriationSearch.setLastModifiedBy(User.builder().id("id").build());
		budgetReAppropriationSearch.setTenantId("tenantId");
		budgetReAppropriationSearch.setPageSize(1);
		budgetReAppropriationSearch.setOffset(1);

		return budgetReAppropriationSearch;
	}

	public BudgetReAppropriationSearchContract searchContract() {

		BudgetReAppropriationSearchContract contract = new BudgetReAppropriationSearchContract();

		contract.setId("id");
		contract.setBudgetDetail(BudgetDetailContract.builder().id("id").build());
		contract.setAdditionAmount(BigDecimal.ONE);
		contract.setDeductionAmount(BigDecimal.ONE);
		contract.setOriginalAdditionAmount(BigDecimal.ONE);
		contract.setOriginalDeductionAmount(BigDecimal.ONE);
		contract.setAnticipatoryAmount(BigDecimal.ONE);
		contract.setStatus(EgfStatusContract.builder().id("id").build());
		contract.setCreatedBy(User.builder().id("id").build());
		contract.setLastModifiedBy(User.builder().id("id").build());
		contract.setTenantId("tenantId");
		contract.setPageSize(1);
		contract.setOffset(1);

		return contract;
	}

}
