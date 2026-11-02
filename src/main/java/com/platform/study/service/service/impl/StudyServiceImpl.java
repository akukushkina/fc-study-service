package com.platform.study.service.service.impl;

import com.platform.study.service.entity.SessionEntity;
import com.platform.study.service.exception.ForbiddenException;
import com.platform.study.service.exception.ResourceNotFoundException;
import com.platform.study.service.model.request.ReviewRequest;
import com.platform.study.service.model.request.SessionRequest;
import com.platform.study.service.model.response.DeckOwnerResponse;
import com.platform.study.service.model.response.DueCard;
import com.platform.study.service.model.response.ReviewResponse;
import com.platform.study.service.model.response.SessionResponse;
import com.platform.study.service.repository.SessionRepository;
import com.platform.study.service.service.DeckServiceClient;
import com.platform.study.service.service.StudyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class StudyServiceImpl implements StudyService {

    private final DeckServiceClient deckServiceClient;
    private final SessionRepository sessionRepository;

    public StudyServiceImpl(DeckServiceClient deckServiceClient, SessionRepository sessionRepository) {
        this.deckServiceClient = deckServiceClient;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public SessionResponse fetchDueCards(String userId, SessionRequest sessionRequest) {

        DeckOwnerResponse response = deckServiceClient.getDeckOwner(sessionRequest.getDeckId());
        log.info("deck-card-service returned owner={}, requesting userId={}, response={}", response.getUserId(), userId, response);


        if(!userId.equals(response.getUserId())){
            throw new ForbiddenException("UserId is not matching with ownerId");
        }
        List<DueCard> cardList = deckServiceClient.getDueCards(sessionRequest.getDeckId(), 50);

        if(cardList.isEmpty()){
            throw new ResourceNotFoundException("No cards found for study for deck: " + sessionRequest.getDeckId());
        }

        List<String> idList = new ArrayList<>();

        for(DueCard item : cardList){
            idList.add(item.getId());
        }

        SessionEntity sessionEntity = SessionEntity.builder()
                .sessionId(UUID.randomUUID().toString())
                .userId(userId)
                .deckId(response.getDeckId())
                .cardIds(idList)
                .status("ACTIVE")
                .startedAt(Instant.now())
                .totalCards(idList.size())
                .avgQualityRating(0.0f)
                .build();


        SessionEntity savedEntity = sessionRepository.save(sessionEntity);

        SessionResponse sessionResponse = SessionResponse.builder()
                .sessionId(savedEntity.getSessionId())
                .dueCards(cardList)
                .build();

        return sessionResponse;
    }


}
