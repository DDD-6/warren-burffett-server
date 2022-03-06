package com.warrenbuffett.server.repository;

import com.warrenbuffett.server.domain.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository  extends JpaRepository<Badge,Long> {
}
