package com.dongVu1105.post_service.repository;

import com.dongVu1105.post_service.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findAllByPostId (String postId);
    Optional<Comment> findById(String id);
}
