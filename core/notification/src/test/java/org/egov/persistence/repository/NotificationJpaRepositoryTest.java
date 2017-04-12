package org.egov.persistence.repository;

import org.egov.persistence.entity.Notification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NotificationJpaRepositoryTest {

    @Autowired
    NotificationJpaRepository notificationJpaRepository;

    @Test
    @Sql(scripts = { "/sql/clear_notification_table.sql", "/sql/insert_notifications.sql"})
    public void test_should_save_notification() {
        Notification notification = new Notification();
        notification.setMessageCode("message.code");
        notification.setMessageValues("key:value");
        notification.setReference("crn");
        notification.setUserId(1L);
        notification.setRead("N");
        notification.setCreatedBy(0L);
        notification.setCreatedDate(new Date());
        notification.setLastModifiedBy(0L);
        notification.setLastModifiedDate(new Date());
        notificationJpaRepository.save(notification);

        List<Notification> notifications = notificationJpaRepository.findAll();
        assertThat(notifications.size()).isEqualTo(2);
    }

    @Test
    @Sql(scripts = { "/sql/clear_notification_table.sql", "/sql/insert_notifications.sql"})
    public void test_should_mark_notification_as_read() {
        final Notification notification = notificationJpaRepository.findOne(1L);
        assertThat(notification.isRead()).isFalse();

        final int count = notificationJpaRepository.markNotificationAsRead(1L);
        assertThat(count).isEqualTo(1);
    }
}