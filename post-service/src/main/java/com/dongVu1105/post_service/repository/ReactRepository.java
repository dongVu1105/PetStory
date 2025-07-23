package com.dongVu1105.post_service.repository;

import com.dongVu1105.post_service.entity.React;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactRepository extends MongoRepository<React, String> {
    React findByPostIdAndAndUserId (String postId, String userId);
    List<React> findAllByPostId (String postId);
    long countByPostId (String postId);
}
