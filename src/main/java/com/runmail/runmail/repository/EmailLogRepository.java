package com.runmail.runmail.repository;

import com.runmail.runmail.model.EmailLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailLogRepository  extends MongoRepository<EmailLog, String> {
    List<EmailLog> findByUserIdAndTimestampBetween(String userId, LocalDateTime start, LocalDateTime end);
}
