package com.platform.study.service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collation = "sessions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionEntity {
    private String sessionId;
    private String userId;
    private String deckId;
    private String status;
    private Instant startedAt;
    private Instant completedAt;
    private int totalCards;
    private float avgQualityRating;
}
