package org.egov.asset.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.DisposalCriteria;
import org.egov.asset.model.enums.TransactionType;
import org.egov.asset.repository.builder.DisposalQueryBuilder;
import org.egov.asset.repository.rowmapper.DisposalRowMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class DisposalRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private DisposalQueryBuilder disposalQueryBuilder;

    @Mock
    private DisposalRowMapper disposalRowMapper;

    @InjectMocks
    private DisposalRepository disposalRepository;

    @Test
    public void test_should_search_Disposals() {

        final List<Disposal> disposals = new ArrayList<>();
        final Disposal disposal = getDisposalFromDB();
        disposals.add(disposal);
        when(disposalQueryBuilder.getQuery(any(DisposalCriteria.class), any(List.class))).thenReturn(StringUtils.EMPTY);
        when(jdbcTemplate.query(any(String.class), any(Object[].class), any(DisposalRowMapper.class)))
                .thenReturn(disposals);
        assertTrue(disposals.equals(disposalRepository.search(new DisposalCriteria())));
    }

    @Test
    public void testCreateDisposal() {
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        final Disposal disposal = getDisposalFromDB();
        final DisposalRequest disposalRequest = new DisposalRequest();
        disposalRequest.setRequestInfo(requestInfo);
        disposalRequest.setDisposal(disposal);
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        disposalRepository.create(disposalRequest);
    }

    private Disposal getDisposalFromDB() {

        final Disposal disposal = new Disposal();
        disposal.setTenantId("ap.kurnool");
        disposal.setId(Long.valueOf("15"));
        disposal.setAssetId(Long.valueOf("31"));
        disposal.setBuyerName("Abhi");
        disposal.setBuyerAddress("Bangalore");
        disposal.setDisposalReason("disposalReason");
        disposal.setDisposalDate(Long.valueOf("1496564536178"));
        disposal.setPanCardNumber("baq1234567");
        disposal.setAadharCardNumber("12345678123456");
        disposal.setAssetCurrentValue(new BigDecimal("100.0"));
        disposal.setSaleValue(new BigDecimal("200.0"));
        disposal.setTransactionType(TransactionType.SALE);
        disposal.setAssetSaleAccount(Long.valueOf("15"));

        final AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("2");
        auditDetails.setCreatedDate(Long.valueOf("1496746205544"));
        auditDetails.setLastModifiedBy("2");
        auditDetails.setLastModifiedDate(Long.valueOf("1496746205544"));
        disposal.setAuditDetails(auditDetails);

        return disposal;
    }
}
