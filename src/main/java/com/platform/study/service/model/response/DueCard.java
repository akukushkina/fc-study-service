package com.platform.study.service.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DueCard {
    private String id;
    private String deckId;
    private String userId;
    private String front;
    private String back;
    private int repetition;
    private Float easeFactor;
    private int interval;
    private LocalDate nextReviewDate;
    private Instant createdAt;
    private Instant updatedAt;
}
