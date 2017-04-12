package org.egov.persistence.entity;

import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


public class NotificationTest {

    @Test
    public void test_should_create_domain_from_entity() {
        Date date = new Date();
        Notification notificationEntity = new Notification();
        notificationEntity.setId(1L);
        notificationEntity.setRead("N");
        notificationEntity.setReference("crn");
        notificationEntity.setMessageValues("key:value");
        notificationEntity.setMessageCode("message.code");
        notificationEntity.setUserId(0L);
        notificationEntity.setCreatedDate(date);

        org.egov.domain.model.Notification notificationModel = notificationEntity.toDomain();

        assertThat(notificationModel.getId()).isEqualTo(1L);
        assertThat(notificationModel.isRead()).isFalse();
        assertThat(notificationModel.getReference()).isEqualTo("crn");
        assertThat(notificationModel.getMessageValues()).isEqualTo("key:value");
        assertThat(notificationModel.getMessageCode()).isEqualTo("message.code");
        assertThat(notificationModel.getUserId()).isEqualTo(0L);
        assertThat(notificationModel.getCreatedDate()).isEqualTo(date);
    }

    @Test
    public void test_is_read_should_return_false_when_read_is_set_to_no() {
        Notification notification = new Notification();
        notification.setRead("N");

        assertThat(notification.isRead()).isFalse();
    }

    @Test
    public void test_is_read_should_return_true_when_read_is_set_to_yes() {
        Notification notification = new Notification();
        notification.setRead("Y");

        assertThat(notification.isRead()).isTrue();
    }
}