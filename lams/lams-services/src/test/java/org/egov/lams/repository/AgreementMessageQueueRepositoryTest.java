package org.egov.lams.repository;

import org.egov.lams.model.Agreement;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.User;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AgreementMessageQueueRepositoryTest {

	public static final String START_WORKFLOW = "START_WORKFLOW";
	public static final String UPDATE_WORKFLOW = "UPDATE_WORKFLOW";
	public static final String SAVE = "SAVE";
	public static final String UPDATE = "UPDATE";

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    private AgreementMessageQueueRepository agreementMessageQueueRepository;

    @Before
    public void before(){
		agreementMessageQueueRepository = new AgreementMessageQueueRepository(kafkaTemplate, START_WORKFLOW,
				UPDATE_WORKFLOW, SAVE, UPDATE);
    }

    @Test
    public void test_to_send_message_to_Kafka(){

        agreementMessageQueueRepository.save(getAgreementRequest(), UPDATE);

        verify(kafkaTemplate).send(any(), any(), any());
    }

    private AgreementRequest getAgreementRequest(){
        return AgreementRequest.builder()
                .agreement(getAgreement())
                .requestInfo(getRequestInfo())
                .build();
    }

    private Agreement getAgreement(){
        return Agreement.builder()
                .agreementDate(new Date())
                .id(1l)
                .bankGuaranteeAmount(210112.22)
                .councilNumber("454")
                .build();
    }

    private RequestInfo getRequestInfo(){
        User user = User.builder()
                .type("EMPLOYEE")
                .name("test")
                .userName("test")
                .id(1l)
                .build();

        return RequestInfo.builder()
                .userInfo(user)
                .build();
    }
}