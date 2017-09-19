package org.egov.lams.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AgreementCriteriaTest {

    private AgreementCriteria agreementCriteria;

    @Before
    public void setup(){
        agreementCriteria = getAgreementCriteria();
    }

    @Test
    public void test_for_is_agreement_not_null(){
        boolean isAgreementNull = agreementCriteria.isAgreementEmpty();

        assertEquals(isAgreementNull, false);
    }

    @Test
    public void test_for_is_agreement_null(){
        agreementCriteria = getEmptyAgreementCriteria();

        boolean isAgreementNull = agreementCriteria.isAgreementEmpty();

        assertEquals(isAgreementNull, true);
    }

    @Test
    public void test_if_allottee_not_present(){
        agreementCriteria = getEmptyAgreementCriteria();

        boolean isAllootteePresent = agreementCriteria.isAllotteeEmpty();

        assertEquals(isAllootteePresent, true);
    }

    @Test
    public void test_if_allottee_present(){
        boolean isAllootteePresent = agreementCriteria.isAllotteeEmpty();

        assertEquals(isAllootteePresent, false);
    }

    @Test
    public void test_for_asset_not_empty(){
        boolean isAssetNull = agreementCriteria.isAssetEmpty();

        assertEquals(isAssetNull, false);
    }

    @Test
    public void test_to_check_for_empty_asset(){
        agreementCriteria = getEmptyAgreementCriteria();

        boolean isAssetNull = agreementCriteria.isAssetEmpty();

        assertEquals(isAssetNull, true);
    }

    private AgreementCriteria getEmptyAgreementCriteria(){
        return AgreementCriteria.builder()
                .build();
    }

    private AgreementCriteria getAgreementCriteria(){
        return AgreementCriteria.builder()
                .tenantId("default")
                .agreementNumber("111")
                .allotteeName("Ravi")
                .mobileNumber(6525325689L)
                .assetCode("AST001")
                .build();
    }
}