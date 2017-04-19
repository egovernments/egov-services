package org.egov.persistence.repository;

import org.egov.persistence.entity.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class MessageRepositoryTest {


    private static final String EN_IN = "en_IN";
    private static final String TENANT_ID = "tenant_123";
    private static final String MR_IN = "MR_IN";
    @Mock
    private MessageJpaRepository messageJpaRepository;
    @InjectMocks
    private MessageRepository messageRepository;

    @Test
    public void test_should_call_jpa_repository_toget_message_onthe_basis_of_tenantid_and_locale() {
        List<Message> entityMessages = getEntityMessages();
        when(messageJpaRepository.findByTenantIdAndLocale(TENANT_ID, EN_IN)).thenReturn(entityMessages);
        List<org.egov.domain.model.Message> messages = messageRepository.findByTenantIdAndLocale(TENANT_ID, EN_IN);
        List<org.egov.domain.model.Message>englishMessages= messages.stream().filter(message -> message.getLocale()
          .equals(EN_IN)).collect(Collectors.toList());
        assertThat(englishMessages.size()).isEqualTo(2);
        List<org.egov.domain.model.Message>marathiMessages= messages.stream().filter(message -> message.getLocale()
            .equals(MR_IN)).collect(Collectors.toList());
        assertThat(marathiMessages.size()).isEqualTo(2);
        assertThat(englishMessages.get(0).getCode()).isEqualTo("core.msg.entermobileno");
        assertThat(englishMessages.get(1).getCode()).isEqualTo("core.msg.enterfullname");
        assertThat(marathiMessages.get(0).getCode()).isEqualTo("core.msg.OTPvalidated");
        assertThat(marathiMessages.get(1).getCode()).isEqualTo("core.lbl.imageupload");
    }


    @Test
    public void test_should_call_message_jparepository_to_save_entity_messages()
    {
        List<Message> entityMessages=getEntityMessages();
      List<org.egov.domain.model.Message> modelMessages =  messageRepository.saveAllEntities(entityMessages);
       verify(messageJpaRepository).save(entityMessages);
       assertThat( modelMessages.size()).isEqualTo(4);
    }

    List<Message> getEntityMessages() {

       Message message1=org.egov.persistence.entity.Message .builder().message("OTP यशस्वीपणे प्रमाणित")
           .code("core.msg.OTPvalidated").locale(MR_IN).tenantId(TENANT_ID).build();
       Message message2=org.egov.persistence.entity.Message.builder().message("प्रतिमा यशस्वीरित्या अपलोड")
           .code("core.lbl.imageupload").locale(MR_IN).tenantId(TENANT_ID).build();
       Message message3=org.egov.persistence.entity.Message.builder().message("EnterMobileNumber")
           .code("core.msg.entermobileno").locale(EN_IN).tenantId(TENANT_ID).build();
       Message message4=org.egov.persistence.entity.Message.builder().message("Enter fullname")
           .code("core.msg.enterfullname").locale(EN_IN).tenantId(TENANT_ID).build();
        return (Arrays.asList(message1, message2, message3,message4));

    }


}