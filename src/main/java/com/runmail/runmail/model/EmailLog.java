package com.runmail.runmail.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class EmailLog {

    @Id
    private String id;
    private String userId;
    private LocalDateTime timestamp;
}
