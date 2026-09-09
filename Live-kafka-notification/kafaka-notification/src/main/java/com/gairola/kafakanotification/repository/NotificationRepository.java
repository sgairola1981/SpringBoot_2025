package com.gairola.kafakanotification.repository;




import com.gairola.kafakanotification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification>
    findByUserIdOrderByCreatedAtDesc(
            String userId
    );

    long countByUserIdAndReadFalse(
            String userId
    );

    List<Notification>
    findByUserIdAndReadFalse(
            String userId
    );
}