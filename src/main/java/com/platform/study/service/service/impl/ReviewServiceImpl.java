package com.platform.study.service.service.impl;

import com.platform.study.service.entity.ReviewsEntity;
import com.platform.study.service.entity.SessionEntity;
import com.platform.study.service.exception.*;
import com.platform.study.service.model.request.ReviewRequest;
import com.platform.study.service.model.response.ReviewResponse;
import com.platform.study.service.repository.ReviewRepository;
import com.platform.study.service.repository.SessionRepository;
import com.platform.study.service.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private final SessionRepository sessionRepository;
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(SessionRepository sessionRepository, ReviewRepository reviewRepository) {
        this.sessionRepository = sessionRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public ReviewResponse reviewCard(String userId, ReviewRequest reviewRequest, String sessionId) {
        SessionEntity sessionEntity = sessionRepository.findById(sessionId).orElseThrow(() ->
                new ResourceNotFoundException("No session found for sessionId: " + sessionId));

        if(!sessionEntity.getUserId().equals(userId)){
            throw new ForbiddenException("This session does not belong to this user: !" + userId);
        }

        if(!sessionEntity.getStatus().equals("ACTIVE")){
            throw new SessionIsNotActiveException("This session is not active anymore: " + sessionId);
        }

        if(!sessionEntity.getCardIds().contains(reviewRequest.getCardId())){
            throw new CardDoesnotBelongToSessionException("The card does not belong to this session");
        }

        List<ReviewsEntity> reviewsEntityList = reviewRepository.findAllBySessionId(sessionId);

        return null;
    }
}
