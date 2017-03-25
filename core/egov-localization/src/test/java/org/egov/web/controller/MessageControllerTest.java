package org.egov.web.controller;

import org.apache.commons.io.IOUtils;
import org.egov.domain.model.RequestContext;
import org.egov.persistence.entity.Message;
import org.egov.persistence.repository.MessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    private static final String TENANT_ID = "tenant123";
    private static final String LOCALE = "mr_IN";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageRepository messageRepository;

    @Test
    public void test_should_fetch_messages_for_given_locale() throws Exception {
        final List<Message> entitMessages = getEntityMessages();
        when(messageRepository.findByTenantIdAndLocale(TENANT_ID, LOCALE))
            .thenReturn(entitMessages);

        mockMvc.perform(get("/messages")
            .param("tenantId", TENANT_ID)
            .param("locale", LOCALE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().json(getFileContents("messagesResponse.json")));
    }

    @Test
    public void test_should_save_new_messages() throws Exception {
        final Message message1 = Message.builder()
            .locale(LOCALE)
            .tenantId(TENANT_ID)
            .code("code1")
            .message("message1")
            .build();
        final Message message2 = Message.builder()
            .locale(LOCALE)
            .tenantId(TENANT_ID)
            .code("code2")
            .message("message2")
            .build();
        List<Message> expectedMessages = Arrays.asList(message1, message2);
        when(messageRepository.save(expectedMessages)).thenReturn(getEntityMessages());

        mockMvc.perform(post("/messages")
            .content(getFileContents("newMessagesRequest.json")).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().json(getFileContents("messagesResponse.json")));
    }

    @Test
    public void
    test_should_return_bad_request_with_error_response_when_create_message_request_does_not_have_mandatory_parameters
        () throws Exception {
        mockMvc.perform(post("/messages")
            .content(getFileContents("newMessagesRequestWithMissingMandatoryParameters.json"))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().json(getFileContents("mandatoryFieldsMissingErrorResponse.json")));
    }

    private String getFileContents(String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader()
                .getResourceAsStream(fileName), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Message> getEntityMessages() {
        final Message message1 = Message.builder()
            .id(1L)
            .locale(LOCALE)
            .tenantId(TENANT_ID)
            .code("code1")
            .message("message1")
            .build();
        final Message message2 = Message.builder()
            .id(2L)
            .locale(LOCALE)
            .tenantId(TENANT_ID)
            .code("code2")
            .message("message2")
            .build();
        return Arrays.asList(message1, message2);
    }
}