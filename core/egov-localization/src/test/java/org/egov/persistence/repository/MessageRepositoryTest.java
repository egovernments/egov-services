package org.egov.persistence.repository;

import org.egov.domain.model.Tenant;
import org.egov.persistence.entity.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class MessageRepositoryTest {

    private static final String TENANT_ID = "tenant_123";
    private static final String MR_IN = "MR_IN";

    @Mock
    private MessageJpaRepository messageJpaRepository;

    @InjectMocks
    private MessageRepository messageRepository;

    @Test
    public void test_should_save_messages() {
        List<org.egov.domain.model.Message> domainMessages = getDomainMessages();

        messageRepository.save(domainMessages);

        verify(messageJpaRepository).save(anyListOf(Message.class));
    }

    List<org.egov.domain.model.Message> getDomainMessages() {
        org.egov.domain.model.Message message1 = org.egov.domain.model.Message.builder()
            .message("OTP यशस्वीपणे प्रमाणित")
            .code("core.msg.OTPvalidated")
            .locale(MR_IN)
            .tenant(new Tenant(TENANT_ID))
            .build();
        org.egov.domain.model.Message message2 = org.egov.domain.model.Message.builder()
            .message("प्रतिमा यशस्वीरित्या अपलोड")
            .code("core.lbl.imageupload")
            .locale(MR_IN)
            .tenant(new Tenant(TENANT_ID))
            .build();
        return Arrays.asList(message1, message2);
    }


}