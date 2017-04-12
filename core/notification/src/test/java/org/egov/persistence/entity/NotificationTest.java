package org.egov.persistence.entity;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class NotificationTest {

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