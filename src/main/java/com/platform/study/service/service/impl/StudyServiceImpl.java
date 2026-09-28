package com.platform.study.service.service.impl;

import com.platform.study.service.mapper.StudyMapper;
import com.platform.study.service.model.request.SessionRequest;
import com.platform.study.service.model.response.SessionResponse;
import com.platform.study.service.service.StudyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class StudyServiceImpl implements StudyService {

    private final WebClient webClient;
    private final StudyMapper studyMapper;

    public StudyServiceImpl(WebClient webClient, StudyMapper studyMapper) {
        this.webClient = webClient;
        this.studyMapper = studyMapper;
    }

    @Override
    public SessionResponse fetchDueCards(String userId, SessionRequest sessionRequest) {

        // TODO: get webclient call to  /internal/decks/deckId/owner, check if deck exists
//        TODO: cgeck id deckOwner is == userId
//        TODO: webClient get call to /internal/decks/deckId/due-cards?limit=50
//        TODO: check if cards returned > 0
//        TODO: create study session document with status=active, startedAt=now and cardIds
//        TODO: return session Id and ordered list of due cards
        return null;
    }
}
