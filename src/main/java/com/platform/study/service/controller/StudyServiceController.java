package com.platform.study.service.controller;

import com.platform.study.service.model.request.ReviewRequest;
import com.platform.study.service.model.request.SessionRequest;
import com.platform.study.service.model.response.ReviewResponse;
import com.platform.study.service.model.response.SessionResponse;
import com.platform.study.service.service.ReviewService;
import com.platform.study.service.service.StudyService;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.platform.study.service.util.ApplicationConstant.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/sessions")
public class StudyServiceController extends BaseApiController{

    private final StudyService studyService;
    private final ReviewService reviewService;

    public StudyServiceController(StudyService studyService, ReviewService reviewService) {
        this.studyService = studyService;
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<SessionResponse> dueCards(@NotBlank(message = HEADER_VALIDATION) @RequestHeader("X-User-Id") String userId,
                                                    @RequestBody SessionRequest sessionRequest){
        log.info("Entering into dueCards method");

        SessionResponse response = studyService.fetchDueCards(userId, sessionRequest);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{sessionId}/reviews")
    public ResponseEntity<ReviewResponse> reviews(@NotBlank(message = HEADER_VALIDATION) @RequestHeader("X-User-Id") String userId,
                                                 @RequestBody ReviewRequest reviewRequest,
                                                  @PathVariable("sessionId") String sessionId){
        log.info("Entering into reviews method");

        ReviewResponse reviewResponse = reviewService.reviewCard(userId, reviewRequest, sessionId);

        return ResponseEntity.status((HttpStatus.OK)).body(reviewResponse);
    }
}
