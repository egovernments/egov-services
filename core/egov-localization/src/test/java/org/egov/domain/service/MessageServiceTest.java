package org.egov.domain.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.domain.model.Message;

import org.egov.persistence.repository.MessageRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {
    private static final String EN_IN = "en_IN";
    private static final String TENANT_ID = "tenant_123";
    private static final String MR_IN = "mr_IN";

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;


    @Test
    public void test_should_get_all_messages_when_locale_is_other_than_english() {

        List<Message> englishModelMessages = getEnglishModelMessages();
        List<Message> mararthiModelMessages = getMarathiModelMessages();

        when(messageRepository.findByTenantIdAndLocale(TENANT_ID, EN_IN)).thenReturn(englishModelMessages);

        when(messageRepository.findByTenantIdAndLocale(TENANT_ID, MR_IN)).thenReturn(mararthiModelMessages);

        List<Message> messagesAsPerLocale = messageService.getMessagesAsPerLocale(MR_IN, TENANT_ID);
        List<Message> localMessages= messagesAsPerLocale.stream().filter(message->message.getLocale().equals(MR_IN)).collect(Collectors.toList());
        List<Message> englishMessages=messagesAsPerLocale.stream().filter(message->message.getLocale().equals(EN_IN)).collect(Collectors.toList());
        assertThat(messagesAsPerLocale.size()).isEqualTo(5);
        assertThat(localMessages.size()).isEqualTo(3);
        assertThat(englishMessages.size()).isEqualTo(2);
        assertThat(messagesAsPerLocale.get(0).getLocale()).isEqualTo(MR_IN);
        assertThat(messagesAsPerLocale.get(1).getLocale()).isEqualTo(MR_IN);
        assertThat(messagesAsPerLocale.get(2).getLocale()).isEqualTo(MR_IN);
        assertThat(messagesAsPerLocale.get(3).getLocale()).isEqualTo(EN_IN);
        assertThat(messagesAsPerLocale.get(4).getLocale()).isEqualTo(EN_IN);
    }


    @Test
    public void test_should_get_all_messages_when_locale_isenglish() {
        List<Message> englishModelMessages = getEnglishModelMessages();
        when(messageRepository.findByTenantIdAndLocale(TENANT_ID,EN_IN)).thenReturn(englishModelMessages);
        List<Message> messages=messageService.getMessagesAsPerLocale(EN_IN,TENANT_ID);
        assertThat(messages.size()).isEqualTo(5);
        List<Message> englishMessages= messages.stream().filter(message->message.getLocale().equals(EN_IN)).collect(Collectors.toList());
        assertThat(englishMessages.size()).isEqualTo(5);
        assertThat(messages.get(0).getLocale()).isEqualTo(EN_IN);
        assertThat(messages.get(1).getLocale()).isEqualTo(EN_IN);
        assertThat(messages.get(2).getLocale()).isEqualTo(EN_IN);
        assertThat(messages.get(3).getLocale()).isEqualTo(EN_IN);
        assertThat(messages.get(4).getLocale()).isEqualTo(EN_IN);
    }

    @Test
    public void test_should_save_all_entity_messages()
    { List<Message> modelMessages=getMessages();
       when(messageRepository.saveAllEntities(getEntityMessages())).thenReturn(modelMessages);
      List<Message> messages= messageService.saveAllEntityMessages(getEntityMessages());
      assertThat(messages.size()).isEqualTo(4);
      List<Message> modelMessagesWhereLocalIsEnglish= messages.stream().filter(message->message.getLocale().equals(EN_IN)).collect(Collectors.toList());
        List<Message> modelMessagesWhereLocalIsElseThanEnglish= messages.stream().filter(message->message.getLocale().equals(MR_IN)).collect(Collectors.toList());

        assertThat(modelMessagesWhereLocalIsEnglish.size()).isEqualTo(2);
        assertThat(modelMessagesWhereLocalIsElseThanEnglish.size()).isEqualTo(2);
    }

    private List<org.egov.persistence.entity.Message> getEntityMessages() {

        org.egov.persistence.entity.Message message1 =  org.egov.persistence.entity.Message .builder().message("OTP यशस्वीपणे प्रमाणित").code("core.msg.OTPvalidated").locale(MR_IN).tenantId(TENANT_ID).build();
        org.egov.persistence.entity.Message message2=org.egov.persistence.entity.Message.builder().message("प्रतिमा यशस्वीरित्या अपलोड").code("core.lbl.imageupload").locale(MR_IN).tenantId(TENANT_ID).build();
        org.egov.persistence.entity.Message message3=org.egov.persistence.entity.Message.builder().message("EnterMobileNumber").code("core.msg.entermobileno").locale(EN_IN).tenantId(TENANT_ID).build();
        org.egov.persistence.entity.Message message4=org.egov.persistence.entity.Message.builder().message("Enter fullname").code("core.msg.enterfullname").locale(EN_IN).tenantId(TENANT_ID).build();
        return (Arrays.asList(message1, message2, message3,message4));

    }

    private List<Message> getMessages() {
        Message message1 = Message.builder().code("core.msg.OTPvalidated").message("OTP यशस्वीपणे प्रमाणित")
            .locale(MR_IN).tenantId(TENANT_ID).build();
        Message message2 = Message.builder().code("core.lbl.imageupload").message("प्रतिमा यशस्वीरित्या अपलोड")
            .locale(MR_IN).tenantId(TENANT_ID).build();
        Message message3 = Message.builder().code("core.msg.entermobileno").locale(EN_IN)
            .message("EnterMobileNumber").tenantId(TENANT_ID).build();
        Message message4 = Message.builder().code("core.msg.enterfullname").locale(EN_IN)
            .message("Enter fullname").tenantId(TENANT_ID).build();

        return (Arrays.asList(message1, message2, message3,message4));
    }




    private List<Message> getMarathiModelMessages() {
        Message message1 = Message.builder().code("core.msg.OTPvalidated").message("OTP यशस्वीपणे प्रमाणित")
            .locale(MR_IN).tenantId(TENANT_ID).build();
        Message message2 = Message.builder().code("core.lbl.imageupload").message("प्रतिमा यशस्वीरित्या अपलोड")
            .locale(MR_IN).tenantId(TENANT_ID).build();
        Message message3 = Message.builder().code("core.lbl.boundary").message("सीमा")
            .locale(MR_IN).tenantId(TENANT_ID).build();
        return Arrays.asList(message1, message2, message3);
    }


    private List<Message> getEnglishModelMessages() {
        Message message1 = Message.builder().code("core.msg.OTPvalidated").locale(EN_IN)
            .message("OTP validated successfully").tenantId(TENANT_ID).build();
        Message message2 = Message.builder().code("core.lbl.imageupload").locale(EN_IN)
            .message("Image Uploaded Successfully").tenantId(TENANT_ID).build();
        Message message3 = Message.builder().code("core.lbl.boundary").locale(EN_IN)
            .message("Boundary").tenantId(TENANT_ID).build();
        Message message4 = Message.builder().code("core.msg.entermobileno").locale(EN_IN)
            .message("EnterMobileNumber").tenantId(TENANT_ID).build();
        Message message5 = Message.builder().code("core.msg.enterfullname").locale(EN_IN)
            .message("Enter fullname").tenantId(TENANT_ID).build();
        return Arrays.asList(message1, message2, message3, message4, message5);


    }
}