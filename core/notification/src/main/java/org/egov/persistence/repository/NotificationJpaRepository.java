package org.egov.persistence.repository;

import org.egov.persistence.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {

    @Modifying
    @Query("update Notification n set n.read = 'Y' where n.id = :id")
    int markNotificationAsRead(@Param("id") Long id);
}
