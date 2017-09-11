package org.egov.egf.voucher.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.egov.egf.voucher.domain.model.VoucherSubType;
import org.egov.egf.voucher.domain.model.VoucherSubTypeSearch;
import org.egov.egf.voucher.domain.model.VoucherType;
import org.egov.egf.voucher.persistence.entity.VoucherSubTypeEntity;
import org.egov.egf.voucher.persistence.queue.repository.VoucherSubTypeQueueRepository;
import org.egov.egf.voucher.persistence.repository.VoucherSubTypeJdbcRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VoucherSubTypeRepositoryTest {

	private VoucherSubTypeRepository voucherSubTypeRepositoryWithKafka;

	private VoucherSubTypeRepository voucherSubTypeRepositoryWithOutKafka;
	@Mock
	private VoucherSubTypeJdbcRepository voucherSubTypeJdbcRepository;

	@Mock
	private VoucherSubTypeQueueRepository voucherSubTypeQueueRepository;

	@Mock
	private FinancialConfigurationContractRepository financialConfigurationContractRepository;

	@Mock
	private VoucherSubTypeESRepository voucherSubTypeESRepository;

	@Captor
	private ArgumentCaptor<List<VoucherSubType>> captor;

	private RequestInfo requestInfo = new RequestInfo();

	@Before
	public void setup() {
		voucherSubTypeRepositoryWithKafka = new VoucherSubTypeRepository(
				voucherSubTypeJdbcRepository,
				financialConfigurationContractRepository,
				voucherSubTypeESRepository, voucherSubTypeQueueRepository,
				"yes");

		voucherSubTypeRepositoryWithOutKafka = new VoucherSubTypeRepository(
				voucherSubTypeJdbcRepository,
				financialConfigurationContractRepository,
				voucherSubTypeESRepository, voucherSubTypeQueueRepository, "no");
	}
	
	 	@Test
	    public void test_save_with_kafka() {

	        List<VoucherSubType> expectedResult = getVoucherSubTypes();
	        
	        VoucherSubTypeEntity entity = new VoucherSubTypeEntity().toEntity(expectedResult.get(0));

	        when(voucherSubTypeJdbcRepository.create(any(VoucherSubTypeEntity.class))).thenReturn(entity);
	        requestInfo.setAction("create");
	        voucherSubTypeRepositoryWithKafka.save(expectedResult, requestInfo);

	        verify(voucherSubTypeQueueRepository).addToQue(any(Map.class));
	        
	    }

	    @Test
	    public void test_save_with_out_kafka() {

	        List<VoucherSubType> expectedResult = getVoucherSubTypes();

	        VoucherSubTypeEntity entity = new VoucherSubTypeEntity().toEntity(expectedResult.get(0));

	        when(voucherSubTypeJdbcRepository.create(any(VoucherSubTypeEntity.class))).thenReturn(entity);

	        voucherSubTypeRepositoryWithOutKafka.save(expectedResult, requestInfo);

	        verify(voucherSubTypeQueueRepository).addToSearchQue(any(Map.class));
	    }

	    @Test
	    public void test_update_with_kafka() {

	        List<VoucherSubType> expectedResult = getVoucherSubTypes();

	        requestInfo.setAction("update");
	        voucherSubTypeRepositoryWithKafka.update(expectedResult, requestInfo);

	        verify(voucherSubTypeQueueRepository).addToQue(any(Map.class));

	    }
	    
	    @Test
	    public void test_update_with_out_kafka() {

	        List<VoucherSubType> expectedResult = getVoucherSubTypes();

	        VoucherSubTypeEntity entity = new VoucherSubTypeEntity().toEntity(expectedResult.get(0));

	        when(voucherSubTypeJdbcRepository.update(any(VoucherSubTypeEntity.class))).thenReturn(entity);

	        voucherSubTypeRepositoryWithOutKafka.update(expectedResult, requestInfo);

	        verify(voucherSubTypeQueueRepository).addToSearchQue(any(Map.class));

	    }
	    
	    @Test
	    public void test_save() {

	    	VoucherSubTypeEntity entity = getVoucherSubTypeEntity();
	    	VoucherSubType expectedResult = entity.toDomain();

	        when(voucherSubTypeJdbcRepository.create(any(VoucherSubTypeEntity.class))).thenReturn(entity);

	        VoucherSubType actualResult = voucherSubTypeRepositoryWithKafka.save(getVoucherSubTypeDomin());

	        assertEquals(expectedResult.getId(), actualResult.getId());
	        assertEquals(expectedResult.getVoucherName(), actualResult.getVoucherName());
	        assertEquals(expectedResult.getVoucherType(), actualResult.getVoucherType());
	    }
	    
	    @Test
	    public void test_search() {

	        Pagination<VoucherSubType> expectedResult = new Pagination<>();
	        expectedResult.setPageSize(500);
	        expectedResult.setOffset(0);

	        when(financialConfigurationContractRepository.fetchDataFrom()).thenReturn("db");
	        when(voucherSubTypeJdbcRepository.search(any(VoucherSubTypeSearch.class))).thenReturn(expectedResult);

	        Pagination<VoucherSubType> actualResult = voucherSubTypeRepositoryWithKafka.search(getVoucherSubTypeSearch());

	        assertEquals(expectedResult, actualResult);

	    }
	    
	    private VoucherSubTypeSearch getVoucherSubTypeSearch() {
	    	VoucherSubTypeSearch voucherSubTypeSearch = new VoucherSubTypeSearch();
	    	voucherSubTypeSearch.setPageSize(500);
	    	voucherSubTypeSearch.setOffset(0);
	        return voucherSubTypeSearch;

	    }
	    
	    private VoucherSubType getVoucherSubTypeDomin() {
	    	VoucherSubType voucherSubTypeDetail = new VoucherSubType();
	    	voucherSubTypeDetail.setId("b96561462fdc484fa97fa72c3944ad89");
	    	voucherSubTypeDetail.setVoucherName("BankToBank");
	    	voucherSubTypeDetail.setVoucherType(VoucherType.STANDARD_VOUCHER_TYPE_CONTRA);
	    	voucherSubTypeDetail.setTenantId("default");
	        return voucherSubTypeDetail;
	    }
	    
	    private VoucherSubTypeEntity getVoucherSubTypeEntity() {
	    	VoucherSubTypeEntity entity = new VoucherSubTypeEntity();
	        entity.setId("");
	        entity.setVoucherName("BankToBank");
	        entity.setVoucherType(VoucherType.STANDARD_VOUCHER_TYPE_CONTRA.toString());
	        entity.setTenantId("default");
	        return entity;
	    }
		
		private List<VoucherSubType> getVoucherSubTypes() {

			List<VoucherSubType> voucherSubTypes = new ArrayList<VoucherSubType>();
			
			VoucherSubType voucherSubType = VoucherSubType.builder().id("b96561462fdc484fa97fa72c3944ad89")
					.voucherType(VoucherType.STANDARD_VOUCHER_TYPE_CONTRA)
					.voucherName("BankToBank").exclude(true).build();
			voucherSubType.setTenantId("default");
			
			voucherSubTypes.add(voucherSubType);
			return voucherSubTypes;
		}

}
