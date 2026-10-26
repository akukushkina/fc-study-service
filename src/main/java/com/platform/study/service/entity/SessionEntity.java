package com.platform.study.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "sessions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionEntity {
    @Id
    private String sessionId;
    @JsonProperty("userId")
    private String userId;
    private String deckId;
    private List<String> cardIds;
    private String status;
    private Instant startedAt;
    private Instant completedAt;
    private int totalCards;
    private float avgQualityRating;
}
