package com.platform.study.service.service;

import com.platform.study.service.model.request.ReviewRequest;
import com.platform.study.service.model.response.ReviewResponse;
import jakarta.validation.constraints.NotBlank;

public interface ReviewService {
    ReviewResponse reviewCard(String userId, ReviewRequest reviewRequest, String sessionId);
}
