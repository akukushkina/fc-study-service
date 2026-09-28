package com.platform.study.service.controller;

import com.platform.study.service.model.request.SessionRequest;
import com.platform.study.service.model.response.SessionResponse;
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

    public StudyServiceController(StudyService studyService) {
        this.studyService = studyService;
    }

    @PostMapping
    public ResponseEntity<SessionResponse> dueCards(@NotBlank(message = HEADER_VALIDATION) @RequestHeader("X-User-Id") String userId,
                                                    @RequestBody SessionRequest sessionRequest){
        log.info("Entering into dueCards method");

        SessionResponse response = studyService.fetchDueCards(userId, sessionRequest);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
