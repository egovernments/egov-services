package org.egov.lams.service;

import org.egov.lams.model.*;
import org.egov.lams.repository.AgreementRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AgreementServiceTest {

    @InjectMocks
    private AgreementService agreementService;

    @Mock
    private AgreementRepository agreementRepository;

    @Test
    public void test_to_check_if_agreement_exists(){
        when(agreementRepository.isAgreementExist("AAA")).thenReturn(true);

        agreementRepository.isAgreementExist("AAA");

        verify(agreementRepository).isAgreementExist("AAA");
    }

    @Test
    public void test_to_fetch_agreements_for_given_asset_id(){
        when(agreementRepository.getAgreementForCriteria(any())).thenReturn(getAgreementsList());

        List<Agreement> agreements =  agreementService.getAgreementsForAssetId(1l);

    }

    private List<Agreement> getAgreementsList(){
        Agreement agreement1 = Agreement.builder()
                .agreementDate(new Date())
                .id(1l)
                .asset(getAsset())
                .allottee(getAllottee())
                .bankGuaranteeAmount(210112.22)
                .councilNumber("454")
                .rent(2000d)
                .securityDeposit(2000d)
                .build();

        List<Agreement> agreementList = new ArrayList<>();
        agreementList.add(agreement1);

        return agreementList;
    }

    private Allottee getAllottee(){
        return Allottee.builder()
                .aadhaarNumber("1998-2222-9089")
                .address("Near central Road")
                .name("Rooney")
                .mobileNumber("9585754586")
                .build();
    }

    private Asset getAsset(){
        return Asset.builder()
                .name("Fish Tank")
                .code("LLN")
                .category(getCategory())
                .id(1l)
                .locationDetails(getLocation())
                .build();
    }

    private AssetCategory getCategory(){
        return AssetCategory.builder()
                .code("234")
                .id(3l)
                .name("Governament Land")
                .build();
    }

    private Location getLocation(){
        return Location.builder()
                .block(2l)
                .doorNo("5454")
                .electionWard(3l)
                .street(5l)
                .zone(6l)
                .pinCode(542121542l)
                .revenueWard(4l)
                .build();
    }
}