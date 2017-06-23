package org.egov.domain.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageTest {
    @Test
    public void test_equality_should_be_true_when_both_instances_of_message_have_same_field_values() {
        final MessageIdentity messageIdentity1 = MessageIdentity.builder()
            .tenant(new Tenant("tenant"))
            .locale("locale")
            .module("module")
            .code("code")
            .build();
        final Message message1 = Message.builder()
            .message("message")
            .messageIdentity(messageIdentity1)
            .build();

        final MessageIdentity messageIdentity2 = MessageIdentity.builder()
            .tenant(new Tenant("tenant"))
            .locale("locale")
            .module("module")
            .code("code")
            .build();
        final Message message2 = Message.builder()
            .message("message")
            .messageIdentity(messageIdentity2)
            .build();

        assertEquals(message1, message2);
    }
}