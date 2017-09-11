package org.egov.lams.service;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.Notice;
import org.egov.lams.repository.NoticeRepository;
import org.egov.lams.repository.WorkFlowRepository;
import org.egov.lams.web.contract.NoticeRequest;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NoticeServiceTest {

    private NoticeService noticeService;

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private WorkFlowRepository workFlowRepository;

    @Mock
    private AgreementService agreementService;

    @Before
    public void before(){
        noticeService = new NoticeService(noticeRepository,
                agreementService,workFlowRepository);
    }

    @Test
    public void test_should_generate_notice(){

    }

    @Test(expected = RuntimeException.class)
    public void test_to_check_for_exception_if_agreements_list_is_empty(){

        when(agreementService.searchAgreement(getAgreementSearchCriteria(), getRequestInfo())).thenReturn(Collections.EMPTY_LIST);

        noticeService.getAgreementByAuckNumOrAgreementNum(getAgreementSearchCriteria(), getRequestInfo());
    }

    @Test
    public void test_to_get_agreements(){
        Agreement agreement = getAgreement();
        when(agreementService.searchAgreement(any(), any())).thenReturn(Collections.singletonList(agreement));

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
                                    .bankGuaranteeAmount(210112.22)
                                    .councilNumber("454")
                                    .build();

        List<Agreement> agreementList = new ArrayList<>();
        agreementList.add(agreement1);

        return agreementList;
    }

    private Agreement getAgreement(){
        return Agreement.builder()
                .agreementDate(new Date())
                .id(1l)
                .bankGuaranteeAmount(210112.22)
                .councilNumber("454")
                .build();
    }

    private AgreementCriteria getAgreementSearchCriteria(){
        return AgreementCriteria.builder()
                .agreementNumber("LKGHRTF8585SW")
                .acknowledgementNumber("121524")
                .tenantId("kurnool")
                .build();
    }
}