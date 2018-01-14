package org.egov.lams.service;

import org.egov.lams.model.*;
import org.egov.lams.model.enums.Action;
import org.egov.lams.model.enums.Status;
import org.egov.lams.repository.NoticeRepository;
import org.egov.lams.repository.WorkFlowRepository;
import org.egov.lams.web.contract.NoticeRequest;
import org.egov.lams.web.contract.NoticeResponse;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private WorkFlowRepository workFlowRepository;

    @Mock
    private AgreementService agreementService;

    private NoticeService noticeService;

    @Before
    public void before(){
        noticeService = new NoticeService(noticeRepository,
                agreementService,workFlowRepository);
    }

    @Test
    public void test_should_generate_notice(){
        when(workFlowRepository.getCommissionerName(any(),any(),any())).thenReturn("Narasimha Rao");
        when(noticeRepository.createNotice(any())).thenReturn(getNotice());
        when(agreementService.getAgreementsByAgreementNumber(any(),any())).thenReturn(getAgreementsList());

        NoticeResponse notices = noticeService.generateNotice(getNoticeRequest());

        assertEquals(1, notices.getNotices().size());
        assertEquals("LAAWERD093039",notices.getNotices().get(0).getAcknowledgementNumber());
    }

    @Test(expected = RuntimeException.class)
    public void test_to_check_for_exception_if_agreements_list_is_empty(){
        when(agreementService.searchAgreement(getAgreementSearchCriteria(), getRequestInfo())).thenReturn(Collections.EMPTY_LIST);

        noticeService.getAgreementByAuckNumOrAgreementNum(getAgreementSearchCriteria(), getRequestInfo());
    }

    @Test
    public void test_to_get_agreements(){
        Agreement agreement = getAgreement();
        when(agreementService.getAgreementsByAgreementNumber(any(), any())).thenReturn(Collections.singletonList(agreement));

        List<Agreement> agreementsList = noticeService.getAgreementByAuckNumOrAgreementNum(getAgreementSearchCriteria(), getRequestInfo());

        assertEquals("454", agreementsList.get(0).getCouncilNumber());
    }

    private NoticeRequest getNoticeRequest(){
        return NoticeRequest.builder()
                .requestInfo(getRequestInfo())
                .notice(getNotice())
                .build();
    }

    private Notice getNotice(){
        return Notice.builder()
                .acknowledgementNumber("LAAWERD093039")
                .agreementPeriod(1l)
                .allotteeMobileNumber("5625145854")
                .allotteeName("Rooney")
                .assetCategory(1l)
                .assetNo(2l)
                .commencementDate(new Date())
                .doorNo("4545")
                .tenantId("kurnool")
                .noticeType("RENEWAL")
                .build();
    }

    private RequestInfo getRequestInfo(){
        User user = User.builder()
                .type("EMPLOYEE")
                .name("Rooney")
                .userName("wazza")
                .id(1l)
                .build();

        return RequestInfo.builder()
                .userInfo(user)
                .build();
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
                                    .action(Action.CREATE)
                                    .build();

        List<Agreement> agreementList = new ArrayList<>();
        agreementList.add(agreement1);

        return agreementList;
    }

    private Agreement getAgreement(){
        return Agreement.builder()
                .agreementDate(new Date())
                .id(1l)
                .allottee(getAllottee())
                .asset(getAsset())
                .bankGuaranteeAmount(210112.22)
                .councilNumber("454")
                .build();
    }

    private AgreementCriteria getAgreementSearchCriteria(){
        return AgreementCriteria.builder()
                .agreementNumber("LKGHRTF8585SW")
                .acknowledgementNumber("121524")
                .tenantId("kurnool")
                .status(Status.ACTIVE)
                .build();
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