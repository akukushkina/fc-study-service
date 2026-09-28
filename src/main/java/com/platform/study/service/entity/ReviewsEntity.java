package com.platform.study.service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "reviews")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewsEntity {
    private String reviewId;
    private String sessionId;
    private String cardId;
    private String userId;
    private int qualityRating;
    private int newInterval;
    private float newEaseFactor;
    private Instant reviewedAt;
}
