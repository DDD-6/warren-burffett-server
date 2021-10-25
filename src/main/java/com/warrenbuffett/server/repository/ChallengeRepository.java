package com.warrenbuffett.server.repository;

import com.warrenbuffett.server.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge,Long> {
}
