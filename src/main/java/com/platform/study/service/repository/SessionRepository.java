package com.platform.study.service.repository;

import com.platform.study.service.entity.SessionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionRepository extends MongoRepository<SessionEntity, String> {
}
