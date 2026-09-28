package com.platform.study.service.service;

import com.platform.study.service.model.request.SessionRequest;
import com.platform.study.service.model.response.SessionResponse;
import jakarta.validation.constraints.NotBlank;

public interface StudyService {
    SessionResponse fetchDueCards( String userId, SessionRequest sessionRequest);
}
