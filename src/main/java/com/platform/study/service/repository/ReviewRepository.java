package com.platform.study.service.repository;

import com.platform.study.service.entity.ReviewsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<ReviewsEntity, String> {
    List<ReviewsEntity> findAllBySessionId(String sessionId);
}
