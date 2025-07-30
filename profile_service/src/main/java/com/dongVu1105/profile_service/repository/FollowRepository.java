package com.dongVu1105.profile_service.repository;


import com.dongVu1105.profile_service.entity.Follow;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends Neo4jRepository<Follow, String> {
    Follow findByFollowingIdAndAndFollowerId (String followingId, String followerId);
    List<Follow> findAllByFollowingId (String followingId);
    List<Follow> findAllByFollowerId (String followerId);
}
